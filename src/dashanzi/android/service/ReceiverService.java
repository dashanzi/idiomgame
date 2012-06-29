package dashanzi.android.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ReceiverService extends Service {
	private final IBinder binder = new MyBinder();
	private boolean readFlag = true;
	private Socket socket;

	private BufferedReader is;

	// ------------- private methods ----------------------------
	private void onMessageRecevied(String strMsg) {
		Log.i("==NET==", "message received: " + strMsg);

		Intent intent = new Intent();
		intent.putExtra("status", "ok");
		intent.putExtra("msg", strMsg);
		intent.setAction("android.intent.action.test");
		sendBroadcast(intent);
	}

	// ------------- public methods ----------------------------
	public void startReceiving(Socket s) {
		this.socket = s;
		Log.i("==NET==", "socket.isConnected()=" + s.isConnected());
		Log.i("==NET==", "socket.isOutputShutdown()=" + s.isOutputShutdown());
		String content;

		try {
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (readFlag) {
			if (s.isConnected()) {
				if (!s.isInputShutdown()) {
					try {
						if (is != null && (!s.isClosed())
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
		Log.i("==NET==", "reader ended");
	}

	public void stopReceiving() {
		try {
			readFlag=false;
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------- other methods ----------------------------

	public IBinder onBind(Intent intent) {
		// Log.i("ReceiverService", "service onBind");
		return binder;
	}

	// ------------- setters and getters ----------------------------
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getIs() {
		return is;
	}

	public void setIs(BufferedReader is) {
		this.is = is;
	}

	// ------------- inner classes ----------------------------
	public class MyBinder extends Binder {
		public ReceiverService getService() {
			return ReceiverService.this;
		}
	}

}
