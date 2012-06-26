package dashanzi.android.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import dashanzi.android.Constants;
import dashanzi.android.R;
import dashanzi.android.db.DBUtil;
import dashanzi.android.db.DatabaseHelper;
import dashanzi.android.dto.IMessage;

public class Welcome extends Activity implements IMessageHandler {
	private final int SPLASH_DELAY_TIME = 3000;
	private String tag = "Welcome";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "......... Welcome onCreate()");
		super.onCreate(savedInstanceState);

		// 1. 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);

		// 自动跳转至login界面
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.i(tag, "---->>> forward to Login activity...");
				startActivity(new Intent(Welcome.this, IndexSelect.class));
				Welcome.this.finish();
			}

		}, SPLASH_DELAY_TIME);

		// 初始化server ip数据
		DBUtil.initDB(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 在首页监听back键， 点击返回键时完全退出程序
			Welcome.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onMesssageReceived(IMessage msg) {
		// TODO Auto-generated method stub

	}
}