package dashanzi.android.dto.notify;

import java.util.List;


public class StartNotifyMsg {

	private String type;
	private String gid;
	private List<User> users;
	private String firstuid;
	private String firstword;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getFirstuid() {
		return firstuid;
	}

	public void setFirstuid(String firstuid) {
		this.firstuid = firstuid;
	}

	public String getFirstword() {
		return firstword;
	}

	public void setFirstword(String firstword) {
		this.firstword = firstword;
	}
}
