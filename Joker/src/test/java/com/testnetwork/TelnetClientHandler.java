package com.testnetwork;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.protocol.S2C.S2CProtos.S2CSayHello;

public class TelnetClientHandler extends SimpleChannelInboundHandler<byte[]> {

	private static final Logger logger = Logger.getLogger(TelnetClientHandler.class.getName());

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		try {
			// 协议体 : |协议长度 |协议编号|协议内容|--|short|short|....|
			ByteArrayInputStream bis = new ByteArrayInputStream(msg);
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

	// @Override
	// public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
	// throws Exception {
	// if (e instanceof ChannelStateEvent) {
	// logger.info(e.toString());
	// }
	// super.handleUpstream(ctx, e);
	// }
	//
	// @Override
	// public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
	// try {
	// // 协议体 : |协议长度 |协议编号|协议内容|--|short|short|....|
	// ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
	// ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
	// DataInputStream dis = new DataInputStream(bis);
	// int length = dis.readShort();// 读取长度
	// // logger.info("op length:" + length);
	// int opCode = dis.readShort();// 协议号
	// // logger.info("op code:" + opCode);
	// byte[] readByte = new byte[length - 2];
	// dis.read(readByte, 0, length - 2);
	// switch (opCode) {
	// case 101:
	// S2CSayHello sh = S2CSayHello.parseFrom(readByte);
	// System.out.println(sh.getMsg());
	// break;
	//
	// default:
	// break;
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

	// @Override
	// public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	// {
	// logger.log(Level.WARNING, "Unexpected exception from downstream.",
	// e.getCause());
	// e.getChannel().close();
	// }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
		ctx.channel().close();
		// super.exceptionCaught(ctx, cause);
	}
}