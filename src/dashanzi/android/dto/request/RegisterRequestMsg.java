package dashanzi.android.dto.request;

/**
 * 注册请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class RegisterRequestMsg extends RequestMsg {
	
	private String name;
	private String password;
	private int gender;
	private String headerImageId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getHeaderImageId() {
		return headerImageId;
	}
	public void setHeaderImageId(String headerImageId) {
		this.headerImageId = headerImageId;
	}
	@Override
	public String toString() {
		return "RegisterRequestMsg [name=" + name + ", password=" + password
				+ ", gender=" + gender + ", headerImageId=" + headerImageId
				+ ", getType()=" + getType() + "]";
	}
}
