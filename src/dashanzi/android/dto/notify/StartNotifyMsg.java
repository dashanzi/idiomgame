package dashanzi.android.dto.notify;

import java.util.List;

import dashanzi.android.dto.User;
import dashanzi.android.dto.request.RequestMsg;


public class StartNotifyMsg extends RequestMsg{

	private String gid;
	private List<User> users;
	private String firstuid;
	private String word;


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

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "StartNotifyMsg [gid=" + gid + ", users=" + users
				+ ", firstuid=" + firstuid + ", word=" + word + ", getType()="
				+ getType() + "]";
	}
}
