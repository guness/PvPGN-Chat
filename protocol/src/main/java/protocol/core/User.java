package protocol.core;

import protocol.types.DWORD;

public class User {
	String username;
	DWORD flag;

	public User(String username, DWORD flag) {
		this.username = username;
		this.flag = flag;
	}

	public User(String username) {
		this.username = username;
		this.flag = new DWORD(0);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			if (((User) o).getUsername().equals(username)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public DWORD getFlag() {
		return flag;
	}

	public void setFlag(DWORD flag) {
		this.flag = flag;
	}

}
