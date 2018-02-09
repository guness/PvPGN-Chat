package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.types.DWORD;

public class SID_AUTH_ACCOUNTLOGON_r extends _rPacket {

	public DWORD dStatus;
	public byte[] Salt = new byte[32];
	public byte[] Server_Key = new byte[32];

	public SID_AUTH_ACCOUNTLOGON_r() {
		super(ID.SID_AUTH_ACCOUNTLOGON);
	}

	@Override
	protected void readData(BNetInputStream is) throws IOException {
		dStatus = DWORD.readDWORD(is);
		is.read(Salt);
		is.read(Server_Key);
	}

}
