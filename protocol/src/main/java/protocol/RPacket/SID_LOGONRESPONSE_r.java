package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.DWORD;

public class SID_LOGONRESPONSE_r extends _rPacket {

	public DWORD dStatus;

	public SID_LOGONRESPONSE_r() {
		super(ID.SID_LOGONRESPONSE);
	}

	@Override
	protected void readData(BNetInputStream is) throws IOException {
		dStatus = DWORD.readDWORD(is);
	}

}
