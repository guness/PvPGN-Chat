package protocol.RPacket;
import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;

public class SID_FRIENDSPOSITION_r extends _rPacket {
	
	public byte Old_Position,New_Position;
	
	public SID_FRIENDSPOSITION_r() {
		super(ID.SID_FRIENDSPOSITION);
	}

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		Old_Position = bis.readByte();
		New_Position = bis.readByte();
	}
}
