package protocol.interfaces;

import protocol.types.DWORD;

public interface AppendableTextView {
	public void appendLine(String username, DWORD event, String text, DWORD flag);
}
