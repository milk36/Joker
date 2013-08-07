package com.testnetwork;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestClient {
	public static void main(String[] args) {
		// ClientBootstrap bootstrap = new ClientBootstrap(new
		// NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
		// Executors.newCachedThreadPool()));
		// // Set up the event pipeline factory.
		// bootstrap.setPipelineFactory(new ClientPipelineFactory());
		// // Start the connection attempt.
		// ChannelFuture future = bootstrap.connect(new
		// InetSocketAddress("192.168.1.104", 12345));
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(new ClientPipelineFactory());
		b.connect("192.168.1.104", 12345);
		System.out.println("TestClient start....");
	}
}
