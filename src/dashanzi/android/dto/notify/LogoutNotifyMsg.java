package dashanzi.android.dto.notify;

import dashanzi.android.dto.request.RequestMsg;

/**
 * 退出大厅通知（由客户端发出）
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class LogoutNotifyMsg extends RequestMsg {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "LogoutNotifyMsg [name=" + name + ", getType()=" + getType()
				+ "]";
	}
}
