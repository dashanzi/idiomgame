package dashanzi.android.dto.request;

public class TimeoutRequestMsg extends RequestMsg{
	
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
		return "TimeoutRequestMsg [gid=" + gid + ", uid=" + uid
				+ ", getType()=" + getType() + "]";
	}
	

}
