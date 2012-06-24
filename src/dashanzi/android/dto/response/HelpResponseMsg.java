package dashanzi.android.dto.response;

public class HelpResponseMsg extends ResponseMsg {
	private String gid;
	private String uid;
	private String word;
	private String nextUid;

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

	@Override
	public String toString() {
		return "HelpResponseMsg [gid=" + gid + ", uid=" + uid + ", word="
				+ word + ", nextUid=" + nextUid + ", getType()=" + getType()
				+ ", getStatus()=" + getStatus() + "]";
	}
}
