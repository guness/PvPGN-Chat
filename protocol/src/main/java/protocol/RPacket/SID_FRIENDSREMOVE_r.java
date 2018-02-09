package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_FRIENDSREMOVE_r extends _rPacket {

	public byte Entry_Number;

	public SID_FRIENDSREMOVE_r() {
		super(ID.SID_FRIENDSREMOVE);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		Entry_Number = bis.readByte();
	}
}
