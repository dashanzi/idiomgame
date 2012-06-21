package dashanzi.android.activity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import dashanzi.android.R;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.User;
import dashanzi.android.dto.notify.StartNotifyMsg;
import dashanzi.android.dto.response.InputResponseMsg;
import dashanzi.android.dto.response.TimeoutResponseMsg;

public class Game extends Activity implements IMessageHandler{

	//gridView
	private static final int EXIT = 0;
	private static final int SUBMIT = 1;
	private static final int HELP = 2;
	//成语结果检查
	private static final int IDIOM_WRONG = 0;
	private static final int IDIOM_CORRECT = 1;
	//倒计时秒数
	private static final int COUNT_DOWN_SECOND = 10;

	// 组件
	private RelativeLayout current_player_layout;// 当前活跃玩家layout，背景颜色区别于观看玩家
	private TextView idiom_show_tv;// 显示成语词条的tv
	private ImageView idiom_check_iv;// 成语词条正误标识图片
	private TextView clock_show_tv;// 显示倒计时内容的tv
	private EditText idiom_write_et;// 录入成语词条的et
	private GridView configGrid;// 按钮gridview
	private String configData[] = { "退出", "提交", "锦囊" };

	private boolean clockStop = false;

	// game状态变量
	private final int pNum = 3;
	private User[] users = new User[3];
	private Map<String, User> userMap = new HashMap<String, User>();
	private String currentUid = null;
	private String myUid = null;

	// game消息变量
	private StartNotifyMsg startNotify;
	private InputResponseMsg inputResponse;
	private TimeoutResponseMsg timeOutResponse;

	// 玩家信息
	private TextView player1Name = null;
	private TextView player2Name = null;
	private TextView player3Name = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		// 1. 获取所有要使用的组件
		idiom_show_tv = (TextView) findViewById(R.id.game_idiom_show_text);
		idiom_check_iv = (ImageView) findViewById(R.id.game_idiom_check_image);
		clock_show_tv = (TextView) findViewById(R.id.clock_value);
		idiom_write_et = (EditText) findViewById(R.id.game_idiom_edit_text);
		// 配置页gridview的设置
		configGrid = (GridView) findViewById(R.id.game_gridview);
		configGrid.setNumColumns(configData.length);
		GridAdapter gridAdapter = new GridAdapter(this,
				Arrays.asList(configData));
		configGrid.setAdapter(gridAdapter);
		configGrid.setOnItemClickListener(new MyOnItemClickListener());

		
		// 2. 进入房间时候，设置Activity title
		Intent intent = this.getIntent();
		String gid = intent.getStringExtra("gid");
		this.setTitle("成语接龙" + "-" + gid + "号房间");

		
/*		// 获取自身的登录名、uid
		String loginName = intent.getStringExtra("name");
		myUid = intent.getStringExtra("uid");

		// 永远将自己放置在玩家1的位置
		player1Name = (TextView) findViewById(R.id.game_player_one_name);
		player1Name.setText(loginName);

		// test TODO
		current_player_layout = (RelativeLayout) findViewById(R.id.game_player_one_layout);
		current_player_layout.setBackgroundColor(Color.RED);
		current_player_layout
				.setBackgroundResource(R.drawable.player_area_background);*/

		countDownThread();

	}

	
	/************************************************************************************************************************
	 * Logic Action
	 ************************************************************************************************************************/
	//进入大厅
	
	//超時操作
	private void doTimeOutAction() {
		// TODO Auto-generated method stub

	}
	
	
	/************************************************************************************************************************
	 * GridView Action
	 ************************************************************************************************************************/
	// 准备操作
	private void doExitAction() {
		this.exit();
	}

	// 提交成语操作
	private void doSubmitAction() {
		// TODO Auto-generated method stub
		// 1. 停表
		this.clockStop = true;
		// 2. 发送提交请求

		String idiom = idiom_write_et.getText().toString();
		showIdiomThread(idiom);

		// TODO
		idiomCheckThread(true);
	}

	// 锦囊操作
	private void doHelpAction() {
		// TODO Auto-generated method stub
		// 1. 发送锦囊请求
	}
	
	
	/************************************************************************************************************************
	 * Handler
	 ************************************************************************************************************************/
	//showIdiomHandler
	private void showIdiomThread(final String idiom) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				if (idiom != null) {
					for (int i = 0; i <= idiom.length(); i++) {
						showIdiomHandler.sendEmptyMessage(i);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}

	private Handler showIdiomHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i("test", msg.what + "");
			String idiomStr = idiom_write_et.getText().toString()
					.substring(0, msg.what);
			idiom_show_tv.setText(idiomStr);
		}
	};
	
	//idiomCheckHandler
	private void idiomCheckThread(final boolean isCorrect) {
		new Thread() {
			public void run() {
				if (isCorrect) {
					idiomCheckHandler.sendEmptyMessage(IDIOM_CORRECT);
				} else {
					idiomCheckHandler.sendEmptyMessage(IDIOM_WRONG);
				}
			};
		}.start();
	}

	private Handler idiomCheckHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 图片渐变模糊度始终
			AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
			// 渐变时间
			aa.setDuration(4000);
			// 消失后不再显示
			aa.setFillAfter(true);

			switch (msg.what) {
			case IDIOM_WRONG:
				// 显示错误
				idiom_check_iv
						.setImageResource(R.drawable.game_idiom_check_wrong);
				idiom_check_iv.startAnimation(aa);

				break;
			case IDIOM_CORRECT:

				// 显示正确
				idiom_check_iv
						.setImageResource(R.drawable.game_idiom_check_correct);
				idiom_check_iv.startAnimation(aa);
				break;
			default:
				break;
			}
		}
	};

	//clockHandler
	private void countDownThread() {
		new Thread() {
			public void run() {
				for (int i = COUNT_DOWN_SECOND; i >= 0; i--) {
					if (!clockStop) {
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

	private Handler clockHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what != 0) {
				clock_show_tv.setText(msg.what + "");
				Log.d("test", msg.what + "");
			} else if (msg.what == 0) {
				clock_show_tv.setText("时间到!");
				clock_show_tv.setTextSize(20);
				clock_show_tv.setTextColor(Color.RED);
			}

			// 如果倒计时结束，则发送超时请求,前提是myUid.equals(currentUid)
			if (msg.what == 0 && myUid != null && currentUid != null
					&& myUid.equals(currentUid)) {

				doTimeOutAction();
			}
		}
	};

	/********************************************************************************************************************************
	 * 按键监听
	 ********************************************************************************************************************************/
	// GridView监听
	class MyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg2) {
			case EXIT:
				doExitAction();
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
			this.exit();
		}
		return false;
	}
	
	private void exit(){
		AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);

		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("确定退出房间吗?");

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

	// 自定义gridAdapter
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
			tv.setLayoutParams(new GridView.LayoutParams(60, 48));
			tv.setTextColor(Color.WHITE);
			if (position == 0) {
				tv.setBackgroundResource(R.drawable.game_exit_background);
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

	@Override
	public void onMesssageReceived(IMessage msg) {
		// TODO Auto-generated method stub
		
	}
}
