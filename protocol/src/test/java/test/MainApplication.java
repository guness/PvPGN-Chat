package test;

import java.io.IOException;

import protocol.VersionInfo;
import protocol.core.Engine;
import protocol.interfaces.LoginResponseListener;

public class MainApplication implements LoginResponseListener {
    public static void main(String args[]) throws IOException {
        new MainApplication();
    }

    public MainApplication() {

        try {
            Engine.getInstance().init("sainaetr", "sainaetr", "wc3.theabyss.ru", VersionInfo.W3XP_126A);
            Engine.getInstance().run();
            Engine.getInstance().login(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] reverse(byte[] arr) {
        byte bt[] = new byte[arr.length];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = arr[arr.length - i - 1];
        }
        return bt;
    }

    @Override
    public void logonSuccess() {
        System.out.println("logon success");
    }

    @Override
    public void logonFailed(int error) {
        System.out.println("logon failed: " + error);
    }

    @Override
    public void channelsLoaded() {
        Engine.getInstance().sendFriendListRequest(true, 5000);
        System.out.println("channels loaded");
    }
}
