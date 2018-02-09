package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.core.Friend;

public class SID_FRIENDSLIST_r extends _rPacket {

	public byte Number_of_Entries;
	public Friend[] friends;

	public SID_FRIENDSLIST_r() {
		super(ID.SID_FRIENDSLIST);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		Number_of_Entries = bis.readByte();
		friends = new Friend[Number_of_Entries];
		for (byte i = 0; i < Number_of_Entries; i++) {
			friends[i] = Friend.readFriend(bis);
		}
	}
}
