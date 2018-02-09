package bnet.pvpgn.chat.util;

import protocol.core.Session;
import android.content.Context;
import android.widget.ArrayAdapter;

public class ChannelListAdapter extends ArrayAdapter<String> {
	public ChannelListAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item, Session
				.getInstance().getChannels());
	}

}
