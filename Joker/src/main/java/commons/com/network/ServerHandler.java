package com.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.network.protocol.GameProtocolHandler;
import com.network.session.SessionManager;
import com.network.session.UserSession;

/**
 * 
 * @author lyh
 * @date 2012-2-29
 */
public class ServerHandler extends SimpleChannelHandler {
	static Logger log = Logger.getLogger(ServerHandler.class);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		try {
			// 设置userssion为当前线程参数
			int channelId = e.getChannel().getId();

			UserSession session = SessionManager.getUserSessionForChannelId(channelId);
			if (session == null) {
				ctx.getChannel().close();
				return;
			}

			// 协议体 : |协议长度 |协议编号|协议内容|--|short|short|....|
			ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
			DataInputStream dis = new DataInputStream(bis);
			int length = dis.readShort();// 读取长度
			log.info("op length:" + length);
			int opCode = dis.readShort();// 协议号
			log.info("op code:" + opCode);
			byte[] readByte = new byte[length - 2];
			dis.read(readByte, 0, length - 2);
			GameProtocolHandler.handleProtocol(session, opCode, readByte);
			// switch (opCode) {
			// case 1:
			// C2SChat chat = C2SChat.parseFrom(readByte);
			// System.out.println(chat.getName() + ":" + chat.getMessage());
			// break;
			// }

		} catch (Exception ex) {
			log.error("com.gameserver.network.ServerHandler errer...", ex);
		} finally {
			// SessionManager.removeLocalSession();
		}
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// 连接建立
		int channelId = e.getChannel().getId();
		UserSession session = SessionManager.getUserSessionForChannelId(channelId);
		if (session != null) {
			SessionManager.removeChannelMap(channelId);
		}
		session = new UserSession(ctx.getChannel());
		SessionManager.putUserSessionForChannelMap(session);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// 连接关闭
		cleanUserSession(ctx);
		
	}

	/**
	 * 当连接中断,清理session操作
	 * 
	 * @param ctx
	 * @author lyh
	 * @date 2012-3-12
	 */
	private void cleanUserSession(ChannelHandlerContext ctx) {
		try {
			int channelId = ctx.getChannel().getId();
			UserSession session = SessionManager.getUserSessionForChannelId(channelId);

			if (session != null) {
				SessionManager.removeChannelMap(channelId);
			}
			session.clean();
			session = null;
			// ctx.setAttachment(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}


