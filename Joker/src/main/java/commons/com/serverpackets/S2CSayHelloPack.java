package com.serverpackets;

import io.netty.channel.Channel;

import java.io.IOException;

import com.network.protocol.GameServerPacket;
import com.protocol.S2C.S2CProtos.S2CSayHello;

public class S2CSayHelloPack extends GameServerPacket {

	private S2CSayHello.Builder protobuf;

	static int opCode = 101;// 协议号

	public S2CSayHelloPack(Channel channel, S2CSayHello.Builder protobuf) {
		super(channel);
		this.protobuf = protobuf;
	}

	@Override
	public void runImpl() throws IOException {
		os.write(protobuf.build().toByteArray());
		bytes = bos.toByteArray();
	}

	public static void sendC(Channel channel, String msg) {
		S2CSayHello.Builder build = S2CSayHello.newBuilder();
		build.setMsg(msg);
		S2CSayHelloPack obj = new S2CSayHelloPack(channel, build);
		obj.send();
	}

	@Override
	public int getOpCode() {
		return opCode;
	}
}
