package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.STRING;

public class SID_CLANMEMBERSTATUSCHANGE_r extends _rPacket {
	public STRING sUsername;
	public byte bRank, bStatus;
	public STRING sLocation;

	public SID_CLANMEMBERSTATUSCHANGE_r() {
		super(ID.SID_CLANMEMBERSTATUSCHANGE);
	}

	@Override
	protected void readData(BNetInputStream is) throws IOException {
		sUsername = STRING.readSTRING(is);
		bRank = is.readByte();
		bStatus = is.readByte();
		sLocation = STRING.readSTRING(is);
	}
}
