package dashanzi.android.activity;

import java.util.Arrays;
import java.util.List;

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
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.User;
import dashanzi.android.dto.notify.QuitNotifyMsg;
import dashanzi.android.dto.notify.RoomNotifyMsg;
import dashanzi.android.dto.notify.StartNotifyMsg;
import dashanzi.android.dto.request.InputRequestMsg;
import dashanzi.android.dto.request.RefreshRoomRequestMsg;
import dashanzi.android.dto.request.TimeoutRequestMsg;
import dashanzi.android.dto.response.InputResponseMsg;
import dashanzi.android.dto.response.RefreshRoomResponseMsg;
import dashanzi.android.dto.response.TimeoutResponseMsg;
import dashanzi.android.util.ToastUtil;

public class Game extends Activity implements IMessageHandler {

	private static final String tag = "Game";
	private IdiomGameApp app = null;
	
	//游戏是否就绪
	private boolean gameReady = false;
	public boolean isGameReady() {
		return gameReady;
	}
	public void setGameReady(boolean gameReady) {
		this.gameReady = gameReady;
	}

	// gridView
	private static final int EXIT = 0;
	private static final int SUBMIT = 1;
	private static final int HELP = 2;

	// 倒计时秒数
	private static final int COUNT_DOWN_SECOND = 10;

	// 组件
	private RelativeLayout current_player_layout;// 当前活跃玩家layout，背景颜色区别于观看玩家
	private TextView idiom_show_tv;// 显示成语词条的tv
	private ImageView idiom_check_iv;// 成语词条正误标识图片
	private TextView clock_show_tv;// 显示倒计时内容的tv
	private EditText idiom_write_et;// 录入成语词条的et
	private GridView configGrid;// 按钮gridview
	private String configData[] = { "退出", "提交", "锦囊" };

	// game状态变量

	private String currentUid = null;
	private String currentWord = null;
	private String myUid = null;
	private String gid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		// 0. app
		app = (IdiomGameApp) this.getApplication();
		app.setCurrentActivity(this);

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
		gid = intent.getStringExtra("gid");
		myUid = intent.getStringExtra("uid");
		Log.i("uid", myUid);
		this.setTitle("成语接龙" + "-" + gid + "号房间");

