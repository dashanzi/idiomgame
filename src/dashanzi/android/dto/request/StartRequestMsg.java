package dashanzi.android.dto.request;

public class StartRequestMsg extends RequestMsg {
	private String gid;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	@Override
	public String toString() {
		return "StartRequestMsg [gid=" + gid + ", getType()=" + getType() + "]";
	}
	
}
