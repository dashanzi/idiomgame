package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.User;

public class RefreshRoomResponseMsg extends ResponseMsg {
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "RefreshRoomResponseMsg [users=" + users + "]";
	}
}
