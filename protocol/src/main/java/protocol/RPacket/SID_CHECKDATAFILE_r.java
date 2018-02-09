package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_CHECKDATAFILE_r extends _rPacket {

	public SID_CHECKDATAFILE_r() {
		super(ID.SID_CHECKDATAFILE);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_CHECKDATAFILE);
	}
}
