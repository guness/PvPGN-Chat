package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_AUTH_ACCOUNTUPGRADEPROOF_r extends _rPacket {

	public SID_AUTH_ACCOUNTUPGRADEPROOF_r() {
		super(ID.SID_AUTH_ACCOUNTUPGRADEPROOF);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_AUTH_ACCOUNTUPGRADEPROOF);
	}
}
