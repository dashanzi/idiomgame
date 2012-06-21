package dashanzi.android.dto.request;

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
}
