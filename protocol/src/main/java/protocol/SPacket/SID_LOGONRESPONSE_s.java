package protocol.SPacket;

import protocol.ID;
import protocol.types.ByteArray;
import protocol.types.DWORD;
import protocol.types.STRING;

public class SID_LOGONRESPONSE_s extends _sPacket {

	public DWORD dClient_Token, dServer_Token, dPassword_Hash[] = new DWORD[5];
	public STRING sUsername;

	public SID_LOGONRESPONSE_s() {
		super(ID.SID_LOGONRESPONSE);
	}

	@Override
	protected void prepareData() {
		ByteArray data = dClient_Token.getByteArray();
		data = data.concat(dServer_Token);
		data = data.concat(dPassword_Hash);
		data = data.concat(sUsername);
		setData(data);
	}
}
