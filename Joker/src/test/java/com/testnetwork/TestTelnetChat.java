package com.testnetwork;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.protocol.C2S.C2SProtos.C2SChat;

public class TestTelnetChat {
	public static void main(String[] args) throws Exception {
//		// Print usage if no argument is specified.
//		if (args.length != 2) {
//			System.err.println("Usage: " + TestTelnetChat.class.getSimpleName() + " <host> <port>");
//			return;
//		}

		// Parse options.
		String host = "192.168.1.104";
		int port = 12345;

		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Configure the pipeline factory.
		bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());

		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

		// Wait until the connection attempt succeeds or fails.
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		// Read commands from the stdin.
		ChannelFuture lastWriteFuture = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter a name!");
		String name = in.readLine();
		for (;;) {
			String line = in.readLine();
			if (line == null) {
				break;
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bos);

			C2SChat.Builder chat = C2SChat.newBuilder();
			chat.setName(name);
			chat.setMessage(line);
			os.writeShort(1);// 协议号
			os.write(chat.build().toByteArray());// 协议内容
			byte[] buff = bos.toByteArray();
			os.flush();

			ChannelBuffer buf = buffer(buff.length + 2);
			buf.writeShort(buff.length);
			buf.writeBytes(buff);
			lastWriteFuture = Channels.write(channel, buf);// 发送到服务器

			// Sends the received line to the server.
			// lastWriteFuture = channel.write(line + "\r\n");

			// If user typed the 'bye' command, wait until the server closes
			// the connection.
			if (line.toLowerCase().equals("bye")) {
				channel.getCloseFuture().awaitUninterruptibly();
				break;
			}
		}

		// Wait until all messages are flushed before closing the channel.
		if (lastWriteFuture != null) {
			lastWriteFuture.awaitUninterruptibly();
		}

		// Close the connection. Make sure the close operation ends because
		// all I/O operations are asynchronous in Netty.
		channel.close().awaitUninterruptibly();

		// Shut down all thread pools to exit.
		bootstrap.releaseExternalResources();
	}
}
