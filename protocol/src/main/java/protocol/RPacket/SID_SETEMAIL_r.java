package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class SID_SETEMAIL_r extends _rPacket {

	public SID_SETEMAIL_r(byte messageID) {
		super(messageID);
	}

	@Override
	protected void readData(BNetInputStream is) throws IOException {
	}

}
