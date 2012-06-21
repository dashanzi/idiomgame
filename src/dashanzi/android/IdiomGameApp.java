package dashanzi.android;

import android.app.Activity;
import android.app.Application;
import dashanzi.android.dto.IMessage;

public class IdiomGameApp extends Application {
	private Activity currentActivity;

	// ------------- public method ----------------------------
	/**
	 * used by activities
	 */
	public void sendMessage(IMessage msg) {

	}

	// ------------- setters and getters ----------------------------
	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}

}
