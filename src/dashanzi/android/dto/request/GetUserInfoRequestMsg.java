package dashanzi.android.dto.request;

/**
 * 玩家信息查询请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class GetUserInfoRequestMsg extends RequestMsg {
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
		return "GetUserInfoRequestMsg [gid=" + gid + ", uid=" + uid
				+ ", getType()=" + getType() + "]";
	}
}
