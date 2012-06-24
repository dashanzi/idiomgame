package dashanzi.android.dto.response;

public class TimeoutResponseMsg extends ResponseMsg {

	private String gid;
	private String word;
	private String uid;
	private String nextuid;

	
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

	public String getNextuid() {
		return nextuid;
	}

	public void setNextuid(String nextuid) {
		this.nextuid = nextuid;
	}

	@Override
	public String toString() {
		return "TimeoutResponseMsg [gid=" + gid + ", word=" + word + ", uid="
				+ uid + ", nextuid=" + nextuid + ", getType()=" + getType()
				+ ", getStatus()=" + getStatus() + "]";
	}
}
