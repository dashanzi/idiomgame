package dashanzi.android.heartbeat;

import android.util.Log;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.dto.request.RefreshRequestMsg;

public class HeartBeat {
	private static final String tag = "heartbeat";
	
	
	public static void startHeartBeat(final IdiomGameApp app){
		
		new Thread(){
			public void run() {
				RefreshRequestMsg req = new RefreshRequestMsg();
				req.setType(Constants.Type.REFRESH_REQ);
				
				while(true){
					
					if(app!=null){
						app.sendMessage(req);
						Log.i(tag, "---->>> send heartbeat msg !");
					}
					
					try {
						Thread.sleep(Constants.HeatBeat.HEARTBEAT_INTERVAl);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

}
