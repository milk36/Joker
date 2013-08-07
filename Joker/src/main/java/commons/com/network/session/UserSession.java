package com.network.session;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;


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

	public void write(ByteBuf buff, boolean isClose) {
		if (channel == null)
			return;
		ChannelFuture future = channel.writeAndFlush(buff);
		if (isClose)
			future.addListener(ChannelFutureListener.CLOSE);// 添加关闭监听
	}
}
