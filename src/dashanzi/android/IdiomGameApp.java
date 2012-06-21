package dashanzi.android;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import dashanzi.android.activity.IConnectHandler;
import dashanzi.android.dto.IMessage;
import dashanzi.android.service.NetworkService;

public class IdiomGameApp extends Application {
	private Activity currentActivity;
	private NetworkService networkService;

	private String serverIp = "127.0.0.1";
	private int serverPort = 12345;
	private IConnectHandler handler;

	public void onCreate(Bundle savedInstanceState) {

	}

	// ------------- public methods ----------------------------
	/**
	 * used by all activities
	 */
	public void sendMessage(IMessage msg) {
		if (networkService == null) {
			Log.e("sendMessage", "networkService == NULL");
		} else {
			networkService.sendMessage(msg);
		}
	}

	/**
	 * used by all welcome activity
	 */
	public void connect(IConnectHandler handler) {
		this.handler = handler;
		initService();
		// connnectService();
	}

	public void disconnect() {
		networkService.disconnect();
	}

	// ------------- private methods ----------------------------
	private void onMessageReceived(String msg) {

	}

	private void initService() {
		// 1. init service
		Intent intent = new Intent(this, NetworkService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.i("ttt", "Dispatcher() service binded");

		// 2. reg receiver
		MyReceiver receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		// filter.addAction("android.intent.action.test");
		// IdiomGameApp.this.registerReceiver(receiver, filter);
		// Log.i("ttt", "TestadActivity onCreate reg ok");

	}

	// private void connnectService() {
	//
	// try {
	// Thread.sleep(3000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// Log.i("dddd", "=====service=" + networkService);
	// networkService.connect(serverIp, serverPort);
	// }

	private ServiceConnection connection = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			networkService = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("onServiceConnected");
			networkService = ((NetworkService.MyBinder) service).getService();

			networkService.connect(serverIp, serverPort);
			System.out.println("service=" + networkService);

			Log.i("iiiii", "before handle");
			IdiomGameApp.this.handler.handle();
			// networkService.test();
		}
	};

	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("ttt", "DP OnReceive");
			Bundle bundle = intent.getExtras();
			String strMsg = bundle.getString("msg");
			onMessageReceived(strMsg);
			// int a = bundle.getInt("i");
			// Log.i("ttt", "DP int -> " + a);

		}

		public MyReceiver() {
			System.out.println("MyReceiver");
		}

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
