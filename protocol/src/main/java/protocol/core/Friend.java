package protocol.core;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.types.DWORD;
import protocol.types.STRING;

public class Friend {
	public STRING sAccount;
	public byte Location, Status;
	public DWORD dProductID;
	public STRING sLocation_Name;
	
	public static Friend readFriend(BNetInputStream bis) throws IOException {
		Friend friend = new Friend();
		friend.sAccount = STRING.readSTRING(bis);
		friend.Status = bis.readByte();
		friend.Location = bis.readByte();
		friend.dProductID = DWORD.readDWORD(bis);
		friend.sLocation_Name = STRING.readSTRING(bis);
		return friend;
	}
}
