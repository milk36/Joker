package com.testnetwork;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

import com.protocol.C2S.C2SProtos.C2SChat;
import com.protocol.S2C.S2CProtos.S2CSayHello;


public class ClientHander extends SimpleChannelInboundHandler<byte[]> {
	static Logger log = Logger.getLogger(ClientHander.class);


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(bos);

		// os.writeShort(2);// 写入数据
		// os.writeShort(5678);

		// Chat.Builder chatB=Chat.newBuilder();
		// chatB.setName("xiaoxiao");
		// chatB.setMessage("haohaohao...中文");

		// System.out.println(chatB.build().get);

		// byte[] protoclBytes = chatB.build().toByteArray();
		//
		// os.flush();

		C2SChat.Builder chat = C2SChat.newBuilder();
		chat.setName("milk");
		chat.setMessage("hello world...");
		os.writeShort(1);// 协议号
		os.write(chat.build().toByteArray());// 协议体
		byte[] buff = bos.toByteArray();
		os.flush();

		ByteBuf buf = Unpooled.buffer(buff.length + 2);
		buf.writeShort(buff.length);// 长度
		buf.writeBytes(buff);
		ChannelFuture future = ctx.writeAndFlush(buf);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(msg);
		DataInputStream dis = new DataInputStream(bis);
		int length = dis.readShort();// 读取长度
		log.info("op length:" + length);
		int opCode = dis.readShort();// 协议号
		log.info("op code:" + opCode);
		byte[] readByte = new byte[length - 2];// 减去协议号
		dis.read(readByte, 0, length - 2);
		switch (opCode) {
		case 101:
			S2CSayHello obj = S2CSayHello.parseFrom(readByte);
			System.out.println(obj.getMsg());
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
