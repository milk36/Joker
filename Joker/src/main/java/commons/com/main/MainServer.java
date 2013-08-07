package com.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.config.Config;
import com.network.ServerPipelineFactory;
import com.network.protocol.GameProtocolHandler;

public class MainServer {
	static Logger log = Logger.getLogger(MainServer.class);

	private static ServerBootstrap bootstrap = null;
	private static int PORT = 12345;

	public static void main(String[] args) {

		Config.init();
		GameProtocolHandler.init();
		try {
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class).childHandler(new ServerPipelineFactory());
			bootstrap.bind(new InetSocketAddress(PORT));// 绑定端口
			log.info("MainServer listen on " + PORT);
		} catch (Exception e) {
			log.error("MainServer bootstrap error...", e);
		}
	}
}
