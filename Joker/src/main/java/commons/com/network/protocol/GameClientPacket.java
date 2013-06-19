package com.network.protocol;

import com.network.session.UserSession;


public abstract class GameClientPacket implements Cloneable {
	protected long uuid;

	protected UserSession session = null;

	protected byte[] bytes = null;

	public long getUUID() {
		return uuid;
	}

	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	public UserSession getSession() {
		return session;
	}

	public void setSession(UserSession session) {
		this.session = session;
	}

	public void read(byte[] bytes) throws Exception {
		this.bytes = bytes;
		parseProtoBuf();
	}

	/**
	 * 
	 * @Title: parseProtoBuf
	 * @Description: 解析生成Protocol buffer对象
	 * @throws Exception
	 *             void
	 */
	protected abstract void parseProtoBuf() throws Exception;

	public void run() {
		runImpl();
	}

	/**
	 * 运行协议实现方法
	 * 
	 * @date 2010-8-16
	 * @author eric.chan
	 */
	protected abstract void runImpl();

	public GameClientPacket clone() {
		try {
			return (GameClientPacket) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
