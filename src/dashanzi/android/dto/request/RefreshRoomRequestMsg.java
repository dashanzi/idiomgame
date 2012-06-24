package dashanzi.android.dto.request;

public class RefreshRoomRequestMsg extends RequestMsg {
	private String gid;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	@Override
	public String toString() {
		return "RefreshRoomRequestMsg [gid=" + gid + ", getType()=" + getType()
				+ "]";
	}

}
