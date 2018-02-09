package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_READMEMORY_r extends _rPacket {

	public SID_READMEMORY_r() {
		super(ID.SID_READMEMORY);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_READMEMORY);
	}
}
