package dashanzi.android.dto.request;

/**
 * 使用锦囊请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class HelpRequestMsg extends RequestMsg {
	private String gid;
	private String uid;

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

	@Override
	public String toString() {
		return "HelpRequestMsg [gid=" + gid + ", uid=" + uid + ", getType()="
				+ getType() + "]";
	}
}
