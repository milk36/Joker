package com.network.session;

import io.netty.channel.Channel;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
	private static ConcurrentHashMap<Channel, UserSession> channelMap = new ConcurrentHashMap<Channel, UserSession>();

	public static UserSession getUserSessionForChannelId(Channel channel) {
		return channelMap.get(channel);
	}

	public static void putUserSessionForChannelMap(UserSession session) {
		channelMap.put(session.getChannel(), session);
	}

	public static UserSession removeChannelMap(Channel channel) {
		return channelMap.remove(channel);
	}

	/**
	 * 
	 * @Title: getAllChannelId
	 * @Description: 获取当前所有channelId
	 * @return Set<Integer>
	 */
	public static Set<Channel> getAllChannel() {
		return channelMap.keySet();
	}
}
