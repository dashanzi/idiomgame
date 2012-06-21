package dashanzi.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.dto.IMessage;

public class Welcome extends Activity implements IMessageHandler {
	private final int SPLASH_DELAY_TIME = 5000;
	private String Tag = "Welcome";
	private IdiomGameApp app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(Tag, "onCreate()");
		super.onCreate(savedInstanceState);

		// 1. 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);

		// 2. 建立TCP连接 TODO
		app = (IdiomGameApp) this.getApplication();
		app.setServerIp("210.75.225.158");
		app.setServerPort(8888);

		app.connect(new IConnectHandler() {
			public void handle() {
				// 自动跳转至 Login界面
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						startActivity(new Intent(Welcome.this, Login.class));
						Welcome.this.finish();
					}

				}, SPLASH_DELAY_TIME);
			}
		});
	}

	@Override
	public void onMesssageReceived(IMessage msg) {
		// TODO Auto-generated method stub

	}

	public void handle() {}
}