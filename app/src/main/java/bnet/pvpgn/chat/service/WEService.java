package bnet.pvpgn.chat.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;

import bnet.pvpgn.chat.R;
import bnet.pvpgn.chat.gui.activity.MainActivity;

public class WEService extends Service {

    private WakeLock mWakeLock;
    private WifiLock mWifiLock;
    private final String TAG = "WEService";

    private SoundPool mSoundPool;
    private int mClickId = -1;
    private boolean mClickLoaded = false;

    public class WEBinder extends Binder {
        public WEService getService() {
            return WEService.this;
        }
    }

    @Override
    public void onCreate() {
        // Display a notification about us starting. We put an icon in the
        // status bar.
        startForeground(
                R.string.app_name,
                new NotificationCompat.Builder(this)
                        .setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("WesChat")
                        .setContentIntent(
                                PendingIntent.getActivity(
                                        getApplicationContext(),
                                        0,
                                        new Intent(this, MainActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                                        PendingIntent.FLAG_UPDATE_CURRENT))
                        .setContentText("Unique PvPGN android app.")
                        .build());

        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.setReferenceCounted(false);
        mWakeLock.acquire();

        mWifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
        mWifiLock.acquire();

        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                mClickLoaded = true;
            }
        });
        mClickId = mSoundPool.load(this, R.raw.whisp, 1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mWakeLock.release();
        mWifiLock.release();
        mWakeLock = null;
        mWifiLock = null;
        mSoundPool.release();
        mSoundPool = null;
        mClickId = -1;
        mClickLoaded = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new WEBinder();

    public void playClick() {
        if (mClickLoaded) {
            mSoundPool.play(mClickId, 1, 1, 1, 0, 1);
        }
    }
}
