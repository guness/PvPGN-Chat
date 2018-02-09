package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_LEAVECHAT_r extends _rPacket {

	public SID_LEAVECHAT_r() {
		super(ID.SID_LEAVECHAT);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		throw new UnsupportedOperationException("Packet is not implemented yet: "+ID.SID_LEAVECHAT);
	}
}
