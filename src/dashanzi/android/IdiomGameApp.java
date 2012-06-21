package dashanzi.android;

import android.app.Activity;
import android.app.Application;
import dashanzi.android.dto.IMessage;

public class IdiomGameApp extends Application {
	private Activity currentActivity;

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

}
