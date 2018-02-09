package bnet.pvpgn.chat.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import bnet.pvpgn.chat.R;
import protocol.core.Engine;
import protocol.core.Session;
import protocol.core.User;

public class UserListAdapter extends BaseAdapter implements Observer {

    private EditText chat_input_text;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<User> userList = new ArrayList<User>();

    private AlertDialog.Builder menuOpener;

    public UserListAdapter(Context context, EditText chat_input_text) {
        this.chat_input_text = chat_input_text;
        this.context = context;
        inflater = LayoutInflater.from(context);
        userList.clear();
    }

    public UserListAdapter(ArrayList<User> users, Context context,
                           EditText chat_input_text) {
        userList.clear();
        userList.addAll(users);
        this.context = context;
        this.chat_input_text = chat_input_text;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User u = (User) getItem(position);
        String uname = u.getUsername();
        TextView tv;
        if (convertView == null) {
            tv = (TextView) inflater.inflate(R.layout.user, null);
        } else {
            tv = (TextView) convertView;
        }

        if (u.getFlag().intValue() == 1) {
            tv.setBackgroundResource(R.drawable.admin);
            tv.setTextColor(context.getResources().getColor(R.color.White));
            tv.setShadowLayer(0, 0, 0, 0);
        } else if (uname.equalsIgnoreCase("sainaetr")) {
            tv.setBackgroundResource(R.drawable.owner);
            tv.setTextColor(context.getResources().getColor(R.color.Blue));
            tv.setShadowLayer(1, 1, 1, R.color.Black);
        } else {
            tv.setBackgroundResource(R.drawable.userlist);
            tv.setTextColor(context.getResources().getColor(R.color.White));
            tv.setShadowLayer(0, 0, 0, 0);
        }
        tv.setText(uname);
        tv.setOnClickListener(new OnUserClicked(u));
        tv.setOnLongClickListener(new OnUserLongClicked(u));
        return tv;
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        final Object[] obs = (Object[]) arg1;
        final String command = (String) obs[0];

        ((Activity) context).runOnUiThread(new Thread() {
            public void run() {
                if (command.equals("a")) {
                    userList.add((User) obs[1]);
                } else if (command.equals("r")) {
                    userList.remove((User) obs[1]);
                } else if (command.equals("i")) {
                    userList.clear();
                }
                notifyDataSetChanged();
            }
        });
    }

    private class OnUserClicked implements OnClickListener {

        User user;

        public OnUserClicked(User user) {
            this.user = user;
        }

        @Override
        public void onClick(View v) {
            chat_input_text.setText("/w " + user.getUsername() + " ");
            chat_input_text.setSelection(chat_input_text.getText().length());
        }
    }

    private class OnUserLongClicked implements OnLongClickListener {

        User toUser;

        public OnUserLongClicked(User toUser) {
            this.toUser = toUser;

        }

        @Override
        public boolean onLongClick(View v) {
            if (toUser.getUsername().equalsIgnoreCase(
                    Session.getInstance().getUsername())) {
                return true;
            }
            if (menuOpener == null) {
                menuOpener = new AlertDialog.Builder(context);

            }
            menuOpener.setTitle(toUser.getUsername());
            if (Session.getInstance().getFlags().intValue() == 1) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter
                        .createFromResource(context, R.array.actions_admin,
                                android.R.layout.simple_spinner_item);
                menuOpener.setAdapter(adapter, new UserActions(true));
            } else {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter
                        .createFromResource(context, R.array.actions_user,
                                android.R.layout.simple_spinner_item);
                menuOpener.setAdapter(adapter, new UserActions(false));
            }
            menuOpener.show();
            return true;
        }

        private class UserActions implements DialogInterface.OnClickListener {

            boolean isAdmin;

            public UserActions(boolean isAdmin) {
                this.isAdmin = isAdmin;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isAdmin) {
                    /*
					 * 
					 * <item >Kick</item> <item >Ban</item> <item >Mute</item>
					 * <item >Unmute</item> <item >Whois</item> <item
					 * >Finger</item> <item >Rat</item> <item >Sd</item> <item
					 * >Squelch</item> <item >Unsquelch</item> <item >Send
					 * Ads</item>
					 */
                    switch (which) {
                        case 0:
                            Engine.getInstance().sendChatCommand(
                                    "/kick " + toUser.getUsername());
                            break;
                        case 1:
                            Engine.getInstance().sendChatCommand(
                                    "/ban " + toUser.getUsername());
                            break;
                        case 2:
                            Engine.getInstance().sendChatCommand(
                                    "/mute " + toUser.getUsername());
                            break;
                        case 3:
                            Engine.getInstance().sendChatCommand(
                                    "/unmute " + toUser.getUsername());
                            break;
                        case 4:
                            Engine.getInstance().sendChatCommand(
                                    "/whois " + toUser.getUsername());
                            break;
                        case 5:
                            Engine.getInstance().sendChatCommand(
                                    "/finger " + toUser.getUsername());
                            break;
                        case 6:
                            Engine.getInstance().sendChatCommand(
                                    "/rat " + toUser.getUsername());
                            break;
                        case 7:
                            Engine.getInstance().sendChatCommand(
                                    "/sd " + toUser.getUsername());
                            break;
                        case 8:
                            Engine.getInstance().sendChatCommand(
                                    "/ignore " + toUser.getUsername());
                            break;
                        case 9:
                            Engine.getInstance().sendChatCommand(
                                    "/unignore " + toUser.getUsername());
                            break;
                    }
                } else {
					/*
					 * <item >Whois</item> <item >Finger</item> <item
					 * >Rat</item> <item >Sd</item> <item >Squelch</item> <item
					 * >Unsquelch</item> <item >Send Ads</item>
					 */
                    switch (which) {
                        case 0:
                            Engine.getInstance().sendChatCommand(
                                    "/whois " + toUser.getUsername());
                            break;
                        case 1:
                            Engine.getInstance().sendChatCommand(
                                    "/finger " + toUser.getUsername());
                            break;
                        case 2:
                            Engine.getInstance().sendChatCommand(
                                    "/rat " + toUser.getUsername());
                            break;
                        case 3:
                            Engine.getInstance().sendChatCommand(
                                    "/sd " + toUser.getUsername());
                            break;
                        case 4:
                            Engine.getInstance().sendChatCommand(
                                    "/ignore " + toUser.getUsername());
                            break;
                        case 5:
                            Engine.getInstance().sendChatCommand(
                                    "/unignore " + toUser.getUsername());
                            break;
                    }
                }
            }
        }
    }
}
