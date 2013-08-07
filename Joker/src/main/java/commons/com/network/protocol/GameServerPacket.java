package com.network.protocol;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.network.session.SessionManager;
import com.network.session.UserSession;

public abstract class GameServerPacket {

	protected Channel channel;
	protected byte[] bytes;
	protected ByteArrayOutputStream bos;
	protected DataOutputStream os;

	public GameServerPacket(Channel channel) {
		this.channel = channel;
	}

	/**
	 * 
	 * @throws IOException
	 * @Title: head
	 * @Description: 处理头 void
	 */
	public void head() throws IOException {
		bos = new ByteArrayOutputStream();
		os = new DataOutputStream(bos);
		bytes = null;
		os.writeShort(getOpCode());
	}

	public abstract int getOpCode();

	/**
	 * 
	 * @Title: runImpl
	 * @Description: 协议体填充逻辑 void
	 */
	public abstract void runImpl() throws IOException;

	/**
	 * 
	 * @throws IOException
	 * @Title: flush
	 * @Description: 通过channel发送协议 void
	 */
	private void flush() throws IOException {
		os.flush();
		UserSession session = SessionManager.getUserSessionForChannelId(channel);
		if (session != null) {
			// ByteBuf buf = Unpooled.buffer(bytes.length + 2);
			// buf.writeShort(bytes.length);
			ByteBuf buf = Unpooled.buffer(bytes.length);
			buf.writeBytes(bytes);
			session.write(buf, false);
		}
	}

	public void send() {
		try {
			head();
			runImpl();
			flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
