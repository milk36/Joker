package com.network.session;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.Channels;

public class UserSession {

	/**
	 * 频道
	 */
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public UserSession(Channel channel) {
		this.channel = channel;
	}

	public void clean() {
		if (channel != null)
			channel.close();
		this.channel = null;
	}

	public void write(ChannelBuffer buff, boolean isClose) {
		if (channel == null)
			return;
		ChannelFuture future = Channels.write(channel, buff);
		if (isClose)
			future.addListener(ChannelFutureListener.CLOSE);// 添加关闭监听
	}
}
