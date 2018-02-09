package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.DWORD;
import protocol.types.STRING;

public class SID_CHATEVENT_r extends _rPacket {

	public DWORD dEvent_ID, dUser_s_Flags, dPing, dIP_Address, dAccount_number,
			dRegistration_Authority;
	public STRING sUsername, sText;

	public SID_CHATEVENT_r() {
		super(ID.SID_CHATEVENT);
	}

	@Override
	protected void readData(BNetInputStream is) throws IOException {
		dEvent_ID = DWORD.readDWORD(is);
		dUser_s_Flags = DWORD.readDWORD(is);
		dPing = DWORD.readDWORD(is);
		dIP_Address = DWORD.readDWORD(is);
		dAccount_number = DWORD.readDWORD(is);
		dRegistration_Authority = DWORD.readDWORD(is);

		sUsername = STRING.readSTRING(is);
		sText = STRING.readSTRING(is);
	}

}
