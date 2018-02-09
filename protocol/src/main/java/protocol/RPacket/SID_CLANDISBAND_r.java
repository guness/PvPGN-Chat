package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_CLANDISBAND_r extends _rPacket {

	public SID_CLANDISBAND_r() {
		super(ID.SID_CLANDISBAND);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_CLANDISBAND);
	}
}
