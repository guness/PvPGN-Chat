package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.DWORD;
import protocol.types.STRING;

public class SID_AUTH_CHECK_r extends _rPacket {

	public DWORD dResult;
	public STRING sAdditional_Information;

	public SID_AUTH_CHECK_r() {
		super(ID.SID_AUTH_CHECK);
	}

	@Override
	protected void readData(BNetInputStream is) throws IOException {
		dResult = DWORD.readDWORD(is);
		sAdditional_Information = STRING.readSTRING(is);
	}
}
