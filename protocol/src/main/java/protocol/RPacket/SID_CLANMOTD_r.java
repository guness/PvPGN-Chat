package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_CLANMOTD_r extends _rPacket {

	public SID_CLANMOTD_r() {
		super(ID.SID_CLANMOTD);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_CLANMOTD);
	}
}
