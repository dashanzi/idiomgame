package dashanzi.android;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Application;
import android.util.Log;

public class GameApp extends Application {
	
	public Socket mSocketClient = null;
	
	
	private final static String SERVER_IP = "127.0.0.1";
	private final static int PORT = 12345;
	
	
	@Override
	public void onCreate() {
		
		try {
			//
			Log.d("app", ".........................");
			mSocketClient = new Socket(SERVER_IP,PORT);
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onCreate();
	}

}
