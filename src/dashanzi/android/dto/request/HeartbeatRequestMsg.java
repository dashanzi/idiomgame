package dashanzi.android.dto.request;

/**
 * 心跳保活请求
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class HeartbeatRequestMsg extends RequestMsg {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
