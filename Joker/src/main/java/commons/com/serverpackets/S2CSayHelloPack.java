package com.serverpackets;

import java.io.IOException;

import com.network.protocol.GameServerPacket;
import com.protocol.S2C.S2CProtos.S2CSayHello;

public class S2CSayHelloPack extends GameServerPacket {

	private S2CSayHello.Builder protobuf;

	static int opCode = 101;// 协议号

	public S2CSayHelloPack(int channelId, S2CSayHello.Builder protobuf) {
		super(channelId);
		this.protobuf = protobuf;
	}

	@Override
	public void runImpl() throws IOException {
		os.write(protobuf.build().toByteArray());
		bytes = bos.toByteArray();
	}

	public static void sendC(int channelId, String msg) {
		S2CSayHello.Builder build = S2CSayHello.newBuilder();
		build.setMsg(msg);
		S2CSayHelloPack obj = new S2CSayHelloPack(channelId, build);
		obj.send();
	}

	@Override
	public int getOpCode() {
		return opCode;
	}
}
