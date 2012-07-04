package dashanzi.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.dto.request.HeartbeatRequestMsg;

/**
 * 心跳
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class HeartbeatService extends Service {

	private final IBinder binder = new MyBinder();
	private boolean flag = true;

	// ------------- other methods ----------------------------
	public void onCreate() {
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
		Log.i("==HB==", "heatbeat service destroyed");
		super.onDestroy();
	}

	// ------------- inner classes ----------------------------
	public class MyBinder extends Binder {
		public HeartbeatService getService() {
			return HeartbeatService.this;
		}
	}

	public void startHeartbeat(final IdiomGameApp app, final String name) {

		Runnable r = new Runnable() {
			public void run() {
				Log.i("==HB==", "heatbeat service started");
				HeartbeatRequestMsg hrm = new HeartbeatRequestMsg();
				hrm.setType(Constants.Type.HEARTBEAT_REQ);
				hrm.setName(name);

				while (flag) {
					app.sendMessage(hrm);
					try {
						Log.i("==HB==", "heatbeating... ...");
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Log.i("==HB==", "heatbeat service ended.");
			}
		};

		new Thread(r).start();
	}

	public void stopHeartbeat() {
		flag = false;
		Log.i("==HB==", "heatbeat service stopped.");
	}

}
