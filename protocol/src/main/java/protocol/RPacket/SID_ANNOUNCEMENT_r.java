package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_ANNOUNCEMENT_r extends _rPacket {

	public SID_ANNOUNCEMENT_r() {
		super(ID.SID_ANNOUNCEMENT);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_ANNOUNCEMENT);
	}
}
