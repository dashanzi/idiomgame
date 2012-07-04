package dashanzi.android.dto.request;

/**
 * 游戏开始请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

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
