package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.DWORD;

public class SID_PING_r extends _rPacket {

	public DWORD dPing_Value;

	public SID_PING_r() {
		super(ID.SID_PING);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		dPing_Value = DWORD.readDWORD(bis);
	}
}
