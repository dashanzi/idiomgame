package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.User;

/**
 * 使用锦囊响应
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class HelpResponseMsg extends ResponseMsg {
	private String gid;
	private String uid;
	private String helpNum;
	private String word;
	private String nextUid;
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

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getNextUid() {
		return nextUid;
	}

	public void setNextUid(String nextUid) {
		this.nextUid = nextUid;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "HelpResponseMsg [gid=" + gid + ", uid=" + uid + ", word="
				+ word + ", nextUid=" + nextUid + ", getType()=" + getType()
				+ ", getStatus()=" + getStatus() + "]";
	}

	public String getHelpNum() {
		return helpNum;
	}

	public void setHelpNum(String helpNum) {
		this.helpNum = helpNum;
	}
}
