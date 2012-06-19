package dashanzi.android.dto.response;

public class TimeoutResponseMsg extends ResponseMsg {

	private String word;
	private String uid;
	private String nextuid;

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


}
