package com.network;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

/**
 * 
 * @author lyh
 * @date 2012-2-29
 */
public class ServerPipelineFactory implements ChannelPipelineFactory {
	/**
	 * 定时器
	 */
	static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
	public static OrderedMemoryAwareThreadPoolExecutor e = new OrderedMemoryAwareThreadPoolExecutor(16, 0, 0);
	/**
	 * 顺序处理线程池
	 */
	private static final ExecutionHandler executionHandler = new ExecutionHandler(e);

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("executor", executionHandler);
		pipeline.addLast("decoder", new ProtocolDecoder(2048, 0, 2, 0, 0));// 解析协议包,处理断包,粘包问题
		pipeline.addLast("encoder", new ProtocolEncoder(2, false));// 发送协议包,长度2个byte
		pipeline.addLast("timeout", new ReadTimeoutHandler(hashedWheelTimer, 20 * 60));// 读包超时处理,长时间无包上行踢掉
		pipeline.addLast("handler", new ServerHandler());// 协议包处理
		return pipeline;
	}

}


