package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.User;

/**
 * 房间信息刷新响应
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

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
		return "RefreshRoomResponseMsg [users=" + users + ", getType()="
				+ getType() + ", getStatus()=" + getStatus() + "]";
	}
}
