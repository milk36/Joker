package com.network.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.network.session.UserSession;
import com.utils.GroovyUtil;

public class GameProtocolHandler {
	static Logger log = Logger.getLogger(GameProtocolHandler.class);

	private static ConcurrentHashMap<Integer, Class<GameClientPacket>> opClass = new ConcurrentHashMap<Integer, Class<GameClientPacket>>();

	public static void handleProtocol(UserSession session, int opCode, byte[] bytes) {
		try {
			GameClientPacket proto = opClass.get(opCode).newInstance();
			if (proto == null) {
				log.error("protocol is not exist");
			} else {
				proto.setSession(session);
				proto.read(bytes);
				proto.run();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void init() {
		parseC2SPacket();
	}

	public static void parseC2SPacket() {
		Map<? extends Integer, ? extends Class<GameClientPacket>> maps = (Map<? extends Integer, ? extends Class<GameClientPacket>>) GroovyUtil.invokeMethod("c2sPacketClass",
				"getC2SPacketClass", false);
		opClass.putAll(maps);

	}

	public static void main(String[] args) {
		parseC2SPacket();
	}
}
