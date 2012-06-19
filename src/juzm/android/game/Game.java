package juzm.android.game;

import java.util.Arrays;
import java.util.List;

import juzm.android.GameApp;
import juzm.android.Help;
import juzm.android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class Game extends Activity {

	private static final int READY = 0;
	private static final int SUBMIT = 1;
	private static final int HELP = 2;

	private GridView configGrid;
	private String configData[] = { "准备", "提交", "锦囊" };
	private RelativeLayout rl;
	private TextView clock;
	private boolean clockStop = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		
		//TODO
		GameApp app = (GameApp) this.getApplication();
//		app.mSocketClient
		
		
		// 配置页gridview的设置
		configGrid = (GridView) findViewById(R.id.game_gridview);
		configGrid.setNumColumns(configData.length);
		GridAdapter gridAdapter = new GridAdapter(this,
				Arrays.asList(configData));
		configGrid.setAdapter(gridAdapter);
		configGrid.setOnItemClickListener(new MyOnItemClickListener());

		// test TODO
		rl = (RelativeLayout) findViewById(R.id.game_player_one_layout);
		rl.setBackgroundColor(Color.RED);
		rl.setBackgroundResource(R.drawable.player_area_background);
		clock = (TextView) findViewById(R.id.clock_value);
		countDown();
	}

	private void countDown(){
		new Thread(){
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for(int i=10;i>=0;i--){
					if(!clockStop){
						clockHandler.sendEmptyMessage(i);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}
	
	private Handler clockHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			clock.setText(msg.what+"");
			Log.d("test", msg.what+"");
		}
	};
	
	// GridView监听
	class MyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch (arg2) {
			case READY:
				doReadyAction();
				break;
			case SUBMIT:
				doSubmitAction();
				break;
			case HELP:
				doHelpAction();
				break;
			default:
				// do nothing
				break;

			}
		}
	}

	//准备操作
	private void doReadyAction() {
		// TODO Auto-generated method stub
		//在此与服务端建立连接

	}

	//提交成语操作
	public void doSubmitAction() {
		// TODO Auto-generated method stub
		//1. 停表
		this.clockStop = true;
		
	}

	//锦囊操作
	public void doHelpAction() {
		// TODO Auto-generated method stub
		
	}

	
	// 菜单操作
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.about);
		menu.add(0, 2, 2, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item != null) {
			if (item.getItemId() == 2) {
				// 退出程序
				Game.this.finish();
			} else if (item.getItemId() == 1) {
				Intent intent = new Intent();
				intent.setClass(Game.this, Help.class);
				Game.this.startActivity(intent);
			}
		} else {
			System.out.println("item is null ");
		}

		return super.onMenuItemSelected(featureId, item);
	}

	// 监听back键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);

			builder.setIcon(R.drawable.tanhao);
			builder.setTitle("确定退出吗?");

			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {

							Game.this.finish();
						}
					});

			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});
			builder.create().show();
		}
		return false;
	}

	//自定义gridAdapter
	public class GridAdapter extends BaseAdapter {
		Context context;
		List list;

		public GridAdapter(Context c, List datalist) {
			this.context = c;
			list = datalist;
		}

		public GridAdapter(OnTabChangeListener onTabChangeListener,
				List<String> asList) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			TextView tv;
			if (convertView == null) {
				tv = new TextView(context);
			} else {
				tv = (TextView) convertView;
			}
			tv.setLayoutParams(new GridView.LayoutParams(110, 48));
			tv.setTextColor(Color.WHITE);
			if (position == 0) {
				tv.setBackgroundResource(R.drawable.game_ready_background);
			} else if (position == 1) {
				tv.setBackgroundResource(R.drawable.game_submit_background);
			} else if (position == 2) {
				tv.setBackgroundResource(R.drawable.game_jinnang_background);
			}
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(20);
			tv.setText(list.get(position).toString());

			return tv;
		}
	}
}
