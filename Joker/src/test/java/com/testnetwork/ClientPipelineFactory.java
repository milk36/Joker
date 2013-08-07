package com.testnetwork;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class ClientPipelineFactory extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// pipeline.addLast("decoder", new ProtocolDecoder(2048, 0, 2, 0, 0));//
		// 解析协议包,处理断包,粘包问题
		pipeline.addLast("bytesDecoder", new ByteArrayDecoder());
		// pipeline.addLast("encoder", new ProtocolEncoder(2, false));//
		// 发送协议包,长度2个byte
		pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
		pipeline.addLast("handler", new ClientHander());

	}

	// @Override
	// public ChannelPipeline getPipeline() throws Exception {
	// ChannelPipeline pipeline = pipeline();
	//
	// pipeline.addLast("handler", new ClientHander());
	// return pipeline;
	// }

}
