package com.testnetwork;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TelnetClientPipelineFactory extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("handler", new TelnetClientHandler());
	}

	// public ChannelPipeline getPipeline() throws Exception {
	// // Create a default pipeline implementation.
	// ChannelPipeline pipeline = pipeline();
	//
	// // Add the text line codec combination first,
	// // pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
	// // Delimiters.lineDelimiter()));
	// // pipeline.addLast("decoder", new StringDecoder());
	// // pipeline.addLast("encoder", new StringEncoder());
	//
	// // and then business logic.
	// pipeline.addLast("handler", new TelnetClientHandler());
	//
	// return pipeline;
	// }
}