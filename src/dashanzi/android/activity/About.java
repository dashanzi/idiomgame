package dashanzi.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import dashanzi.android.R;

public class About extends Activity  implements OnGestureListener {

	private ViewFlipper flipper;

	private GestureDetector detector;
	private TextView title_tv;
	private String[] titles ={"游戏宗旨","注册与登陆","选择房间","接龙游戏","具体规则"};
	private int tiltle_index = 0;
	private final int pageNum = 5;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		title_tv = (TextView) findViewById(R.id.about_title_tv);
		title_tv.setText(titles[0]);
		
		
		detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);

		flipper.addView(getImageView(R.drawable.page1));
		flipper.addView(getImageView(R.drawable.page2));
		flipper.addView(getImageView(R.drawable.page3));
		flipper.addView(getImageView(R.drawable.page4));
		flipper.addView(getImageView(R.drawable.image5));
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			if(tiltle_index == pageNum-1){
				tiltle_index=0;
			}else{
				tiltle_index++;
			}
			title_tv.setText(titles[tiltle_index]);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			this.flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			if(tiltle_index == 0){
				tiltle_index=pageNum-1;
			}else{
				tiltle_index--;
			}
			title_tv.setText(titles[tiltle_index]);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			this.flipper.showPrevious();
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}