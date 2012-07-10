package dashanzi.android.activity;

import dashanzi.android.dto.IMessage;
/**
 * 消息接口
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public interface IMessageHandler {
	/**
	 * 收到服务端消息后，进行的消息调用回调函数
	 * @param msg
	 */
	public void onMesssageReceived(IMessage msg);

}
