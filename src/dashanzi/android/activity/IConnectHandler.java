package dashanzi.android.activity;

public interface IConnectHandler {

	/**
	 * socket连接成功回调方法
	 */
	public void handle();
	
	/**
	 * socket连接失败、异常回调方法
	 */
	public void exceptionCatch();
}