		// 3. 向服务端发送刷新房间信息请求
		RefreshRoomRequestMsg req = new RefreshRoomRequestMsg();
		req.setType(Constants.Type.REFRESHROOM_REQ);
		req.setGid(gid);
		app.sendMessage(req);

	}

	/************************************************************************************************************************
	 * Logic Action
	 ************************************************************************************************************************/

	// 封装uid与layoutid之间的关系，便于找到当前玩家
	int[] layoutArray = { R.id.game_player_one_layout,
			R.id.game_player_two_layout, R.id.game_player_three_layout };
	int[] playerNameArray = { R.id.game_player_one_name,
			R.id.game_player_two_name, R.id.game_player_three_name };

	@Override
	public void onMesssageReceived(IMessage msg) {
		if (msg == null) {
			Log.e(tag, "msg is null !");
			return;
		}

		// 刷新房间信息
		if (msg instanceof RefreshRoomResponseMsg) {
			RefreshRoomResponseMsg resp = (RefreshRoomResponseMsg) msg;
			Log.i(tag, "----->>> RefreshRoom Response  = " + resp.toString());
			List<User> users = resp.getUsers();
			if (users.size() > Constants.PLAYER_NUM) {
				Log.e(tag, "users.size()>3");
				return;
			}

			if(users.size() == Constants.PLAYER_NUM){
				//设置游戏为就绪状态
				this.setGameReady(true);
				
				//向服务端发送startNotifyRequest TODO
				
				
			}
			// 显示玩家名
			showPlayerNames(users);
		}

		// RoomNotify
		if (msg instanceof RoomNotifyMsg) {

			RoomNotifyMsg resp = (RoomNotifyMsg) msg;
			Log.i(tag, "----->>> RoomNotifyMsg   = " + resp.toString());

			List<User> users = resp.getUsers();
			if (users.size() > Constants.PLAYER_NUM) {
				Log.e(tag, "users.size()>3");
				return;
			}

			if(users.size() < Constants.PLAYER_NUM){//房间未满
				
				//设置房间为未就绪状态
				this.setGameReady(false);
			}
			
			
			// 显示玩家名
			showPlayerNames(users);
		}

		// 开始游戏通知
		if (msg instanceof StartNotifyMsg) {
			//判断游戏是否就绪
			if(!this.isGameReady()){
				Log.e(tag, "revieve StartNotifyMsg , but game is not ready !!");
				ToastUtil.toast(this, "房间未满,不能游戏!", android.R.drawable.ic_dialog_alert);
				return;
			}
			
			StartNotifyMsg startNotify = (StartNotifyMsg) msg;
			Log.i(tag, "----->>> StartNotifyMsg   = " + startNotify.toString());

			currentUid = startNotify.getFirstuid();
			currentWord = startNotify.getWord();
			List<User> users = startNotify.getUsers();

			if (users.size() != Constants.PLAYER_NUM) {
				Log.e(tag, "users.size() != 3");
			}

			// 显示玩家名
			showPlayerNames(users);

			// 切换玩家角色背景
			changePlayerBackground(currentUid);

			// 判断是否是自己出牌
			if (currentUid.equals(myUid)) {
				idiom_write_et.setClickable(true);
			}

			// 显示word
			showIdiomThread(0, currentWord);
		}

		// inputResponse
		if (msg instanceof InputResponseMsg) {
			
			//判断游戏是否就绪
			if(!this.isGameReady()){
				Log.e(tag, "revieve InputResponseMsg , but game is not ready !!");
				ToastUtil.toast(this, "房间未满,不能游戏!", android.R.drawable.ic_dialog_alert);
				return;
			}
			
			InputResponseMsg inputRes = (InputResponseMsg) msg;
			Log.i(tag, "----->>> InputResponseMsg = " + inputRes.toString());

			// 2. 显示结果正确与否,显示对号，叉号
			if (inputRes.getStatus().equals(Constants.Response.SUCCESS)) {

				idiomCheckThread(Constants.CheckResultType.CORRECT);
			} else if (inputRes.getStatus().equals(Constants.Response.FAILED)) {
				// 显示玩家的错误成语
				String answer = inputRes.getAnswer();
				showIdiomThread(0, answer);
				idiomCheckThread(Constants.CheckResultType.WORING);

			}

			// 1. 显示填写的成语
			currentWord = inputRes.getWord();
			showIdiomThread(2000, currentWord);

			// 3. 判断是否是自己出牌
			currentUid = inputRes.getNextUid();
			if (currentUid.equals(myUid)) {// 可编辑
				idiom_write_et.setClickable(true);
			} else {// 不可编辑
				idiom_write_et.setClickable(false);
			}

			// 切换玩家角色背景
			changePlayerBackground(currentUid);
		}

		// 超时响应
		if (msg instanceof TimeoutResponseMsg) {
			//判断游戏是否就绪
			if(!this.isGameReady()){
				Log.e(tag, "revieve TimeoutResponseMsg , but game is not ready !!");
				ToastUtil.toast(this, "房间未满,不能游戏!", android.R.drawable.ic_dialog_alert);
				return;
			}
			TimeoutResponseMsg resp = (TimeoutResponseMsg) msg;
			Log.i(tag, "----->>> TimeoutResponseMsg = " + resp.toString());
			currentWord = resp.getWord();
			currentUid = resp.getNextuid();
			// 显示word
			showIdiomThread(0, currentWord);
			// 显示超时
			idiomCheckThread(Constants.CheckResultType.TIME_OUT);

			// 切换背景颜色
			changePlayerBackground(currentUid);

			// 判断是否是自己出牌
			if (currentUid.equals(myUid)) {// 可编辑
				idiom_write_et.setClickable(true);
			} else {// 不可编辑
				idiom_write_et.setClickable(false);
			}
		}

	}

	private void changePlayerBackground(String currentUserId) {
		Log.i(tag, "-------------->>>  changePlayerBackground");
		// 1. 恢复所有background
		for (int i = 0; i < layoutArray.length; i++) {
			RelativeLayout rl = (RelativeLayout) findViewById(layoutArray[i]);
			rl.setBackgroundResource(R.drawable.player_area_background);
		}
		// 设置当前玩家背景
		current_player_layout = (RelativeLayout) findViewById(layoutArray[Integer
				.parseInt(currentUid.substring(currentUid.length() - 1))]);
		current_player_layout.setBackgroundColor(Color.RED);

	}

	private void showPlayerNames(List<User> userList) {
		Log.i(tag, "-------------->>>   showPlayerNames");
		//1. 首先要清空所有用户信息
		for(int i=0;i<playerNameArray.length;i++){
			TextView playerName = (TextView) findViewById(playerNameArray[i]);
			playerName.setText(" ");
		}
		
		//2. 重新显示用户信息
		for (User user : userList) {
			String tempUid = user.getUid();
			String index = user.getUid().substring(tempUid.length() - 1);
			TextView playerName = (TextView) findViewById(playerNameArray[Integer
					.parseInt(index)]);
			playerName.setText(user.getName());
		}

	}

	// 超時操作
	private void doTimeOutAction() {
		// 发送timeout请求
		TimeoutRequestMsg req = new TimeoutRequestMsg();
		req.setType(Constants.Type.TIMEOUT_REQ);
		req.setGid(gid);
		req.setUid(myUid);
		app.sendMessage(req);
	}

	/************************************************************************************************************************
	 * GridView Action
	 ************************************************************************************************************************/
	// 退出房间操作
	private void doExitAction() {
		this.quitRoom();
	}

	// 提交成语操作
	private void doSubmitAction() {

		// 2. 发送提交请求
		String idiom_write = idiom_write_et.getText().toString();
		if (idiom_write == null || idiom_write.trim().length() != 4) {
			ToastUtil
					.toast(this, "成语格式错误!", android.R.drawable.ic_dialog_alert);
			return;
		}
		InputRequestMsg inputReq = new InputRequestMsg();
		inputReq.setGid(gid);
		inputReq.setType(Constants.Type.INPUT_REQ);
		inputReq.setUid(myUid);
		inputReq.setWord(idiom_write.toString());
		Log.i(tag, ">>>>> input request = " + inputReq.toString());
		app.sendMessage(inputReq);
	}

	// 锦囊操作
	private void doHelpAction() {
		// TODO Auto-generated method stub
		// 1. 发送锦囊请求
	}

	/************************************************************************************************************************
	 * Handler
	 ************************************************************************************************************************/
	// showIdiomHandler
	private void showIdiomThread(final int delayms, final String idiom) {
		// 倒计时
		if (lastRunner != null) {
			lastRunner.shutDownThread();
		}
		countDownThread();

		new Thread() {
			public void run() {
				// 等待delayms
				try {
					Thread.sleep(delayms);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (idiom != null) {
					for (int i = 0; i <= idiom.length(); i++) {
						Message msg = new Message();
						Bundle b = new Bundle();// 存放数据
						b.putString("idiom", idiom.substring(0, i));
						msg.setData(b);
						showIdiomHandler.sendMessage(msg);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
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

			Bundle b = msg.getData();
			String idiom = b.getString("idiom");
			idiom_show_tv.setText(idiom);
		}
	};

	// idiomCheckHandler
	private void idiomCheckThread(final int resultType) {
		new Thread() {
			public void run() {

				switch (resultType) {

				case Constants.CheckResultType.CORRECT:
					idiomCheckHandler
							.sendEmptyMessage(Constants.CheckResultType.CORRECT);
					break;
				case Constants.CheckResultType.WORING:
					idiomCheckHandler
							.sendEmptyMessage(Constants.CheckResultType.WORING);
					break;
				case Constants.CheckResultType.TIME_OUT:
					idiomCheckHandler
							.sendEmptyMessage(Constants.CheckResultType.TIME_OUT);
					break;
				default:
					break;
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
			case Constants.CheckResultType.CORRECT:
				
				// 显示正确
				idiom_check_iv
				.setImageResource(R.drawable.game_idiom_check_correct);
				idiom_check_iv.startAnimation(aa);
				break;

			case Constants.CheckResultType.WORING:
				// 显示错误
				idiom_check_iv
						.setImageResource(R.drawable.game_idiom_check_wrong);
				idiom_check_iv.startAnimation(aa);

				break;
			case Constants.CheckResultType.TIME_OUT:

				// 显示正确
				idiom_check_iv
						.setImageResource(R.drawable.game_idiom_check_timeout);
				idiom_check_iv.startAnimation(aa);
				break;
			default:
				break;
			}
		}
	};

	// clockHandler
	private Runner lastRunner = null;

	private void countDownThread() {
		Runner r = new Runner();
		lastRunner = r;
		new Thread(r).start();
	}

	class Runner implements Runnable {
		private boolean flag = true;

		public void run() {
			int i = COUNT_DOWN_SECOND;
			while (isShutDown() && i >= 0) {

				clockHandler.sendEmptyMessage(i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i--;
			}
		}

		public synchronized void shutDownThread() {
			flag = false;
		}

		public synchronized boolean isShutDown() {
			return flag;
		}
	}

	private Handler clockHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			clock_show_tv.setTextSize(40);
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

				Log.i(tag, "------------>>>>>> time out !! " + "uid = " + myUid);
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
			this.quitRoom();
		}
		return false;
	}

	private void quitRoom() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);

		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("确定退出房间吗?");

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {

				// 1. 发送退出房间通知
				QuitNotifyMsg quitNotify = new QuitNotifyMsg();
				quitNotify.setType(Constants.Type.QUIT_NOTIFY);
				quitNotify.setGid(gid);
				quitNotify.setUid(myUid);
				app.sendMessage(quitNotify);

				// 2. finish activity
				Game.this.finish();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {

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
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
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
}
