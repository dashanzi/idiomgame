package dashanzi.android.dto.request;

import dashanzi.android.dto.Message;


public class RequestMsg implements Message{

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
