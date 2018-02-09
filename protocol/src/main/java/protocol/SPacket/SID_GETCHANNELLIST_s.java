package protocol.SPacket;

import protocol.ID;
import protocol.VersionInfo;
import protocol.types.DWORD;

public class SID_GETCHANNELLIST_s extends _sPacket {
    public VersionInfo mVersionInfo;

    DWORD dProduct_ID;

    public SID_GETCHANNELLIST_s(VersionInfo versionInfo) {
        super(ID.SID_GETCHANNELLIST);
        mVersionInfo = versionInfo;
        dProduct_ID = new DWORD(_sPacket.reverse(versionInfo.product.getBytes()));
    }

    @Override
    protected void prepareData() {
        setData(dProduct_ID.getByteArray());
    }
}
