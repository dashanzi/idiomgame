package dashanzi.android.dto.response;

public class GetUserInfoResponseMsg extends ResponseMsg {
	
	private String name;
	private String gender;
	private String score;
	private String level;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	@Override
	public String toString() {
		return "GetUserInfoResponseMsg [name=" + name + ", gender=" + gender
				+ ", score=" + score + ", level=" + level + ", getType()="
				+ getType() + ", getStatus()=" + getStatus() + "]";
	}
}
