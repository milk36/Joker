package com.testnetwork;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.protocol.C2S.C2SProtos.C2SChat;


public class ClientHander extends SimpleChannelHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.messageReceived(ctx, e);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.channelOpen(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
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
		os.writeShort(1);
		os.write(chat.build().toByteArray());
		byte[] buff = bos.toByteArray();
		os.flush();

		ChannelBuffer buf = buffer(buff.length + 2);
		buf.writeShort(buff.length);
		buf.writeBytes(buff);
		ChannelFuture future = Channels.write(e.getChannel(), buf);
	}
}
