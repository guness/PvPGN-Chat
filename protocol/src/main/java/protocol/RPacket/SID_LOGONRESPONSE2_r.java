package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_LOGONRESPONSE2_r extends _rPacket {

	public SID_LOGONRESPONSE2_r() {
		super(ID.SID_LOGONRESPONSE2);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_LOGONRESPONSE2);
	}
}
