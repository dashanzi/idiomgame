package dashanzi.android.dto.notify;

import dashanzi.android.dto.request.RequestMsg;

public class QuitNotifyMsg extends RequestMsg {

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
}
