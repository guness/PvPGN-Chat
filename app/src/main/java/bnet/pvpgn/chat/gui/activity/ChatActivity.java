package bnet.pvpgn.chat.gui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import bnet.pvpgn.chat.C;
import bnet.pvpgn.chat.R;
import bnet.pvpgn.chat.model.Chat;
import bnet.pvpgn.chat.service.WEService;
import bnet.pvpgn.chat.util.ChannelListAdapter;
import bnet.pvpgn.chat.util.ChatsAdapter;
import bnet.pvpgn.chat.util.LOG;
import bnet.pvpgn.chat.util.UserListAdapter;
import protocol.ID;
import protocol.core.Channel;
import protocol.core.Engine;
import protocol.core.Session;
import protocol.interfaces.AppendableTextView;
import protocol.interfaces.LoginResponseListener;
import protocol.interfaces.SocketStatusListener;
import protocol.types.DWORD;

public class ChatActivity extends Activity implements AppendableTextView, SocketStatusListener, ServiceConnection,
        LoginResponseListener {

    private static final String TAG = "ChatActivity";
    private Spinner channel_spinner;
    private ArrayAdapter<String> channel_adapter;
    private ImageView open, close;
    private EditText chat_input_text;
    private Button exit_button;
    private ListView users, chat_log;
    private UserListAdapter userListAdapter;
    private RelativeLayout users_tab;
    private ChatsAdapter mChatsAdapter;

    private SharedPreferences mSharedPreferences;
    private WEService mWEService = null;

    private boolean mPlayWhispSounds = true;
    private PopupWindow mPopupWindow;
    private Thread mReconnector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.getInstance().D(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        channel_spinner = findViewById(R.id.label);
        chat_input_text = findViewById(R.id.chat_input_text);
        exit_button = findViewById(R.id.exit_button);
        chat_log = findViewById(R.id.chat_log);
        users = findViewById(R.id.users);
        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        users_tab = findViewById(R.id.users_tab);

        mSharedPreferences = getSharedPreferences(C.SETTINGS_FILE, MODE_PRIVATE);

        mPlayWhispSounds = mSharedPreferences.getBoolean(C.SETTINGS_PLAY_WHISP_SOUNDS, true);

        userListAdapter = new UserListAdapter(this, chat_input_text);
        mChatsAdapter = new ChatsAdapter(this, mSharedPreferences.getInt(C.SETTINGS_CHAT_BUFFER, 40));
        chat_log.setAdapter(mChatsAdapter);

        exit_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                finish();
            }
        });
        chat_input_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("/r ")) {
                    s.replace(0, 2, "/w " + Engine.getInstance().getLastSender());
                }
            }
        });
        chat_input_text.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_NULL) {

                    final String text = chat_input_text.getText().toString();
                    chat_input_text.setText("");
                    if (text.length() == 0) {
                        return true;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Engine.getInstance().sendChatCommand(text);
                        }
                    }, 20);
                    if (handleText(text)) {
                        mChatsAdapter.addChat(Session.getInstance().getUsername() + ": ", text, ID.EID_TALK, false);
                    }
                    handled = true;
                }
                return handled;
            }
        });
        channel_spinner.setSoundEffectsEnabled(true);
        channel_adapter = new ChannelListAdapter(this);
        channel_spinner.setAdapter(channel_adapter);
        // channel_spinner.setSelection(Session.getInstance().getChannels()
        // .indexOf("android"), true);
        final long currentTime = System.currentTimeMillis();
        channel_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String text = (String) parent.getItemAtPosition(pos);
                if (System.currentTimeMillis() > currentTime + 5000
                        && !text.equalsIgnoreCase(Channel.getInstance().getChannelName())) {
                    Engine.getInstance().joinChannel(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        open.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f);
                animation.setDuration(450);

                users_tab.startAnimation(animation);
                users_tab.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open.setVisibility(View.GONE);
                        close.setVisibility(View.VISIBLE);
                        users_tab.setVisibility(View.VISIBLE);
                    }
                }, 450);

            }
        });
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f);
                animation.setDuration(450);

                users_tab.startAnimation(animation);
                users_tab.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open.setVisibility(View.VISIBLE);
                        close.setVisibility(View.GONE);
                        users_tab.setVisibility(View.GONE);
                    }
                }, 450);

            }
        });
        Engine.getInstance().registerSocketListener(this);
    }

    protected boolean handleText(String text) {
        if (text.charAt(0) == '/') {
            return false;
        } else if (text.startsWith(("-bchat log"))) {
            try {
                Toast.makeText(
                        this,
                        getString(R.string.x_users_in_channel, Channel.getInstance().getUserList().size(), Channel
                                .getInstance().getChannelName()), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                LOG.getInstance().P(TAG, e);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        LOG.getInstance().D(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LOG.getInstance().D(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList("chatsList", mChatsAdapter.getChats());
        outState.putInt("channel", channel_spinner.getSelectedItemPosition());
        outState.putInt("visibility", users_tab.getVisibility());
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        LOG.getInstance().D(TAG, "onRestoreInstanceState");
        ArrayList<Chat> list = inState.getParcelableArrayList("chatsList");
        mChatsAdapter.setChatsList(list);

        userListAdapter = new UserListAdapter(Channel.getInstance().getUserList(), this, chat_input_text);
        Channel.getInstance().deleteObservers();
        users.setAdapter(userListAdapter);
        Channel.getInstance().addObserver(userListAdapter);
        userListAdapter.notifyDataSetChanged();
        users.invalidateViews();
        channel_spinner.setSelection(inState.getInt("channel"));

        if (inState.getInt("visibility") == View.VISIBLE) {
            open.setVisibility(View.GONE);
            close.setVisibility(View.VISIBLE);
            users_tab.setVisibility(View.VISIBLE);
        } else {
            open.setVisibility(View.VISIBLE);
            close.setVisibility(View.GONE);
            users_tab.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LOG.getInstance().D(TAG, "onResume");
        Engine.getInstance().unregisterAppendableTextView();
        Engine.getInstance().registerAppendableTextView(this);
        Channel.getInstance().deleteObservers();
        users.setAdapter(userListAdapter);
        Channel.getInstance().addObserver(userListAdapter);

        if (!mSharedPreferences.getBoolean(C.ENTERED_FIRST_CHANNEL, false)) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Engine.getInstance().joinFirstChannel();
                    return null;
                }
            }.execute();
            mSharedPreferences.edit().putBoolean(C.ENTERED_FIRST_CHANNEL, true).apply();
        }
        userListAdapter.notifyDataSetChanged();
        users.invalidateViews();
        bindService(new Intent(ChatActivity.this, WEService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        LOG.getInstance().D(TAG, "onPause");
        Channel.getInstance().deleteObservers();
        unbindService(this);
        if (isFinishing()) {
            mSharedPreferences.edit().putBoolean(C.ENTERED_FIRST_CHANNEL, false).apply();
            LOG.getInstance().D("ChatActivity", "onpause and finishing");
            Engine.getInstance().startLogoff();
            Engine.getInstance().unregisterAppendableTextView();
            Engine.getInstance().unregisterSocketListener();
            Engine.getInstance().stop();
        }
        super.onPause();
    }

    @Override
    public synchronized void appendLine(String username, DWORD eid, String text, DWORD flag) {

        AsyncTask<String, Void, Object[]> adder = new AsyncTask<String, Void, Object[]>() {

            @Override
            protected Object[] doInBackground(String... params) {
                Object[] retValue = new Object[4];
                String username = params[0];
                String text = params[2];
                int flag = Integer.valueOf(params[3]);
                retValue[1] = text;
                retValue[2] = Byte.valueOf(params[1]);
                retValue[3] = Boolean.valueOf(flag == ID.FLAG_Blizzard_Representative.intValue()
                        || flag == ID.FLAG_Channel_Operator.intValue() || flag == ID.FLAG_Speaker.intValue());

                switch (Byte.valueOf(params[1])) {
                    case ID.EID_SHOWUSER:
                    case ID.EID_JOIN:
                    case ID.EID_LEAVE:
                    case ID.EID_USERFLAGS:
                        return null;
                    case ID.EID_WHISPER:
                        retValue[0] = username + " " + getString(R.string.whispers) + ": ";
                        if (mPlayWhispSounds && mWEService != null) {
                            mWEService.playClick();
                        }
                        break;
                    case ID.EID_TALK:
                        retValue[0] = username + ": ";
                        break;
                    case ID.EID_BROADCAST:
                        retValue[0] = getString(R.string.ANNOUNCEMENT) + ": ";
                        break;
                    case ID.EID_CHANNEL:
                        retValue[0] = getString(R.string.joining_channel) + ": ";
                        break;
                    case ID.EID_WHISPERSENT:
                        retValue[0] = getString(R.string.you_whispered_to, username) + ": ";
                        break;
                    case ID.EID_INFO:
                        retValue[0] = getString(R.string.INFO) + ": ";
                        break;
                    case ID.EID_ERROR:
                        retValue[0] = getString(R.string.ERROR) + ": ";
                        break;
                    case ID.EID_EMOTE:
                        retValue[0] = getString(R.string.emote_from, username) + ": ";
                        break;

                    default:
                        return null;
                }
                return retValue;
            }

            @Override
            protected void onPostExecute(Object[] output) {

                if (output != null) {

                    String username = (String) output[0];
                    String text = (String) output[1];
                    Byte eid = (Byte) output[2];
                    boolean isAdmin = (Boolean) output[3];
                    if (eid == ID.EID_CHANNEL) {
                        if (Session.getInstance().getChannels().contains(text)) {
                            if (channel_spinner.getSelectedItem().equals(text)) {
                                return;
                            }
                        } else {
                            Session.getInstance().getChannels().add(text);
                            channel_spinner.setAdapter(channel_adapter);
                            channel_adapter.notifyDataSetChanged();
                        }

                        channel_spinner.setSelection(Session.getInstance().getChannels().indexOf(text));
                    }
                    mChatsAdapter.addChat(username, text, eid, isAdmin);
                }
            }
        };
        adder.execute(username, eid.intValue() + "", text, flag.intValue() + "");
    }

    @Override
    public void onBreak() {
        final boolean reTry = mSharedPreferences.getBoolean(C.SETTINGS_RECONNECT, true);
        if (reTry) {
            setReconnector();
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (reTry) {
                    chat_input_text.setEnabled(false);
                    channel_adapter.notifyDataSetChanged();
                    channel_spinner.invalidate();
                    userListAdapter.notifyDataSetChanged();
                    users.invalidate();
                    if (mPopupWindow == null) {
                        View view = View.inflate(ChatActivity.this, R.layout.reconnect, null);
                        view.findViewById(R.id.imageView2).setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mReconnector != null) {
                                    mReconnector.interrupt();
                                }
                            }
                        });
                        mPopupWindow = new PopupWindow(view);
                        mPopupWindow.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    }
                    mPopupWindow.showAsDropDown(channel_spinner);

                } else {
                    Toast.makeText(ChatActivity.this, R.string.broken_connection, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        WEService.WEBinder binder = (WEService.WEBinder) service;
        mWEService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mWEService = null;
    }

    @Override
    public void logonSuccess() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mPopupWindow.dismiss();
                chat_input_text.setEnabled(true);
            }
        });
    }

    @Override
    public void logonFailed(int error) {
        setReconnector();
    }

    @Override
    public void channelsLoaded() {
        Engine.getInstance().joinFirstChannel();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                channel_adapter.notifyDataSetChanged();
                userListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setReconnector() {
        mReconnector = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(12000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (!Engine.getInstance().isLogoff() && !Engine.getInstance().isConnected()) {
                        Engine.getInstance().reLogin(ChatActivity.this);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mReconnector = null;
            }
        };
        mReconnector.start();
    }
}
