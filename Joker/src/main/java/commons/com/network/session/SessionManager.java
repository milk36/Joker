package com.network.session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
	private static ConcurrentHashMap<Integer, UserSession> channelMap = new ConcurrentHashMap<Integer, UserSession>();

	public static UserSession getUserSessionForChannelId(int channelId) {
		return channelMap.get(channelId);
	}

	public static void putUserSessionForChannelMap(UserSession session) {
		channelMap.put(session.getChannel().getId(), session);
	}

	public static UserSession removeChannelMap(int channelId) {
		return channelMap.remove(channelId);
	}

	/**
	 * 
	 * @Title: getAllChannelId
	 * @Description: 获取当前所有channelId
	 * @return Set<Integer>
	 */
	public static Set<Integer> getAllChannelId() {
		return channelMap.keySet();
	}
}
