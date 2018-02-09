package protocol.RPacket;

import java.io.IOException;
import java.util.ArrayList;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.STRING;

public class SID_GETCHANNELLIST_r extends _rPacket {
	public STRING[] sChannel_names;

	public SID_GETCHANNELLIST_r() {
		super(ID.SID_GETCHANNELLIST);
	}

	@Override
	protected void readData(BNetInputStream is) throws IOException {
		int remains = length - 4;
		ArrayList<STRING> list = new ArrayList<STRING>();
		while (remains > 0) {
			STRING str = STRING.readSTRING(is);
			remains -= str.getByteArray().length();
			if (str.getByteArray().length() > 1) {
				list.add(str);
			}
		}
		sChannel_names = list.toArray(new STRING[] {});
	}

}
