package bnet.pvpgn.chat.gui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;

import bnet.pvpgn.chat.C;
import bnet.pvpgn.chat.R;
import bnet.pvpgn.chat.service.WEService;
import bnet.pvpgn.chat.util.LOG;
import protocol.VersionInfo;
import protocol.core.Engine;
import protocol.interfaces.LoginResponseListener;


public class MainActivity extends Activity implements LoginResponseListener {
    private Button login;
    private EditText username, password;
    private View settings, textView1;
    private TextView gateway;
    private CheckBox remember;
    private ProgressBar progressBar1;

    private SharedPreferences prefs;
    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Engine.getInstance().isConnected()) {
            startActivity(new Intent(MainActivity.this, ChatActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            finish();
            return;
        }
        Engine.getInstance().registerLogger(LOG.getInstance());
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.remember);
        progressBar1 = findViewById(R.id.progressBar1);
        settings = findViewById(R.id.settings);
        gateway = findViewById(R.id.gateway);
        textView1 = findViewById(R.id.textView1);

        prefs = getSharedPreferences(C.SETTINGS_FILE, Context.MODE_PRIVATE);
        server = prefs.getString(C.SETTINGS_GATEWAY, C.DEFAULT_GATEWAY);

        remember.setChecked(prefs.getBoolean(C.SETTINGS_REMEMBER, false));
        if (remember.isChecked()) {
            username.setText(prefs.getString(C.SETTINGS_USERNAME, ""));
            password.setText(prefs.getString(C.SETTINGS_PASSWORD, ""));
        }
        settings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginProcess(true, null);
                String u = username.getText().toString();
                String p = password.getText().toString();
                // String c = channel.getText().toString();

                AsyncTask<String, Void, Void> login = new AsyncTask<String, Void, Void>() {

                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            Engine.getInstance().init(params[0], params[1], server,
                                    VersionInfo.valueOf(prefs.getString(C.SETTINGS_TYPE, VersionInfo.W3XP_1285.name())));
                            Engine.getInstance().run();
                            Engine.getInstance().login(MainActivity.this);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                login.execute(u, p);
            }
        });
        prefs.edit().putBoolean(C.ENTERED_FIRST_CHANNEL, false).apply();
        startService(new Intent(MainActivity.this, WEService.class));
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(MainActivity.this, WEService.class));
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        server = prefs.getString(C.SETTINGS_GATEWAY, C.DEFAULT_GATEWAY);
        gateway.setText(server);
        onLoginProcess(false, true);
    }

    @Override
    public void onPause() {
        if (remember.isChecked()) {
            prefs.edit().putBoolean(C.SETTINGS_REMEMBER, true)
                    .putString(C.SETTINGS_USERNAME, username.getText().toString())
                    .putString(C.SETTINGS_PASSWORD, password.getText().toString()).apply();
        } else {
            prefs.edit().putBoolean(C.SETTINGS_REMEMBER, false)
                    .remove(C.SETTINGS_USERNAME)
                    .remove(C.SETTINGS_PASSWORD)
                    .apply();
        }
        if (isFinishing()) {
            stopService(new Intent(MainActivity.this, WEService.class));
        }
        super.onPause();
    }

    private void onLoginProcess(Boolean invis, Boolean enabled) {
        if (invis != null) {
            if (invis) {
                login.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                gateway.setVisibility(View.GONE);
                remember.setVisibility(View.GONE);
                settings.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                progressBar1.setVisibility(View.VISIBLE);
            } else {
                progressBar1.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                gateway.setVisibility(View.VISIBLE);
                remember.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                settings.setVisibility(View.VISIBLE);
            }
        }
        if (enabled != null) {
            login.setEnabled(enabled);
            username.setEnabled(enabled);
            password.setEnabled(enabled);
            settings.setEnabled(enabled);
        }
    }

    @Override
    public void logonSuccess() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                onLoginProcess(false, false);
            }
        });
    }

    @Override
    public void logonFailed(int error) {
        final int text;
        switch (error) {
            case 0:
                text = R.string.wrong_password;
                break;
            case 2:
                text = R.string.no_connection;
                break;
            case 3:
                text = R.string.protocol_error;
                break;
            default:
                text = R.string.protocol_error;
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                onLoginProcess(false, true);
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void channelsLoaded() {
        Intent i = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(i);
    }
}
