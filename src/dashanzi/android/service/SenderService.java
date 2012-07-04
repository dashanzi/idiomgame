package dashanzi.android.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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

/**
 * 消息发送
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class SenderService extends Service {

	protected boolean isStop = false;
	private final IBinder binder = new MyBinder();
	private Socket socket;
	private BufferedReader is;
	private PrintWriter os;

	// public boolean readFlag = true;

	// ------------- public methods ----------------------------
	public Socket connect(String ip, int port) {
		try {
			// 1. connect
			socket = new Socket();
			// socket.setSoTimeout(5000);
			socket.connect(new InetSocketAddress(ip, port), 10000);

			os = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);

			Log.i("==NET==", "connected, socket=" + socket);

		} catch (UnknownHostException e) {
			return null;
		} catch (IOException e) {
			Log.i("==NET==", "IOException caught when connecting: " + e);

			if (os != null) {
				os.close();
			}

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			// Intent intent = new Intent();
			// intent.putExtra("status", "error");
			// intent.putExtra("code", "1");
			// intent.setAction("android.intent.action.test");
			// sendBroadcast(intent);

			return null;
		}

		return socket;

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
			if (socket == null || socket.isClosed() == true || is == null
					|| os == null) {
				return;
			}
			socket.close();
			is.close();
			os.close();
			Log.i("==NET==",
					"socket closed: socket.isClosed=" + socket.isClosed()
							+ ", socket.isConnected=" + socket.isConnected());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------- reader thread ----------------------------
	// Runnable readerThread = new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// }
	//
	// };

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
		// readFlag = false;
		super.onDestroy();
	}

	// ------------- inner classes ----------------------------
	public class MyBinder extends Binder {
		public SenderService getService() {
			return SenderService.this;
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
