package dashanzi.android.dto.request;

/**
 * 成语提交请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class InputRequestMsg extends RequestMsg{

	private String gid;
	private String uid;
	private String word;

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

	@Override
	public String toString() {
		return "InputRequestMsg [gid=" + gid + ", uid=" + uid + ", word="
				+ word + ", getType()=" + getType() + "]";
	}
	
}
