package dashanzi.android.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.dto.notify.LogoutNotifyMsg;

public class IndexSelect extends TabActivity {
	private static final String tag ="IndexSelect";
	private IdiomGameApp app;
	private TabHost mTabHost;
	private TabWidget mTabWidget;
	
	private String userName = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);
		app = (IdiomGameApp) this.getApplication();
		app.setAboutThreadIsInterrupt(true);// 终止about thread

		Intent intent = this.getIntent();
		if(intent != null){
			userName = intent.getStringExtra("name");
		}else{
			Log.e(tag, " user name is null");
		}
		
		mTabHost = this.getTabHost();
		/* 去除标签下方的白线 */
		mTabHost.setPadding(mTabHost.getPaddingLeft(),
				mTabHost.getPaddingTop(), mTabHost.getPaddingRight(),
				mTabHost.getPaddingBottom() - 5);
		Resources rs = getResources();

		Intent layout1intent = new Intent();

		layout1intent.setClass(this, Login.class);
		TabHost.TabSpec layout1spec = mTabHost.newTabSpec("登陆");
		layout1spec.setIndicator("登陆",
				rs.getDrawable(android.R.drawable.arrow_down_float));
		layout1spec.setContent(layout1intent);
		mTabHost.addTab(layout1spec);

		Intent layout2intent = new Intent();
		layout2intent.setClass(this, Register.class);
		TabHost.TabSpec layout2spec = mTabHost.newTabSpec("注册");
		layout2spec.setIndicator("注册",
				rs.getDrawable(android.R.drawable.arrow_down_float));
		layout2spec.setContent(layout2intent);
		mTabHost.addTab(layout2spec);

		Intent layout3intent = new Intent();
		layout3intent.setClass(this, About.class);
		TabHost.TabSpec layout3spec = mTabHost.newTabSpec("关于");
		layout3spec.setIndicator("关于",
				rs.getDrawable(android.R.drawable.arrow_down_float));
		layout3spec.setContent(layout3intent);
		mTabHost.addTab(layout3spec);

		/* 对Tab标签的定制 */
		mTabWidget = mTabHost.getTabWidget();
		for (int i = 0; i < mTabWidget.getChildCount(); i++) {
			/* 得到每个标签的视图 */
			View view = mTabWidget.getChildAt(i);
			/* 设置每个标签的背景 */
			if (mTabHost.getCurrentTab() == i) {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.number_bg_pressed));
			} else {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.number_bg));
			}
			/* 设置tab的高度 */
			mTabWidget.getChildAt(i).getLayoutParams().height = 50;
			TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			/* 设置tab内字体的颜色 */
			tv.setTextColor(Color.WHITE);
		}

		/* 当点击Tab选项卡的时候，更改当前Tab标签的背景 */
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				for (int i = 0; i < mTabWidget.getChildCount(); i++) {
					View view = mTabWidget.getChildAt(i);
					if (mTabHost.getCurrentTab() == i) {
						view.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.number_bg_pressed));
					} else {
						view.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.number_bg));
					}
				}
			}
		});
	}


	@Override
	protected void onResume() {
		Log.e("IndexSelect", "onResume");
		super.onResume();
		// stop about thread
		app.setAboutThreadIsInterrupt(true);
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("您确定退出游戏吗？")
					.setTitle("友情提示")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.cancel();
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									
									// 1. 发送退出游戏通知
									LogoutNotifyMsg logout = new LogoutNotifyMsg();
									logout.setType(Constants.Type.LOGOUT_NOTIFY);
									logout.setName(userName);
									app.sendMessage(logout);
									Log.i(tag, "--->>> send  LogoutNotifyMsg = " + logout.toString());
									
									
									//断开连接
									app.disconnect();
									IndexSelect.this.finish();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
	                return false;
		}
		return super.dispatchKeyEvent(event);
	};
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//用于解决activity中有ViewFlipper不响应
		getCurrentActivity().onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
}
