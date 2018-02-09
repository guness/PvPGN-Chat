package protocol.RPacket;

import net.bnubot.util.BNetInputStream;

import java.io.IOException;

import protocol.ID;
import protocol.VersionInfo;
import protocol.types.DWORD;
import protocol.types.FILETIME;
import protocol.types.STRING;

public class SID_AUTH_INFO_r extends _rPacket {

    public DWORD dLogon_Type, dServer_Token, dUDPValue;
    public FILETIME fMPQ_filetime;
    public STRING sIX86ver_filename, ValueString;
    public byte[] Server_signature = new byte[128];
    private int mSignatureLength = 0;
    private byte mVersionByte;

    public SID_AUTH_INFO_r(byte versionByte) {
        super(ID.SID_AUTH_INFO);
        mVersionByte = versionByte;
    }

    @Override
    protected void readData(BNetInputStream bis) throws IOException {
        dLogon_Type = DWORD.readDWORD(bis);
        dServer_Token = DWORD.readDWORD(bis);
        dUDPValue = DWORD.readDWORD(bis);
        fMPQ_filetime = FILETIME.readFILETIME(bis);
        sIX86ver_filename = STRING.readSTRING(bis);
        ValueString = STRING.readSTRING(bis);
        if (mVersionByte == VersionInfo.W3XP_1285.versionByte || mVersionByte == VersionInfo.W3XP_126A.versionByte) {
            mSignatureLength = bis.read(Server_signature);
        }
    }
}
