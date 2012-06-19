package juzm.android.login;

import java.util.ArrayList;
import java.util.List;

import juzm.android.R;
import juzm.android.dto.request.LoginRequestMsg;
import juzm.android.game.Game;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Login extends Activity {

	private LinearLayout father_ll;// 父linearLayout
	private LinearLayout loading_ll;// loading_linearLayout

	private EditText userName = null;
	private EditText passWord = null;
	private CheckBox rememberPasswordCheck = null;
	private Button loginBtn = null;
	private LoginRequestMsg loginMsg = new LoginRequestMsg();

	private Animation anm;// loading动画
	private int loadingAnmLoation;// loading动画位置
	private List<ImageView> images;// loading图片列表

	private boolean hasLoginResult = false;// 是否返回了登陆结果

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// 获得login relativelayout, 设置透明度
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.login_input_area);
		rl.getBackground().setAlpha(190);

		// 获取登陆信息
		userName = (EditText) findViewById(R.id.login_edittext_username);
		passWord = (EditText) findViewById(R.id.login_edittext_password);
		rememberPasswordCheck = (CheckBox) findViewById(R.id.login_checkbox_remember_password);
		if (userName != null && userName.getText() != null) {
			loginMsg.setName(userName.getText().toString());
		}
		if (passWord != null && passWord.getText() != null) {
			loginMsg.setPassword(passWord.getText().toString());
		}
		if (rememberPasswordCheck.isChecked()) {
			loginMsg.setRememberPassword(true);
		} else {
			loginMsg.setRememberPassword(false);
		}

		// 设置登陆监听
		loginBtn = (Button) findViewById(R.id.login_button_login);
		loginBtn.setOnClickListener(new MyOnClickListener());

		// 预加载loading动画，此时不显示
		father_ll = (LinearLayout) findViewById(R.id.father_linear_layout);
		int height = father_ll.getHeight();
		loadingAnmLoation = height + 50;
		loading_ll = (LinearLayout) findViewById(R.id.loading_linear_layout);
		images = new ArrayList<ImageView>();
		anm = AnimationUtils.loadAnimation(this, R.anim.loadinganim);
	}

	class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.d("click", "--->> onClickListener !");

			// 显示加载动画
			initImage(loading_ll);
			playAnimation();

			// 发送登陆请求 TODO，接收登陆响应，成功则跳转至游戏界面，否则提示登陆错误
			testLoginResult();
		}
	}

	// test TODO
	private void testLoginResult() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				hasLoginResult = true;
				
				startActivity(new Intent(Login.this , Game.class));  
                Login.this.finish(); 
			}

		}.start();
	}

	private void playAnimation() {
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
							Log.d("loading", "loading...");
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

}