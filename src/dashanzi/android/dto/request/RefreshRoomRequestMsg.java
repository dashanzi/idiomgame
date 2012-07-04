package dashanzi.android.dto.request;

/**
 * 房间信息刷新请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

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
