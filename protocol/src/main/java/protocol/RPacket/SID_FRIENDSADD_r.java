package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.core.Friend;

public class SID_FRIENDSADD_r extends _rPacket {

	public Friend friend;

	public SID_FRIENDSADD_r() {
		super(ID.SID_FRIENDSADD);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		friend = Friend.readFriend(bis);
	}
}
