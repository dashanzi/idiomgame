package dashanzi.android.dto.notify;

import dashanzi.android.dto.request.RequestMsg;

/**
 * 退出房间通知（由客户端发出）
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

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
	@Override
	public String toString() {
		return "QuitNotifyMsg [gid=" + gid + ", uid=" + uid + ", getType()="
				+ getType() + "]";
	}
	
}
