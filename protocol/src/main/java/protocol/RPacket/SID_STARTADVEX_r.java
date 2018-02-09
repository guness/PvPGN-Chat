package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_STARTADVEX_r extends _rPacket {

	public SID_STARTADVEX_r() {
		super(ID.SID_STARTADVEX);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_STARTADVEX);
	}
}
