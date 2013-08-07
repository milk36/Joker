package com.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 
 * @author lyh
 * @date 2012-2-29
 */
public class ServerPipelineFactory extends ChannelInitializer<SocketChannel> {
	// /**
	// * 定时器
	// */
	// static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
	// public static OrderedMemoryAwareThreadPoolExecutor e = new
	// OrderedMemoryAwareThreadPoolExecutor(16, 0, 0);
	// /**
	// * 顺序处理线程池
	// */
	// private static final ExecutionHandler executionHandler = new
	// ExecutionHandler(e);
	//
	// @Override
	// public ChannelPipeline getPipeline() throws Exception {
	// ChannelPipeline pipeline = pipeline();
	// pipeline.addLast("executor", executionHandler);
	// pipeline.addLast("decoder", new ProtocolDecoder(2048, 0, 2, 0, 0));//
	// 解析协议包,处理断包,粘包问题
	// pipeline.addLast("encoder", new ProtocolEncoder(2, false));//
	// 发送协议包,长度2个byte
	// pipeline.addLast("timeout", new ReadTimeoutHandler(hashedWheelTimer, 20 *
	// 60));// 读包超时处理,长时间无包上行踢掉
	// pipeline.addLast("handler", new ServerHandler());// 协议包处理
	// return pipeline;
	// }
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder", new ProtocolDecoder(2048, 0, 2, 0, 0));// 解析协议包,处理断包,粘包问题
		pipeline.addLast("bytesDecoder", new ByteArrayDecoder());
		pipeline.addLast("encoder", new ProtocolEncoder(2, false));// 发送协议包,长度2个byte,false表示不包含长度两个字节,如协议体28,前面两字节就是长度28,而不是30
		pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
		pipeline.addLast("timeout", new ReadTimeoutHandler(20 * 60));// 读包超时处理,长时间无包上行踢掉
		pipeline.addLast("handler", new ServerHandler());// 协议包处理
	}
}


