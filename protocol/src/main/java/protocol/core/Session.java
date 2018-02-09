package protocol.core;

import java.util.ArrayList;

import protocol.types.DWORD;
import protocol.types.STRING;

public class Session {

	private volatile static Session instance;
	private ArrayList<String> channels;
	private ArrayList<Friend> friends;
	private DWORD flags;

	private Session() {
		flags = new DWORD(0);
		channels = new ArrayList<String>();
		friends = new ArrayList<Friend>();
	}

	public synchronized static Session getInstance() {
		if (instance == null) {
			instance = new Session();
		}
		return instance;
	}

	public DWORD getFlags() {
		return flags;
	}

	public void setFlags(DWORD flags) {
		this.flags = flags;
	}

	public ArrayList<String> getChannels() {
		return channels;
	}

	public void setFriends(Friend[] friends) {
		synchronized (friends) {
			this.friends.clear();
			for (Friend f : friends) {
				this.friends.add(f);
			}
		}
	}

	public ArrayList<Friend> getFriends() {
		return friends;
	}

	public void repositionFriend(byte old_Position, byte new_Position) {
		synchronized (friends) {
			Friend f = friends.remove(old_Position);
			friends.add(new_Position, f);
		}
	}

	public void addFriend(Friend friend) {
		synchronized (friends) {
			friends.add(friend);
		}
	}

	public void removeFriend(byte entry_Number) {
		synchronized (friends) {
			friends.remove(entry_Number);
		}
	}

	public void updateFriend(byte entry_number, byte location, STRING sLocation, byte status) {
		synchronized (friends) {
			Friend f = friends.get(entry_number);
			if (f.Location != location) {
				System.out.println(f.sAccount + " updated location " + location);
			}
			if (!f.sLocation_Name.toString().equals(sLocation.toString())) {
				System.out.println(f.sAccount + " updated sLocation_Name " + sLocation);
			}
			if (f.Status != status) {
				System.out.println(f.sAccount + " updated status " + status);
			}
			f.Location = location;
			f.sLocation_Name = sLocation;
			f.Status = status;
		}
	}

	public String getUsername() {
		return Engine.getInstance().mUsername;
	}

	public void disconnected() {
		channels.clear();
		friends.clear();
	}
	// public void printFriends() {
	// synchronized (friends) {
	// for (Friend friend : friends)
	// System.out.println("Friend: " + friend.sAccount + ", Location: " +
	// friend.Location
	// + ", Location Name: " + friend.sLocation_Name + ", Status: " +
	// friend.Status);
	// }
	// }
}
