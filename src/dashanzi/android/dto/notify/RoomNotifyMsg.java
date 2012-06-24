package dashanzi.android.dto.notify;

import java.util.List;

import dashanzi.android.dto.User;
import dashanzi.android.dto.request.RequestMsg;

public class RoomNotifyMsg extends RequestMsg {

	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "RoomNotifyMsg [users=" + users + ", getType()=" + getType()
				+ "]";
	}
	
}
