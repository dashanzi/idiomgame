package dashanzi.android.dto.notify;

public class User {
	
	private String uid;
	private String name;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", name=" + name + "]";
	}
}
