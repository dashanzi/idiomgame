package dashanzi.android.dto.request;


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
}
