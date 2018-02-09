package bnet.pvpgn.chat.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import bnet.pvpgn.chat.R;
import protocol.ID;

public class Chat implements Parcelable {

    private String mFrom, mText;
    private byte mEID;
    private boolean mIsAdmin;

    public Chat(String from, String text, byte eid, boolean isAdmin) {
        mFrom = from;
        mText = text;
        mEID = eid;
        mIsAdmin = isAdmin;
    }

    @Override
    public int describeContents() {
        return 2;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFrom);
        dest.writeString(mText);
        dest.writeByte(mEID);
        dest.writeInt(mIsAdmin ? 1 : 0);
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    private Chat(Parcel in) {
        mFrom = in.readString();
        mText = in.readString();
        mEID = in.readByte();
        mIsAdmin = in.readInt() == 1;
    }

    private static int[] getColors(byte eid, boolean isAdmin) {
        int colors[] = new int[2];
        switch (eid) {
            case ID.EID_WHISPER:
            case ID.EID_WHISPERSENT:
                colors[0] = R.color.channel_sender;
                colors[1] = R.color.channel_whisper;
                break;
            case ID.EID_TALK:
                colors[0] = R.color.channel_sender;
                colors[1] = isAdmin ? R.color.channel_admin : R.color.channel_talk;
                break;
            case ID.EID_BROADCAST:
            case ID.EID_INFO:
                colors[0] = R.color.channel_broadcast;
                colors[1] = R.color.channel_broadcast;
                break;
            case ID.EID_CHANNEL:
                colors[0] = R.color.channel_talk;
                colors[1] = R.color.channel_broadcast;
                break;
            case ID.EID_ERROR:
                colors[0] = R.color.channel_error;
                colors[1] = R.color.channel_error;
                break;
            case ID.EID_EMOTE:
                colors[0] = R.color.channel_emote;
                colors[1] = R.color.channel_emote;
                break;
            default:
                colors[0] = 0;
                colors[1] = 0;
                break;
        }
        return colors;
    }

    public TextView createView(Context context) {
        return convertView(context, new TextView(context));
    }

    public TextView convertView(Context context, TextView textView) {
        int colors[] = getColors(mEID, mIsAdmin);

        Spannable from = null, text = null;
        if (mFrom != null) {
            from = new SpannableString(mFrom);
            from.setSpan(new ForegroundColorSpan(context.getResources()
                            .getColor(colors[0])), 0, from.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (mFrom != null) {
            text = new SpannableString(mText);
            text.setSpan(new ForegroundColorSpan(context.getResources()
                            .getColor(colors[1])), 0, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(text);
        }
        if (mFrom != null) {
            textView.setText(from);
            if (mText != null) {
                textView.append(text);
            }
        } else {
            if (mText != null) {
                textView.setText(text);
            }
        }
        textView.setLinksClickable(true);
        textView.setAutoLinkMask(Linkify.ALL);
        textView.setEms(3);
        textView.setShadowLayer(1, 1, 1,
                context.getResources().getColor(R.color.Black));

        return textView;
    }
}
