package dashanzi.android.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NetworkService extends Service {

	protected boolean isStop = false;
	private final IBinder binder = new MyBinder();
	private Socket socket;
	private BufferedReader is;
	private PrintWriter os;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("ttt", "service onBind");
		return binder;
	}

	public class MyBinder extends Binder {
		public NetworkService getService() {
			return NetworkService.this;
		}
	}

	// ------------- public methods ----------------------------
	public void connect(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			os = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			if (socket.isConnected()) {
				if (!socket.isOutputShutdown()) {
					os.println("are you sb?");
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ------------- private methods ----------------------------
	public void onMessageRecevied(String strMsg) {

	}

	/**
	 * 
	 */
	public void test() {
		Log.i("ttt", "service execute");

		new Thread() {// �½��̣߳�ÿ��1�뷢��һ�ι㲥��ͬʱ��i�Ž�intent����

			public void run() {
				int i = 0;
				while (!isStop) {
					Intent intent = new Intent();
					intent.putExtra("i", i);
					i++;
					intent.setAction("android.intent.action.test");// action���������ͬ
					sendBroadcast(intent);
					Log.i("ttt", "[in ex] Broadcasting... " + String.valueOf(i));
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	public void onCreate() {
		Log.i("ttt", "service onCreate");
		super.onCreate();
	}

	public void onStart(Intent intent, int startId) {
		Log.i("ttt", "Services onStart");
		super.onStart(intent, startId);
		new Thread() {// �½��̣߳�ÿ��1�뷢��һ�ι㲥��ͬʱ��i�Ž�intent����

			public void run() {
				int i = 0;
				while (!isStop) {
					Intent intent = new Intent();
					intent.putExtra("i", i);
					i++;
					intent.setAction("android.intent.action.test");// action���������ͬ
					sendBroadcast(intent);
					Log.i("ttt", "Broadcasting... " + String.valueOf(i));
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	public int onStartCommand(Intent i, int a, int b) {
		Log.i("ttt", "Services onStartCommand");

		new Thread() {// �½��̣߳�ÿ��1�뷢��һ�ι㲥��ͬʱ��i�Ž�intent����

			public void run() {
				int i = 0;
				while (!isStop) {
					Intent intent = new Intent();
					intent.putExtra("i", i);
					i++;
					intent.setAction("android.intent.action.test");// action���������ͬ
					sendBroadcast(intent);
					Log.i("ttt", "Broadcasting... " + String.valueOf(i));
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

		return 0;
	}

	@Override
	public void onDestroy() {
		Log.i("TAG", "Services onDestory");
		isStop = true;// ��ʹservice����߳�Ҳ����ֹͣ����������ͨ������isStop��ֹͣ�߳�
		super.onDestroy();
	}
}
