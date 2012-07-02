package dashanzi.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.dto.request.HeartbeatRequestMsg;

public class HeartbeatService extends Service {

	private final IBinder binder = new MyBinder();
	private boolean flag = true;

	// ------------- other methods ----------------------------

	public void onCreate() {
		// Log.i("==NET==", "service created");
		super.onCreate();
	}

	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	public int onStartCommand(Intent i, int a, int b) {
		return 0;
	}

	public void onDestroy() {
		// Log.i("==NET==", "service destroyed");
		super.onDestroy();
	}

	// ------------- inner classes ----------------------------
	public class MyBinder extends Binder {
		public HeartbeatService getService() {
			return HeartbeatService.this;
		}
	}

	public void startHeartbeat(IdiomGameApp app, String name) {
		Log.i("==HB==", "heatbeat service started");
		HeartbeatRequestMsg hrm = new HeartbeatRequestMsg();
		hrm.setType(Constants.Type.REFRESHROOM_REQ);
		hrm.setName(name);

		while (flag) {
			app.sendMessage(hrm);
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.i("==HB==", "heatbeat service ended.");
	}

	public void stopHeartbeat() {
		flag = false;
		Log.i("==HB==", "heatbeat service stopped.");
	}

}
