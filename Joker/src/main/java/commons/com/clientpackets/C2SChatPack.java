package com.clientpackets;

import com.network.protocol.GameClientPacket;
import com.network.session.SessionManager;
import com.protocol.C2S.C2SProtos.C2SChat;
import com.serverpackets.S2CSayHelloPack;

public class C2SChatPack extends GameClientPacket {
	private C2SChat chat;
	@Override
	protected void parseProtoBuf() throws Exception {
		chat = C2SChat.parseFrom(bytes);
	}

	@Override
	protected void runImpl() {
		String msg=chat.getName() + " say :" + chat.getMessage();
		System.out.println(msg);
		for(Integer id:SessionManager.getAllChannelId()){
			S2CSayHelloPack.sendC(id, msg);
		}
	}

}
