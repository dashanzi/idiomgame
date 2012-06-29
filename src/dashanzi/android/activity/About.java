package dashanzi.android.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import dashanzi.android.R;

/**
 * 
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class About extends Activity implements OnGestureListener {

	private static final String tag = "About";

	private ViewFlipper flipper;

	private GestureDetector detector;
	private TextView title_tv;
	private String[] titles = { "游戏宗旨", "注册与登陆", "选择房间", "接龙游戏", "具体规则" };
	private int tiltle_index = 0;
	private final int pageNum = 5;

	// 声明两个按钮，分别代表向左和向右滑动
	private ImageView btnLeft = null;
	private ImageView btnRight = null;

	// 设置WindowManager
	private WindowManager wm = null;
	private WindowManager.LayoutParams wmParams = null;

	private boolean isFirstTime = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		isFirstTime = true;
		title_tv = (TextView) findViewById(R.id.about_title_tv);
		title_tv.setText(titles[0]);

		detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);

		flipper.addView(getImageView(R.drawable.page1));
		flipper.addView(getImageView(R.drawable.page2));
		flipper.addView(getImageView(R.drawable.page3));
		flipper.addView(getImageView(R.drawable.page4));
		flipper.addView(getImageView(R.drawable.image5));

		//初始化悬浮按钮
		initImageButtonView();
	}

	private ImageView getImageView(final int id) {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(id);
		return imageView;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			
			// 下一张
			showNextPage();
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			
			//上一张
			showPreviousPage();
			return true;
		}
		return false;
	}

	/**
	 * 初始化悬浮按钮
	 */
	private void initImageButtonView() {
		// 获取WindowManager
		wm = (WindowManager) getApplicationContext().getSystemService("window");

		// 设置LayoutParams相关参数
		wmParams = new WindowManager.LayoutParams();

		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;

		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;

		// 设置Window flag参数
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;

		// 设置x、y初始值
		wmParams.x = 0;
		wmParams.y = 0;

		// 设置窗口长宽数据
		wmParams.width = 50;
		wmParams.height = 50;

		// 创建左右按钮
		createLeftButtonView();
		createRightButtonView();
	}

	/**
	 * 设置左边按钮
	 */
	private void createLeftButtonView() {
		btnLeft = new ImageView(this);
		btnLeft.setImageResource(R.drawable.about_left_arrow);
		btnLeft.setAlpha(100);

		btnLeft.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// 上一张
				showPreviousPage();
			}
		});

		// 调整窗口
		wmParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

		// 显示图像
		wm.addView(btnLeft, wmParams);
	}

	/**
	 * 设置右边按钮
	 */
	private void createRightButtonView() {
		btnRight = new ImageView(this);
		btnRight.setImageResource(R.drawable.about_right_arrow);
		btnRight.setAlpha(100);

		btnRight.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// 下一张
				showNextPage();
			}
		});
		// 调整窗口
		wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

		// 显示图像
		wm.addView(btnRight, wmParams);
	}

	private void showPreviousPage() {
		if (tiltle_index == 0) {
			tiltle_index = pageNum - 1;
		} else {
			tiltle_index--;
		}
		title_tv.setText(titles[tiltle_index]);
		this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));
		this.flipper.showPrevious();
	}

	
	private void showNextPage() {
		if (tiltle_index == pageNum - 1) {
			tiltle_index = 0;
		} else {
			tiltle_index++;
		}
		title_tv.setText(titles[tiltle_index]);
		this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_out));
		this.flipper.showNext();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.e(tag, "onResume");

		if (isFirstTime) {
			isFirstTime = false;
		} else {
			// 显示图像
			initImageButtonView();
		}
	}

	@Override
	protected void onPause() {
		Log.e(tag, "-->> onPause");
		super.onPause();

		wm.removeView(btnLeft);
		wm.removeView(btnRight);
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}