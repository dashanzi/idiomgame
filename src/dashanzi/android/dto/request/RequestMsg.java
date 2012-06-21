package dashanzi.android.dto.request;


public class RequestMsg {

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RequestMsg [type=" + type + "]";
	}
}
