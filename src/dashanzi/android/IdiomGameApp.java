package dashanzi.android;

import org.json.JSONException;

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
import dashanzi.android.activity.IExceptionHandler;
import dashanzi.android.activity.IMessageHandler;
import dashanzi.android.dto.IMessage;
import dashanzi.android.service.HeartbeatService;
import dashanzi.android.service.NetworkService;
import dashanzi.android.util.Json2BeansUtil;

/**
 * 应用程序类
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class IdiomGameApp extends Application {
	private IMessageHandler currentActivity;
	private NetworkService networkService;

	private MyReceiver receiver;
	private boolean aboutThreadIsInterrupt = false;

	private HeartbeatService heartbeatService;
	private ServiceConnection scHeartbeat;

	private String serverIp = "127.0.0.1";
	private int serverPort = 12345;
	private IConnectHandler handler;

	// add TODO
	private String lastRegisterName;
	private String voiceIdiom;
	private int[] femaleHeaderIdArray = { R.drawable.g000, R.drawable.g001,
			R.drawable.g002, R.drawable.g003, R.drawable.g004, R.drawable.g005,
			R.drawable.g006, R.drawable.g007, R.drawable.g008 };

	private int[] manHeaderIdArray = { R.drawable.b000, R.drawable.b001,
			R.drawable.b002, R.drawable.b003, R.drawable.b004, R.drawable.b005,
			R.drawable.b006, R.drawable.b007, R.drawable.b008 };

//	//DFA 敏感词生成树
//	private final Node rootNode = new Node('R');

	public void onCreate(Bundle savedInstanceState) {

		// TODO
//		// init
//		Resources res = getResources();
//		final String[] arr = res.getStringArray(R.array.censor_words);
//		DFAUtil.createTree(arr, rootNode);
//		Log.e("dfa","initial .....");
		
	}

	// ------------- public methods ----------------------------
	/**
	 * used by all activities
	 */
	public void sendMessage(IMessage msg) {
		if (networkService == null) {
			Log.e("==APP==", "networkService == NULL");
		} else {
			networkService.sendMessage(msg);
		}
	}

	/**
	 * used by welcome activity
	 */
	public void doConnect(String ip, int port, IConnectHandler handler) {
		this.serverIp = ip;
		this.serverPort = port;
		if (networkService == null) {
			this.handler = handler;
			initService();
		} else {
			Log.i("==APP==", "service already binded, reusing...");
			handler.handle();
		}
		// connnectService();
	}

	/**
	 * used by all login activity
	 */
	public void doStartHeartbeat(final String userName) {
		// 1. init service
		Intent intent = new Intent(this, HeartbeatService.class);
		scHeartbeat = new ServiceConnection() {

			public void onServiceDisconnected(ComponentName name) {
				Log.i("==APP==", "heatbeat service disconnected");
				heartbeatService = null;
			}

			public void onServiceConnected(ComponentName name, IBinder service) {
				Log.i("==APP==", "heatbeat service connected");
				heartbeatService = ((HeartbeatService.MyBinder) service)
						.getService();
				heartbeatService.startHeartbeat(IdiomGameApp.this, userName);
			}
		};
		bindService(intent, scHeartbeat, Context.BIND_AUTO_CREATE);
	}

	public void doDisconnect() {
		// TODO edit by juzm
		if (networkService != null) {
			IdiomGameApp.this.unregisterReceiver(receiver);

			// stop network service
			networkService.disconnect();
			unbindService(scNetwork);
			networkService = null;

			// stop heartbeat service
			if(heartbeatService!=null){//edited by juzm
				heartbeatService.stopHeartbeat();
				unbindService(scHeartbeat);
				heartbeatService = null;
			}
			receiver = null;
		}
	}

	// ------------- private methods ----------------------------
	private void onMessageReceived(String s) throws JSONException {
		if (s.trim().length() == 0) {
			Log.i("==APP==", "message length=0");
			return;
		}
		Log.i("==APP==", "onMessageReceived => " + s);
		IMessage msg = Json2BeansUtil.getMessageFromJsonStr(s);
		currentActivity.onMesssageReceived(msg);
	}

	private void initService() {
		// 1. init service
		Intent intent = new Intent(this, NetworkService.class);
		bindService(intent, scNetwork, Context.BIND_AUTO_CREATE);
		Log.i("==APP==", "service binded");

		// 2. reg receiver
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.test");
		IdiomGameApp.this.registerReceiver(receiver, filter);
		Log.i("==APP==", "receiver registered");

	}

	private ServiceConnection scNetwork = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			networkService = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("==APP==", "service connected");
			networkService = ((NetworkService.MyBinder) service).getService();

			networkService.connect(serverIp, serverPort);

			IdiomGameApp.this.handler.handle();
		}
	};

	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("==APP==", "message received");
			Bundle bundle = intent.getExtras();
			String strStatus = bundle.getString("status");
			if (strStatus.equals("ok")) {
				String strMsg = bundle.getString("msg");
				try {
					onMessageReceived(strMsg);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (strStatus.equals("error")) {
				String strCode = bundle.getString("code");
				if (strCode.equals("1")) {// connection error
					doDisconnect();
					((IExceptionHandler) currentActivity).exceptionCatch();
				} else if (strCode.equals("2")) {// send message error
					doDisconnect();
					((IExceptionHandler) currentActivity).exceptionCatch();
				}
			}
		}

		public MyReceiver() {
		}

	}

	// ------------- setters and getters ----------------------------

	public String getServerIp() {
		return serverIp;
	}

	public boolean isAboutThreadIsInterrupt() {
		return aboutThreadIsInterrupt;
	}

	public synchronized void setAboutThreadIsInterrupt(
			boolean aboutThreadIsInterrupt) {
		this.aboutThreadIsInterrupt = aboutThreadIsInterrupt;
	}

	public IMessageHandler getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(IMessageHandler currentActivity) {
		this.currentActivity = currentActivity;
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

	public String getLastRegisterName() {
		return lastRegisterName;
	}

	public void setLastRegisterName(String lastRegisterName) {
		this.lastRegisterName = lastRegisterName;
	}

	public int[] getFemaleHeaderIdArray() {
		return femaleHeaderIdArray;
	}

	public void setFemaleHeaderIdArray(int[] femaleHeaderIdArray) {
		this.femaleHeaderIdArray = femaleHeaderIdArray;
	}

	public int[] getManHeaderIdArray() {
		return manHeaderIdArray;
	}

	public void setManHeaderIdArray(int[] manHeaderIdArray) {
		this.manHeaderIdArray = manHeaderIdArray;
	}

	public String getVoiceIdiom() {
		return voiceIdiom;
	}

	public void setVoiceIdiom(String voiceIdiom) {
		this.voiceIdiom = voiceIdiom;
	}

}
