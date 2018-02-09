package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.DWORD;
import protocol.types.STRING;

public class SID_FRIENDSUPDATE_r extends _rPacket {

	public byte Entry_number, Location, Status;
	public DWORD dProductID;
	public STRING sLocation;

	public SID_FRIENDSUPDATE_r() {
		super(ID.SID_FRIENDSUPDATE);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		Entry_number = bis.readByte();
		Status = bis.readByte();
		Location = bis.readByte();
		dProductID = DWORD.readDWORD(bis);
		sLocation = STRING.readSTRING(bis);
	}
}
