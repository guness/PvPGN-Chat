package protocol.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import protocol.types.DWORD;

public class Channel extends Observable {
	private static final String TAG = "Channel";

	private volatile static Channel instance = null;

	private HashMap<String, User> userlist;
	private String name;
	private DWORD flag;

	private Channel() {
		name = Settings.FirstChannel;
		userlist = new HashMap<String, User>();
	}

	public synchronized void init(String name) {
		this.name = name;
		userlist.clear();
		Engine.printLog('i', TAG, "initing " + name);
		setChanged();
		notifyObservers(new Object[] { "i", name });
	}

	public synchronized static Channel getInstance() {
		if (instance == null) {
			instance = new Channel();
		}
		return instance;

	}

	public ArrayList<User> getUserList() {
		return new ArrayList<User>(userlist.values());
	}

	public synchronized void addUser(User user) {
		userlist.put(user.getUsername(), user);
		setChanged();
		notifyObservers(new Object[] { "a", user });
	}

	public synchronized boolean removeUser(User user) {
		boolean b = userlist.remove(user.getUsername()) != null;
		setChanged();
		notifyObservers(new Object[] { "r", user });
		return b;
	}

	public synchronized boolean removeUser(String user) {
		User u = userlist.remove(user);
		boolean b = u != null;
		setChanged();
		notifyObservers(new Object[] { "r", u });
		return b;
	}

	public synchronized String getChannelName() {
		return name;
	}

	public synchronized void updateChannelFlag(DWORD flag) {
		this.flag = flag;
	}

	public synchronized DWORD getFlag() {
		return flag;
	}

	public synchronized void updateUserFlag(String username, DWORD flag) {
		if (username.equalsIgnoreCase(Session.getInstance().getUsername())) {
			Session.getInstance().setFlags(flag);
		}
		User u = userlist.get(username);
		if (u != null) {
			u.setFlag(flag);
		}
	}
}
