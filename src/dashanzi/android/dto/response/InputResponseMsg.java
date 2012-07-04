package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.User;

/**
 * 提交成语响应
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class InputResponseMsg extends ResponseMsg {

	private String gid;
	private String word;
	private String answer;
	private List<User> users;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	private String uid;
	private String nextUid;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
		return "InputResponseMsg [gid=" + gid + ", word=" + word + ", answer="
				+ answer + ", uid=" + uid + ", nextUid=" + nextUid
				+ ", getType()=" + getType() + ", getStatus()=" + getStatus()
				+ "]";
	}
}
