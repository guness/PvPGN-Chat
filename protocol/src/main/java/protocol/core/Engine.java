package protocol.core;

import net.bnubot.util.BNetInputStream;
import net.bnubot.util.BNetOutputStream;

import org.jbls.Hashing.DoubleHash;
import org.jbls.Hashing.SRP;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import protocol.ID;
import protocol.RPacket.SID_AUTH_CHECK_r;
import protocol.RPacket.SID_AUTH_INFO_r;
import protocol.RPacket.SID_CHATEVENT_r;
import protocol.RPacket.SID_CLANMEMBERSTATUSCHANGE_r;
import protocol.RPacket.SID_FRIENDSADD_r;
import protocol.RPacket.SID_FRIENDSLIST_r;
import protocol.RPacket.SID_FRIENDSPOSITION_r;
import protocol.RPacket.SID_FRIENDSREMOVE_r;
import protocol.RPacket.SID_FRIENDSUPDATE_r;
import protocol.RPacket.SID_GETCHANNELLIST_r;
import protocol.RPacket.SID_LOGONRESPONSE_r;
import protocol.RPacket.SID_PING_r;
import protocol.RPacket.SID_unknown_r;
import protocol.SPacket.SID_AUTH_CHECK_s;
import protocol.SPacket.SID_AUTH_INFO_s;
import protocol.SPacket.SID_CHATCOMMAND_s;
import protocol.SPacket.SID_FRIENDSLIST_s;
import protocol.SPacket.SID_FRIENDSUPDATE_s;
import protocol.SPacket.SID_GETCHANNELLIST_s;
import protocol.SPacket.SID_JOINCHANNEL_s;
import protocol.SPacket.SID_LOGONRESPONSE_s;
import protocol.SPacket.SID_PING_s;
import protocol.SPacket._sPacket;
import protocol.VersionInfo;
import protocol.interfaces.AppendableTextView;
import protocol.interfaces.Logger;
import protocol.interfaces.LoginResponseListener;
import protocol.interfaces.SocketStatusListener;
import protocol.types.DWORD;
import protocol.types.STRING;

public class Engine {
    private static final String TAG = "Engine";
    private BNetInputStream bis;
    private BNetOutputStream bos;
    ArrayBlockingQueue<_sPacket> sQueu;
    Thread sender, receiver = null;
    AppendableTextView output = null;
    SocketStatusListener socketStatusListener = null;
    private static Logger log = null;
    private volatile static Engine instance;
    private String lastSender = "";
    private ArrayList<struct> cache;
    private FriendStatusUpdater mFriendStatusUpdater = null;

    private DWORD dServer_Token = null;
    private LoginResponseListener mLoginResponseListener = null;
    private Socket socket = null;
    private boolean isConnected = false;
    private boolean isConnecting = false;
    private boolean isLogoff = false;
    String mUsername, mPassword, mDomain;
    private VersionInfo mVersionInfo;

    private Engine() {
        sQueu = new ArrayBlockingQueue<>(1500);
    }

