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
 * 软件介绍
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
	private String[] titles = { "寓教于乐", "软件首页", "游戏大厅", "游戏房间", "具体规则" };
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

		TextView gameRule = getTextView();
		String separator = "\r\n";
		StringBuffer sb = new StringBuffer();
		sb.append("成语匹配规则：").append(separator);
		sb.append("[1] 龙头成语由服务器端在词库中随机选取发放；").append(separator);
		sb.append("[2] 前后两个相接的成语，前一成语的尾字和后一成语的首字拼音必须相同（音调可以不同）").append(separator);
		sb.append("例如：“头脑风暴”下面可接：“抱薪救火”、“褒采一介”、“苞藏祸心”等").append(separator).append(separator);
		
		sb.append("积分输赢规则：").append(separator);
		sb.append("[1] 在规定时间内回答正确: 回答成语在词库中的词频越小，获得积分越多").append(separator);
		sb.append("	积分公式：(MAX_FREQ-freq)*5/ MAX_FREQ").append(separator);
		sb.append("[2] 在规定时间内回答错误: 积分-1").append(separator);
		sb.append("[3] 超时: 积分-1").append(separator);
		sb.append("[4] 使用锦囊 : 积分+3，但锦囊数-1").append(separator).append(separator);
		
		sb.append("等级规则：").append(separator);
		sb.append("[1] 积分  <= 0 : 等级为0"+ "; [2] 积分  1-10 : 等级为1").append(separator);
		sb.append("[3] 积分11-30 : 等级为2"+"; [4] 积分31-60 : 等级为3").append(separator);
		sb.append("[5] 积分61-100 : 等级为4" + "; [6] 积分  >100 : 等级为5").append(separator);
		
		gameRule.setText(sb.toString());
		flipper.addView(gameRule);

		// 初始化悬浮按钮
		initImageButtonView();
	}

	private ImageView getImageView(final int id) {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(id);
		return imageView;
	}

	private TextView getTextView() {
		TextView textView = new TextView(this);
		return textView;
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

			// 上一张
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