package dashanzi.android.dto.request;

/**
 * 进入房间请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class JoinRequestMsg extends RequestMsg {
	
	private String gid;
	private String name;
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "JoinRequestMsg [gid=" + gid + ", name=" + name + ", getType()="
				+ getType() + "]";
	}
	
}
