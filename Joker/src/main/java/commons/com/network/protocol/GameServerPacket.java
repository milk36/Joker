package com.network.protocol;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;

import com.network.session.SessionManager;
import com.network.session.UserSession;

public abstract class GameServerPacket {

	protected int channelId;
	protected byte[] bytes;
	protected ByteArrayOutputStream bos;
	protected DataOutputStream os;

	public GameServerPacket(int channelId) {
		this.channelId = channelId;
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
		UserSession session = SessionManager.getUserSessionForChannelId(channelId);
		if (session != null) {
			ChannelBuffer buf = buffer(bytes.length + 2);
			buf.writeShort(bytes.length);
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
