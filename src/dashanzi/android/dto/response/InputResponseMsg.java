package dashanzi.android.dto.response;

public class InputResponseMsg extends ResponseMsg{

	private String gid;
	private String word;
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
}
