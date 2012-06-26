package dashanzi.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;

public class About extends Activity {
	private final static String tag = "About";

	private IdiomGameApp app;
	
	private ViewFlipper viewFlipper;
	private Animation translateIn;
	private Animation translateOut;
	private Animation alphaIn;
	private Animation alphaOut;
	private RelativeLayout layout;
	private TextView title_tv;
	private final int pageNum = 5;
	private String[] titles ={"第一步：注册","第二步：登陆","第三步：选择房间","第四步：开始游戏","游戏规则"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		app = (IdiomGameApp) this.getApplication();
		app.setAboutThreadIsInterrupt(false);//开启放映thread
		
		viewFlipper = (ViewFlipper) getLayoutInflater().inflate(R.layout.aboutview,
				null);
		title_tv = (TextView) findViewById(R.id.about_title_tv);
		
		// ViewFlipper添加视图
		viewFlipper.addView(getImageView(R.drawable.login_page));
		viewFlipper.addView(getImageView(R.drawable.login_page));
		viewFlipper.addView(getImageView(R.drawable.image3));
		viewFlipper.addView(getImageView(R.drawable.image4));
		viewFlipper.addView(getImageView(R.drawable.image5));
		getImageView(R.drawable.login_page).getContext();
		// 将ViewFlipper添加到指定的视图中
		layout = (RelativeLayout) findViewById(R.id.viewId);
		layout.addView(viewFlipper);

		// 自右向做滑动效果
		translateIn = AnimationUtils.loadAnimation(this, R.anim.translate_in);
		translateOut = AnimationUtils.loadAnimation(this, R.anim.translate_out);
		// 浮现替换滑动效果
		alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
		alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
		AboutShowThread myThread = new AboutShowThread();
		myThread.start();
	}

	class AboutShowThread extends Thread {
		@Override
		public void run() {// 重写的run方法
			int i=0;
			while(!app.isAboutThreadIsInterrupt() && i<pageNum) {// 循环
				Log.d(tag,
						"AboutShowThread sendEmptyMessage is ----------------->>>"
								+ i);
				myHandler.sendEmptyMessage(i);// 发送消息
				try {
					Thread.sleep(5000);// 睡眠
				} catch (Exception e) {
					e.printStackTrace();// 打印异常信息
				}
				i++;
			}
		}
	}

	Handler myHandler = new Handler() {// 创建一个Handler对象
		@Override
		public void handleMessage(Message msg) {// 重写接收消息的方法
			Log.d(tag, "AboutShowThread getMessage is ----------------->>>"
					+ msg.what);
			title_tv.setText(titles[msg.what]);
			int flag = msg.what%2;
			switch (flag) {
			
			case 0:
				// 调用自右向做滑动效果
				viewFlipper.setInAnimation(translateIn);
				viewFlipper.setOutAnimation(translateOut);
				viewFlipper.showNext();
				break;
			case 1:// what值为1时
				viewFlipper.setInAnimation(alphaIn);
				viewFlipper.setOutAnimation(alphaOut);
				viewFlipper.showNext();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 获取背景图片方法
	 * 
	 * @param id
	 * @return
	 */
	private ImageView getImageView(final int id) {
		final String mString = getResources().getResourceName(id);
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(id);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 显示字符资源文件的文件名
				Toast.makeText(About.this, "该图片名称为：" + mString.split("/")[1], 5)
						.show();
			}
		});
		return imageView;
	}
	
	@Override
	protected void onResume() {
		Log.i("About", "onResume");
		super.onResume();
		//再次进入该界面时，重复播放
		app.setAboutThreadIsInterrupt(false);//开启放映thread
		AboutShowThread myThread = new AboutShowThread();
		myThread.start();
	}
}