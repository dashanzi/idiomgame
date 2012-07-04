package dashanzi.android.dto.request;

import dashanzi.android.dto.IMessage;

/**
 * 请求消息父类
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

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
