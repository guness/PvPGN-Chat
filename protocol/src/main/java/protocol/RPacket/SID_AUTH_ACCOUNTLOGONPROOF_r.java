package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_AUTH_ACCOUNTLOGONPROOF_r extends _rPacket {

	public SID_AUTH_ACCOUNTLOGONPROOF_r() {
		super(ID.SID_AUTH_ACCOUNTLOGONPROOF);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_AUTH_ACCOUNTLOGONPROOF);
	}
}
