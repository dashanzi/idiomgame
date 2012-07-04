package dashanzi.android.dto.request;

/**
 * 登陆请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class LoginRequestMsg extends RequestMsg{

	private String name;
	private String password;
	private boolean isRememberPassword;

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

	public boolean isRememberPassword() {
		return isRememberPassword;
	}

	public void setRememberPassword(boolean isRememberPassword) {
		this.isRememberPassword = isRememberPassword;
	}

	@Override
	public String toString() {
		return "LoginRequestMsg [name=" + name + ", password=" + password
				+ ", isRememberPassword=" + isRememberPassword + ", getType()="
				+ getType() + "]";
	}
	
}
