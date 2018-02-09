package bnet.pvpgn.chat.util;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bnet.pvpgn.chat.model.Chat;

public class ChatsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Chat> mChats;
    private int mBuffer;

    public ChatsAdapter(Context context, int buffer) {
        mContext = context;
        mChats = new ArrayList<Chat>();
        mBuffer = buffer;
    }

    public void setChatsList(ArrayList<Chat> chats) {
        mChats = chats;
        notifyDataSetChanged();
    }

    public synchronized void addChat(String user, String text, byte eid, boolean isAdmin) {
        mChats.add(new Chat(user, text, eid, isAdmin));
        if (mChats.size() > mBuffer && mBuffer != 0) {
            mChats.remove(0);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mChats.size();
    }

    @Override
    public Object getItem(int position) {
        return mChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            return mChats.get(position).createView(mContext);
        } else {
            return mChats.get(position).convertView(mContext, (TextView) convertView);
        }
    }

    public ArrayList<? extends Parcelable> getChats() {
        return mChats;
    }
}
