package dashanzi.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.db.DBUtil;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.ServerInfo;
import dashanzi.android.dto.request.LoginRequestMsg;
import dashanzi.android.dto.response.LoginResponseMsg;
import dashanzi.android.listener.MyConfigOnClickListener;
import dashanzi.android.util.ToastUtil;

public class Login extends Activity implements IMessageHandler {

	private static final String tag = "Login";
	private final int configBtnTag = 1;
	private final int loginBtnTag = 2;
	private IdiomGameApp app;
	// 组件
	private LinearLayout father_ll;// 父linearLayout
	private LinearLayout loading_ll;// loading_linearLayout，用于显示loading动画
	private EditText userName = null;
	private EditText passWord = null;
	private Button loginBtn = null;
	private ImageButton configBtn = null;

	// loading动画
	private Animation anm;// loading动画
	private int loadingAnmLoation;// loading动画位置
	private List<ImageView> images;// loading图片列表

	private LoginRequestMsg loginMsg = new LoginRequestMsg();
	private boolean hasLoginResult = false;// 是否返回了登陆结果

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "......... Login onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		app = (IdiomGameApp) this.getApplication();
		app.setCurrentActivity(this);
		app.setAboutThreadIsInterrupt(true);// 终止about thread

		// 获得组件，login relativelayout, 设置透明度
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.login_input_area);
		rl.getBackground().setAlpha(190);
		userName = (EditText) findViewById(R.id.login_edittext_username);
		passWord = (EditText) findViewById(R.id.login_edittext_password);

		// 服务器ip配置监听
		configBtn = (ImageButton) findViewById(R.id.login_server_ip_config);
		configBtn.setOnClickListener(new MyConfigOnClickListener(this));
		configBtn.setTag(configBtnTag);

		// 设置登陆监听
		loginBtn = (Button) findViewById(R.id.login_button_login);
		loginBtn.setOnClickListener(new MyOnClickListener());
		loginBtn.setTag(loginBtnTag);

		// 预加载loading动画，此时不显示
		father_ll = (LinearLayout) findViewById(R.id.father_linear_layout);
		int height = father_ll.getHeight();
		loadingAnmLoation = height + 50;
		loading_ll = (LinearLayout) findViewById(R.id.loading_linear_layout);
		images = new ArrayList<ImageView>();
		anm = AnimationUtils.loadAnimation(this, R.anim.loadinganim);
	}

	/**********************************************************************************************
	 * Logic Action
	 **********************************************************************************************/

	@Override
	public void onMesssageReceived(IMessage msg) {
		// 得到服务端消息
		if (!(msg instanceof LoginResponseMsg)) {
			Log.e(tag, "LoginResponseMsg format error ! msg=" + msg);
			Log.e(tag, "class =" + msg.getClass().getCanonicalName());
			return;
		}

		LoginResponseMsg loginRes = (LoginResponseMsg) msg;
		Log.i(tag, "<<<<--- get LoginResponseMsg = " + msg.toString());
		// 终止登陆thread
		hasLoginResult = true;

		if (loginRes.getStatus().equals(Constants.Response.SUCCESS)) {
			// 将username uid传给House界面
			Intent intent = new Intent();
			intent.putExtra("name", userName.getText().toString());
			intent.setClass(Login.this, House.class);
			Login.this.startActivity(intent);

			// 终止Login
			Login.this.finish();
		} else if (loginRes.getStatus().equals(Constants.Response.FAILED)) {
			// 提示登陆失败
			ToastUtil.toast(this, "登陆失败,请重新登陆!",
					android.R.drawable.ic_dialog_alert);
		}
	}

	/**********************************************************************************************
	 * 按钮监听
	 **********************************************************************************************/

	// 登陆按钮监听
	class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.i(tag, "--->> login btn onClickListener !");
			// 封装loginMsg
			loginMsg.setType(Constants.Type.LOGIN_REQ);
			if (userName != null && userName.getText() != null) {
				loginMsg.setName(userName.getText().toString());
			}
			if (passWord != null && passWord.getText() != null) {
				loginMsg.setPassword(passWord.getText().toString());
			}

			// 建立连接
			Log.i(tag, "--->>>> connecting to server");
			// app.setServerIp("210.75.225.158");
			// app.setServerPort(8888);
			
			ServerInfo dto = DBUtil.getServerInfo(Login.this);
			if(dto == null || dto.getIp()==null || dto.getPort()==0){
				Log.e(tag, " DBUtil.getServerInfo error !");
				return;
			}
			app.setServerIp(dto.getIp());
			app.setServerPort(dto.getPort());
			
			Log.e(tag, " Login  IP = " + dto.getIp() + ": PORT = " + dto.getPort());
			app.connect(new IConnectHandler() {
				public void handle() {

					// 连接成功后，向服务端发送登陆请求
					Log.i(tag, "---->>> connect success !! send LogMsg = "
							+ loginMsg.toString());
					app.sendMessage(loginMsg);
				}
			});

			// 显示加载动画
			initImage(loading_ll);
			playAnimationThread();
		}
	}

	/**********************************************************************************************
	 * loading动画
	 **********************************************************************************************/
	private void playAnimationThread() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int runcount = 0;
				while (!hasLoginResult) {
					if (runcount < 2) {
						for (int i = 0; i <= 6; i++) {
							handler.sendEmptyMessage(i);
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						runcount++;
					} else {
						handler.sendEmptyMessage(99);
						runcount = 0;
					}
				}
			}

		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				images.get(0).setImageDrawable(
						Login.this.getResources().getDrawable(R.drawable.l));
				images.get(0).startAnimation(anm);

				break;
			case 1:
				images.get(1).setImageDrawable(
						Login.this.getResources().getDrawable(R.drawable.o));
				images.get(1).startAnimation(anm);
				break;
			case 2:
				images.get(2).setImageDrawable(
						Login.this.getResources().getDrawable(R.drawable.a));
				images.get(2).startAnimation(anm);
				break;
			case 3:
				images.get(3).setImageDrawable(
						Login.this.getResources().getDrawable(R.drawable.d));
				images.get(3).startAnimation(anm);
				break;
			case 4:
				images.get(4).setImageDrawable(
						Login.this.getResources().getDrawable(R.drawable.i));
				images.get(4).startAnimation(anm);
				break;
			case 5:
				images.get(5).setImageDrawable(
						Login.this.getResources().getDrawable(R.drawable.n));
				images.get(5).setAnimation(anm);
				break;
			case 6:
				images.get(6).setImageDrawable(
						Login.this.getResources().getDrawable(R.drawable.g));
				images.get(6).setAnimation(anm);
				break;
			case 99:
				clearImage();
				break;
			}
		}
	};

	private void clearImage() {
		for (ImageView image : images) {
			image.setImageDrawable(null);
			image.destroyDrawingCache();
		}
	}

	private void initImage(LinearLayout layout) {

		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(40, 40);
		param.setMargins(0, loadingAnmLoation, 0, 0);

		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(40, 40);
		param2.setMargins(-5, loadingAnmLoation, 0, 0);

		ImageView l = new ImageView(this);
		l.setLayoutParams(param);
		layout.addView(l);
		images.add(l);

		ImageView o = new ImageView(this);
		o.setLayoutParams(param2);
		layout.addView(o);
		images.add(o);

		ImageView a = new ImageView(this);
		a.setLayoutParams(param2);
		layout.addView(a);
		images.add(a);

		ImageView d = new ImageView(this);
		d.setLayoutParams(param2);
		layout.addView(d);
		images.add(d);

		ImageView i = new ImageView(this);
		i.setLayoutParams(param2);
		layout.addView(i);
		images.add(i);

		ImageView n = new ImageView(this);
		n.setLayoutParams(param2);
		layout.addView(n);
		images.add(n);

		ImageView g = new ImageView(this);
		g.setLayoutParams(param2);
		layout.addView(g);
		images.add(g);
	}

	@Override
	protected void onResume() {
		Log.i("login", "onResume");
		super.onResume();
		app.setCurrentActivity(this);
		// stop about thread
		app.setAboutThreadIsInterrupt(true);
	}

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    //关闭dbHelper
	    DBUtil.closeDBHelper();
	}

	/**********************************************************************************************
	 * getter and setter
	 **********************************************************************************************/
}