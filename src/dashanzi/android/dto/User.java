package dashanzi.android.dto;

/**
 * 玩家信息
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class User {
	
	private String uid;
	private String name;
	
	//add
	private String gender;//性别
	private String score;//积分
	private String headerImageId;//头像id
	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
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
		return "User [uid=" + uid + ", name=" + name + ", gender=" + gender
				+ ", score=" + score + ", headerImageId=" + headerImageId + "]";
	}
	
}
