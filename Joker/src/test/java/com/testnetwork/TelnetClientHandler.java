package com.testnetwork;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.protocol.S2C.S2CProtos.S2CSayHello;

public class TelnetClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(TelnetClientHandler.class.getName());

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			// 协议体 : |协议长度 |协议编号|协议内容|--|short|short|....|
			ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
			DataInputStream dis = new DataInputStream(bis);
			int length = dis.readShort();// 读取长度
			// logger.info("op length:" + length);
			int opCode = dis.readShort();// 协议号
			// logger.info("op code:" + opCode);
			byte[] readByte = new byte[length - 2];
			dis.read(readByte, 0, length - 2);
			switch (opCode) {
			case 101:
				S2CSayHello sh = S2CSayHello.parseFrom(readByte);
				System.out.println(sh.getMsg());
				break;

			default:
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}