package com.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.apache.log4j.Logger;

import com.network.protocol.GameProtocolHandler;
import com.network.session.SessionManager;
import com.network.session.UserSession;

/**
 * 
 * @author lyh
 * @date 2012-2-29
 */
public class ServerHandler extends SimpleChannelInboundHandler<byte[]> {
	static Logger log = Logger.getLogger(ServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		try {
			// 设置userssion为当前线程参数
			Channel channel = ctx.channel();
			
			UserSession userSession = SessionManager.getUserSessionForChannelId(channel);
			if (userSession == null) {
				ctx.channel().close();
				return;
			}
			
			// 协议体 : |协议长度 |协议编号|协议内容|--|short|short|....|
			ByteArrayInputStream bis = new ByteArrayInputStream(msg);
			 DataInputStream dis = new DataInputStream(bis);
			int length = dis.readShort();// 读取长度
			 log.info("op length:" + length);
			int opCode = dis.readShort();// 协议号
			 log.info("op code:" + opCode);
			 byte[] readByte = new byte[length - 2];
			 dis.read(readByte, 0, length - 2);
			 GameProtocolHandler.handleProtocol(userSession, opCode, readByte);
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
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// channelOpen、channelBound和channelConnected被合并为channelActive 激活
		// 连接建立
		Channel channel = ctx.channel();
		UserSession session = SessionManager.getUserSessionForChannelId(channel);
		if (session != null) {
			SessionManager.removeChannelMap(channel);
		}
		session = new UserSession(channel);
		SessionManager.putUserSessionForChannelMap(session);
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// channelDisconnected、channelUnbound和channelClosed被合并为channelInactive
		// 失效
		// 连接关闭
		cleanUserSession(ctx);
		super.channelInactive(ctx);
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
			Channel channel = ctx.channel();
			UserSession session = SessionManager.getUserSessionForChannelId(channel);

			if (session != null) {
				SessionManager.removeChannelMap(channel);
			}
			session.clean();
			session = null;
			// ctx.setAttachment(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}


