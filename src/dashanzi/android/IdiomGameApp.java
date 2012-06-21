package dashanzi.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import dashanzi.android.dto.IMessage;

public class IdiomGameApp extends Application {
	private Activity currentActivity;

	private String serverIp = "127.0.0.1";

	private int serverPort = 12345;

	public void onCreate(Bundle savedInstanceState) {

	}

	// ------------- public method ----------------------------
	/**
	 * used by all activities
	 */
	public void sendMessage(IMessage msg) {

	}

	/**
	 * used by all welcome activity
	 */
	public void connect() {

	}

	// ------------- setters and getters ----------------------------
	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
