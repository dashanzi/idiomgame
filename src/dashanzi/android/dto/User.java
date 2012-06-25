package dashanzi.android.dto;

public class User {
	
	private String uid;
	private String name;
	
	//add
	private int gender;//性别
	private int score;//积分
	private String headerImageId;//头像id
	
	
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getHeaderImageId() {
		return headerImageId;
	}
	public void setHeaderImageId(String headerImageId) {
		this.headerImageId = headerImageId;
	}
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
