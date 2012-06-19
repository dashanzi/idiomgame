package dashanzi.android.dto.request;


public class RequestMsg {

	private String type;
	private String uid;
	private String gid;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	@Override
	public String toString() {
		return "RequestMsg [type=" + type + ", uid=" + uid + ", gid=" + gid
				+ "]";
	}
	
}
