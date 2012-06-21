package dashanzi.android.dto.request;

import dashanzi.android.dto.IMessage;


public class RequestMsg implements IMessage{

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
