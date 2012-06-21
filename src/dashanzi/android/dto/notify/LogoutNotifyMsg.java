package dashanzi.android.dto.notify;

import dashanzi.android.dto.request.RequestMsg;

public class LogoutNotifyMsg extends RequestMsg {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
