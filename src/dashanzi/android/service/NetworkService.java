package dashanzi.android.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import dashanzi.android.dto.IMessage;
import dashanzi.android.util.Beans2JsonUtil;

public class NetworkService extends Service {

	protected boolean isStop = false;
	private final IBinder binder = new MyBinder();
	private Socket socket;
	private BufferedReader is;
	private PrintWriter os;

	public boolean readFlag = true;

	// ------------- public methods ----------------------------
	public void connect(String ip, int port) {
		try {
			// 1. connect
			socket = new Socket();
			// socket.setSoTimeout(5000);
			socket.connect(new InetSocketAddress(ip, port), 10000);
			is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			os = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);

			Log.i("==NET==", "connected");

			// 2. start reader
			new Thread(readerThread).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("==NET==", "IOException caught when connecting: " + e);

			Intent intent = new Intent();
			intent.putExtra("status", "error");
			intent.putExtra("code", "1");
			intent.setAction("android.intent.action.test");
			sendBroadcast(intent);
		}

	}

	public void sendMessage(IMessage msg) {
		if (socket != null) {// edited by juzm TODO
			Log.i("==NET==", "socket.isConnected()=" + socket.isConnected());
			Log.i("==NET==",
					"socket.isOutputShutdown()=" + socket.isOutputShutdown());
		}

		if (socket != null && socket.isConnected()) {// edited by juzm socket !=
														// null TODO
			if (!socket.isOutputShutdown()) {
				try {
					Log.i("==NET==", "write 1");
					socket.getOutputStream().write(
							Beans2JsonUtil.getJsonStr(msg).getBytes("utf-8"));
					Log.i("==NET==", "write 2");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {

					Intent intent = new Intent();
					intent.putExtra("status", "error");
					intent.putExtra("code", "2");
					intent.setAction("android.intent.action.test");
					sendBroadcast(intent);

					e.printStackTrace();
				}
			}
		}
	}

	public void disconnect() {
		try {
			// edited by juzm TODO
			if (socket == null || socket.isClosed() == true || is == null || os == null) {
				return;
			}
			socket.close();
			is.close();
			os.close();
			readFlag = false;
			Log.i("==NET==",
					"socket closed: socket.isClosed=" + socket.isClosed()
							+ ", socket.isConnected=" + socket.isConnected());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------- private methods ----------------------------
	private void onMessageRecevied(String strMsg) {
		Log.i("==NET==", "message received: " + strMsg);

		Intent intent = new Intent();
		intent.putExtra("status", "ok");
		intent.putExtra("msg", strMsg);
		intent.setAction("android.intent.action.test");
		sendBroadcast(intent);
	}

	// ------------- reader thread ----------------------------
	Runnable readerThread = new Runnable() {

		@Override
		public void run() {
			Log.i("==NET==", "socket.isConnected()=" + socket.isConnected());
			Log.i("==NET==",
					"socket.isOutputShutdown()=" + socket.isOutputShutdown());
			String content;
			while (readFlag) {
				if (socket.isConnected()) {
					if (!socket.isInputShutdown()) {
						try {
							if (is != null && (!socket.isClosed())
									&& (content = is.readLine()) != null) {
								System.out.println("content => " + content);
								onMessageRecevied(content);
							} else {

							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}

	};

	// ------------- other methods ----------------------------

	public void onCreate() {
		Log.i("==NET==", "service created");
		super.onCreate();
	}
	
	public IBinder onBind(Intent intent) {
		// Log.i("NetworkService", "service onBind");
		return binder;
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	public int onStartCommand(Intent i, int a, int b) {
		return 0;
	}

	public void onDestroy() {
		Log.i("==NET==", "service destroyed");
		isStop = true;
		readFlag = false;
		super.onDestroy();
	}
	
	// ------------- inner classes ----------------------------
	public class MyBinder extends Binder {
		public NetworkService getService() {
			return NetworkService.this;
		}
	}
}
