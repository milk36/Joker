package com.main;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

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
			bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
			bootstrap.setPipelineFactory(new ServerPipelineFactory());
			bootstrap.bind(new InetSocketAddress(PORT));// 绑定端口
			log.info("MainServer listen on " + PORT);
		} catch (Exception e) {
			log.error("MainServer bootstrap error...", e);
		}
	}
}