    public synchronized static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
        }
        return instance;
    }

    public void init(String username, String password, String domain, VersionInfo versionInfo) throws IOException {
        mUsername = username;
        mPassword = password;
        mDomain = domain;
        mVersionInfo = versionInfo;
        sender = new Sender();
        receiver = new Receiver();
        isLogoff = false;
        socket = new Socket(domain, 6112);
        bis = new BNetInputStream(socket.getInputStream());
        bos = new BNetOutputStream(socket.getOutputStream());
        cache = new ArrayList<>();
    }

    /**
     * login must be used at least once before.
     *
     * @param loginResponseListener
     * @throws UnknownHostException
     * @throws IOException
     */
    public void reLogin(LoginResponseListener loginResponseListener) throws IOException {
        sender = new Sender();
        receiver = new Receiver();
        isLogoff = false;
        socket = new Socket(mDomain, 6112);
        bis = new BNetInputStream(socket.getInputStream());
        bos = new BNetOutputStream(socket.getOutputStream());
        cache = new ArrayList<>();
        run();
        mLoginResponseListener = loginResponseListener;
        sQueu.offer(new SID_AUTH_INFO_s(mVersionInfo));
    }

    public void registerLogger(Logger log) {
        Engine.log = log;
    }

    public void unregisterLogger() {
        log = null;
    }

    public static void printLog(char type, String TAG, String msg) {
        if (log == null) {
            System.out.println(TAG + ": \t " + msg);
        } else {
            switch (type) {
                case 'e':
                    log.E(TAG, msg);
                    break;
                case 'w':
                    log.W(TAG, msg);
                    break;
                case 'v':
                    log.V(TAG, msg);
                    break;
                case 'i':
                    log.I(TAG, msg);
                    break;
                case 'd':
                    log.D(TAG, msg);
                    break;
                default:
                    log.WTF(TAG, msg);
                    break;
            }
        }
    }

    public static void printException(String TAG, Throwable e) {
        if (log == null) {
            System.out.print(TAG + ": \t ");
            System.err.println(e);
        } else {
            log.P(TAG, e);
        }
    }

    public void registerSocketListener(SocketStatusListener listener) {
        socketStatusListener = listener;
    }

    public void unregisterSocketListener() {
        socketStatusListener = null;
    }

    public void registerAppendableTextView(AppendableTextView view) {
        if (output == null) {
            output = view;
            for (int i = 0; i < cache.size(); i++) {
                struct s = cache.get(i);
                view.appendLine(s.username, s.event, s.text, s.flag);
            }
            cache.clear();
        } else {
            Engine.printLog('w', TAG, "We are registering it now but, you should first unregister previous view");
            output = view;
        }
    }

    public void unregisterAppendableTextView() {
        output = null;
    }

    public void run() {
        isConnecting = true;
        sender.start();
        receiver.start();
    }

    public void stop() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (sender != null) {
            sender.interrupt();
        }
        if (receiver != null) {
            receiver.interrupt();
        }
        if (mFriendStatusUpdater != null) {
            mFriendStatusUpdater.interrupt();
        }
    }

    public void login(LoginResponseListener loginResponseListener) {
        mLoginResponseListener = loginResponseListener;
        sQueu.offer(new SID_AUTH_INFO_s(mVersionInfo));
    }

    public String getLastSender() {
        return lastSender;
    }

    private class Sender extends Thread {
        @Override
        public void run() {
            while (!isLogoff && (isConnected || isConnecting)) {
                _sPacket sPacket;
                try {
                    sPacket = sQueu.take();
                } catch (InterruptedException e) {
                    break;
                }
                try {
                    sPacket.sendPacket(bos);
                } catch (IOException e) {
                    Session.getInstance().disconnected();
                    Engine.this.stop();
                    if (isLogoff) {
                        isConnected = false;
                        socketStatusListener = null;
                    } else if (isConnected) {
                        isConnected = false;
                        if (socketStatusListener == null) {
                            e.printStackTrace();
                        } else {
                            socketStatusListener.onBreak();
                        }
                    }
                    break;
                }
            }
        }
    }

    private class Receiver extends Thread {
        @Override
        public void run() {
            while (!isLogoff && (isConnected || isConnecting)) {
                try {
                    if (bis.readByte() == (byte) 0xff) {
                        byte messageID = bis.readByte();
                        int result;
                        switch (messageID) {
                            case ID.SID_CHATEVENT:
                                SID_CHATEVENT_r rChatEvent = new SID_CHATEVENT_r();
                                rChatEvent.readPacketWithoutHeader(bis);
                                String user = rChatEvent.sUsername.toString();
                                String text = new String(rChatEvent.sText.toString().getBytes("ISO-8859-1"), "UTF-8");
                                DWORD flag = rChatEvent.dUser_s_Flags;
                                switch (rChatEvent.dEvent_ID.intValue()) {
                                    case ID.EID_SHOWUSER:
                                    case ID.EID_JOIN:
                                        Channel.getInstance().addUser(new User(user, flag));
                                        break;
                                    case ID.EID_LEAVE:
                                        Channel.getInstance().removeUser(user);
                                        break;
                                    case ID.EID_CHANNEL:
                                        Channel.getInstance().updateChannelFlag(flag);
                                        break;
                                    case ID.EID_WHISPER:
                                        lastSender = user;
                                        break;
                                    case ID.EID_USERFLAGS:
                                        Channel.getInstance().updateUserFlag(user, flag);
                                        break;
                                }

                                if (output == null) {
                                    cache.add(new struct(user, rChatEvent.dEvent_ID, text, flag));
                                    System.out.println((user.length() > 0 ? (user + ": ") : "") + text);
                                } else {
                                    output.appendLine(user, rChatEvent.dEvent_ID, text, flag);
                                }
                                break;
                            case ID.SID_PING:
                                SID_PING_r rPing = new SID_PING_r();
                                rPing.readPacketWithoutHeader(bis);
                                SID_PING_s sPing = new SID_PING_s();
                                sPing.dPing_Value = rPing.dPing_Value;
                                if (!sQueu.offer(sPing)) {
                                    Engine.printLog('w', TAG, "Something is not good, abq is full");
                                }
                                break;
                            case ID.SID_AUTH_INFO:
                                SID_AUTH_INFO_r rInfo = new SID_AUTH_INFO_r(mVersionInfo.versionByte);
                                rInfo.readPacketWithoutHeader(bis);
                                dServer_Token = rInfo.dServer_Token;
                                sQueu.offer(new SID_AUTH_CHECK_s(mVersionInfo));
                                break;
                            case ID.SID_AUTH_CHECK:
                                SID_AUTH_CHECK_r rAuthCheck = new SID_AUTH_CHECK_r();
                                rAuthCheck.readPacketWithoutHeader(bis);
                                result = rAuthCheck.dResult.intValue();
                                if (result == 0) {
                                    SRP srp = new SRP(mUsername, mPassword);
                                    srp.set_NLS(2);
                                    SID_LOGONRESPONSE_s sResponse = new SID_LOGONRESPONSE_s();
                                    sResponse.dClient_Token = new DWORD(new byte[]{1, 2, 3, 4});
                                    sResponse.dServer_Token = dServer_Token;
                                    sResponse.sUsername = new STRING(mUsername);
                                    int a[] = DoubleHash.doubleHash(mPassword, sResponse.dClient_Token.intValue(),
                                            dServer_Token.intValue());
                                    sResponse.dPassword_Hash[0] = new DWORD(a[0]);
                                    sResponse.dPassword_Hash[1] = new DWORD(a[1]);
                                    sResponse.dPassword_Hash[2] = new DWORD(a[2]);
                                    sResponse.dPassword_Hash[3] = new DWORD(a[3]);
                                    sResponse.dPassword_Hash[4] = new DWORD(a[4]);
                                    sQueu.offer(sResponse);
                                    dServer_Token = null;
                                } else {
                                    mLoginResponseListener.logonFailed(8); // 8 means protocol error
                                    mLoginResponseListener = null;
                                }

                                break;
                            case ID.SID_CLANMEMBERSTATUSCHANGE:
                                SID_CLANMEMBERSTATUSCHANGE_r rClanMemberStatusChange = new SID_CLANMEMBERSTATUSCHANGE_r();
                                rClanMemberStatusChange.readPacketWithoutHeader(bis);
                                // TODO: clan tabını eklerken bunu da düzelt
                                break;
                            case ID.SID_LOGONRESPONSE:
                                SID_LOGONRESPONSE_r rLogonResponse = new SID_LOGONRESPONSE_r();
                                rLogonResponse.readPacketWithoutHeader(bis);
                                result = rLogonResponse.dStatus.intValue();
                                switch (result) {
                                    case 0x2:
                                    case 0x0:
                                    case 0x3:
                                        mLoginResponseListener.logonFailed(result);
                                        mLoginResponseListener = null;
                                        break;
                                    default:
                                        mLoginResponseListener.logonSuccess();
                                        Engine.printLog('i', TAG, "Logon Success!!!");
                                        isConnected = true;
                                        isConnecting = false;
                                        sQueu.offer(new SID_GETCHANNELLIST_s(mVersionInfo));
                                }
                                break;
                            case ID.SID_GETCHANNELLIST:
                                SID_GETCHANNELLIST_r rChannelList = new SID_GETCHANNELLIST_r();
                                rChannelList.readPacketWithoutHeader(bis);
                                Engine.printLog('i', TAG, "Channel list:");
                                for (STRING s : rChannelList.sChannel_names) {
                                    Session.getInstance().getChannels().add(s.toString());
                                    Engine.printLog('i', TAG, s.toString());
                                }
                                if (!Session.getInstance().getChannels().contains("android")) {
                                    Session.getInstance().getChannels().add("android");
                                }
                                mLoginResponseListener.channelsLoaded();
                                mLoginResponseListener = null;
                                break;
                            case ID.SID_FRIENDSLIST:
                                SID_FRIENDSLIST_r rFriendList = new SID_FRIENDSLIST_r();
                                rFriendList.readPacketWithoutHeader(bis);
                                Session.getInstance().setFriends(rFriendList.friends);
                                break;
                            case ID.SID_FRIENDSPOSITION:
                                SID_FRIENDSPOSITION_r rFriendsPosition = new SID_FRIENDSPOSITION_r();
                                rFriendsPosition.readPacketWithoutHeader(bis);
                                Session.getInstance().repositionFriend(rFriendsPosition.Old_Position,
                                        rFriendsPosition.New_Position);
                                break;
                            case ID.SID_FRIENDSADD:
                                SID_FRIENDSADD_r rFriendsAdd = new SID_FRIENDSADD_r();
                                rFriendsAdd.readPacketWithoutHeader(bis);
                                Session.getInstance().addFriend(rFriendsAdd.friend);
                                break;
                            case ID.SID_FRIENDSREMOVE:
                                SID_FRIENDSREMOVE_r rFriendsRemove = new SID_FRIENDSREMOVE_r();
                                rFriendsRemove.readPacketWithoutHeader(bis);
                                Session.getInstance().removeFriend(rFriendsRemove.Entry_Number);
                                break;
                            case ID.SID_FRIENDSUPDATE:
                                SID_FRIENDSUPDATE_r rFriendsUpdate = new SID_FRIENDSUPDATE_r();
                                rFriendsUpdate.readPacketWithoutHeader(bis);
                                Session.getInstance().updateFriend(rFriendsUpdate.Entry_number, rFriendsUpdate.Location,
                                        rFriendsUpdate.sLocation, rFriendsUpdate.Status);
                                break;
                            default:
                                Engine.printLog('w', TAG, "Unknown or not implemented packet");
                                new SID_unknown_r(messageID).readPacketWithoutHeader(bis);
                        }
                    } else {
                        Engine.printLog('w', TAG, "We missed something!");
                    }
                } catch (IOException e) {
                    Session.getInstance().disconnected();
                    Engine.this.stop();
                    if (isLogoff) {
                        isConnected = false;
                        socketStatusListener = null;
                    } else if (isConnected) {
                        isConnected = false;
                        if (socketStatusListener == null) {
                            e.printStackTrace();
                        } else {
                            socketStatusListener.onBreak();
                        }
                    }
                    break;
                }
            }
        }
    }

    public void sendChatCommand(String cmd) {
        if (cmd.length() == 0) {
            return;
        }
        if (cmd.length() > 5 && cmd.substring(0, 5).equalsIgnoreCase("/join")) {
            Channel.getInstance().init(cmd.substring(6));
        }
        if (cmd.length() > 7 && cmd.substring(0, 7).equalsIgnoreCase("/rejoin")) {
            Channel.getInstance().init(Channel.getInstance().getChannelName());
        }
        SID_CHATCOMMAND_s sChat = new SID_CHATCOMMAND_s();
        sChat.sText = new STRING(cmd);
        sQueu.offer(sChat);
    }

    private class struct {

        public String username, text;
        public DWORD event, flag;

        public struct(String username, DWORD dEvent_ID, String text, DWORD dFlag) {
            this.username = username;
            this.text = text;
            event = dEvent_ID;
            flag = dFlag;
        }
    }

    public void sendFriendListRequest(boolean startAutoUpdate, int delay) {
        sQueu.offer(new SID_FRIENDSLIST_s());
        if (startAutoUpdate) {
            mFriendStatusUpdater = new FriendStatusUpdater(delay);
            mFriendStatusUpdater.start();
        }
    }

    public void joinFirstChannel() {
        Engine.printLog('i', TAG, "Joining First Channel");
        sQueu.offer(new SID_JOINCHANNEL_s(ID.ChannelJoin_First, Settings.FirstChannel));
    }

    public void joinChannel(String channel) {
        sQueu.offer(new SID_JOINCHANNEL_s(ID.ChannelJoin_Forced, channel));
    }

    public void stopAutoUpdateFriendList() {
        if (mFriendStatusUpdater != null) {
            mFriendStatusUpdater.interrupt();
            mFriendStatusUpdater = null;
        }
    }

    public void startLogoff() {
        isLogoff = true;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isLogoff() {
        return isLogoff;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    private class FriendStatusUpdater extends Thread {
        private volatile byte lastFriend = -1;
        private int mDelay = 10000;

        public FriendStatusUpdater(int delay) {
            mDelay = delay;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(mDelay);
                    int numOfFriends = Session.getInstance().getFriends().size();
                    if (numOfFriends > 0) {
                        lastFriend++;
                        lastFriend %= Session.getInstance().getFriends().size();
                        SID_FRIENDSUPDATE_s sFriendUpdate = new SID_FRIENDSUPDATE_s();
                        sFriendUpdate.Index_on_friends_list = lastFriend;
                        sQueu.offer(sFriendUpdate);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

}
