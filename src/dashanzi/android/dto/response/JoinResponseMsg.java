package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.User;

public class JoinResponseMsg extends ResponseMsg {

	private String gid;
	private String uid;
	private String helpNum;
	private List<User> users;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "JoinResponseMsg [gid=" + gid + ", uid=" + uid + ", users="
				+ users + ", getType()=" + getType() + ", getStatus()="
				+ getStatus() + "]";
	}

	public String getHelpNum() {
		return helpNum;
	}

	public void setHelpNum(String helpNum) {
		this.helpNum = helpNum;
	}

}
