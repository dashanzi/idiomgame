package dashanzi.android.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.iflytek.speech.SynthesizerPlayer;

import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.User;
import dashanzi.android.dto.notify.QuitNotifyMsg;
import dashanzi.android.dto.notify.RoomNotifyMsg;
import dashanzi.android.dto.notify.StartNotifyMsg;
import dashanzi.android.dto.request.GetUserInfoRequestMsg;
import dashanzi.android.dto.request.HelpRequestMsg;
import dashanzi.android.dto.request.InputRequestMsg;
import dashanzi.android.dto.request.RefreshRoomRequestMsg;
import dashanzi.android.dto.request.StartRequestMsg;
import dashanzi.android.dto.request.TimeoutRequestMsg;
import dashanzi.android.dto.response.GetUserInfoResponseMsg;
import dashanzi.android.dto.response.HelpResponseMsg;
import dashanzi.android.dto.response.InputResponseMsg;
import dashanzi.android.dto.response.RefreshRoomResponseMsg;
import dashanzi.android.dto.response.TimeoutResponseMsg;
import dashanzi.android.util.FormatUtil;
import dashanzi.android.util.ToastUtil;
import dashanzi.android.util.VoiceUtil;

/**
 * 
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 * 
 */
public class Game extends Activity implements IMessageHandler,
		IExceptionHandler {

	private final String tag = "Game";

	private IdiomGameApp app = null;

	// 游戏是否就绪
	private boolean gameReady = false;

	// 组件
	private RelativeLayout current_player_layout;// 当前活跃玩家layout，背景颜色区别于观看玩家
	private TextView idiom_show_tv;// 显示成语词条的tv
	private ImageView idiom_check_iv;// 成语词条正误标识图片
	private TextView clock_show_tv;// 显示倒计时内容的tv
	private EditText idiom_write_et;// 录入成语词条的et
	private ImageButton p1ImageBtn;
	private ImageButton p2ImageBtn;
	private ImageButton p3ImageBtn;

	private GridView configGrid;// 按钮gridview
	private String configData[] = { "退出", "提交", "锦囊" };

	// game状态变量

	private String currentUid = null;
	private String currentWord = null;
	private String myUid = null;
	private String gid = null;
	private int helpNum = 0;
	private int[] genderArray = new int[3];
	private GridAdapter gridAdapter;
	private SynthesizerPlayer synPlayer = null;

	private ImageButton voiceRecognizerBtn;
	private ToggleButton speakerBtn;
	private ImageView speakerImage;
	private boolean speakerOn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "......... Game onCreate");
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
		p1ImageBtn = (ImageButton) findViewById(R.id.game_player_one_header_image);
		p2ImageBtn = (ImageButton) findViewById(R.id.game_player_two_header_image);
		p3ImageBtn = (ImageButton) findViewById(R.id.game_player_three_header_image);
		// 设置tag
		p1ImageBtn.setTag(Constants.Game.PLAYER_1_IMAGEBTN_TAG);
		p2ImageBtn.setTag(Constants.Game.PLAYER_2_IMAGEBTN_TAG);
		p3ImageBtn.setTag(Constants.Game.PLAYER_3_IMAGEBTN_TAG);
		p1ImageBtn.setOnClickListener(new MyOnClickListener());
		p2ImageBtn.setOnClickListener(new MyOnClickListener());
		p3ImageBtn.setOnClickListener(new MyOnClickListener());

		// 配置页gridview的设置
		configGrid = (GridView) findViewById(R.id.game_gridview);
		configGrid.setNumColumns(configData.length);
		gridAdapter = new GridAdapter(this, Arrays.asList(configData));
		configGrid.setAdapter(gridAdapter);
		configGrid.setOnItemClickListener(new MyOnItemClickListener());

		// 2. 进入房间时候，设置Activity title
		Intent intent = this.getIntent();
		gid = intent.getStringExtra("gid");
		myUid = intent.getStringExtra("uid");
		String help_num = intent.getStringExtra("helpNum");
		helpNum = FormatUtil.String2Int(help_num.trim());

		this.setTitle("成语接龙" + "-" + gid + "号房间");

		// 3. 向服务端发送刷新房间信息请求

		RefreshRoomRequestMsg req = new RefreshRoomRequestMsg();
		req.setType(Constants.Type.REFRESHROOM_REQ);
		req.setGid(gid);
		app.sendMessage(req);
		Log.e(tag,
				" oncreate --->>> send RefreshRoomRequestMsg = "
						+ req.toString());

		speakerImage = (ImageView) findViewById(R.id.game_voice_speak_image);
		speakerBtn = (ToggleButton) findViewById(R.id.game_voice_speak_toggle_btn);
		speakerBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					ToastUtil.toastAlert(Game.this, "语音播报开启!", 0);
					// 换图片
					speakerImage.setBackgroundResource(android.R.drawable.ic_lock_silent_mode_off);
					// 设置开启flag
					setSpeakerOn(true);
				} else {
					ToastUtil.toastAlert(Game.this, "语音播报关闭!", 0);
					// 换图片
					speakerImage.setBackgroundResource(android.R.drawable.ic_lock_silent_mode);
					// 设置关闭flag
					setSpeakerOn(false);
				}
			}
		});

		// 语音播放
		synPlayer = SynthesizerPlayer.createSynthesizerPlayer(this, "appid="
				+ getString(R.string.app_id));

		((TextView) findViewById(android.R.id.title))
				.setGravity(Gravity.CENTER);

		// 语音识别
		voiceRecognizerBtn = (ImageButton) findViewById(R.id.voice_recognizer_btn);
		voiceRecognizerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 1. 判断是否是自己出成语
				if (currentUid == null || !myUid.equals(currentUid)) {
					ToastUtil.toastAlert(Game.this, "未到自己游戏时间!" + "\n\t稍安勿躁!",
							android.R.drawable.ic_dialog_alert);
					return;
				}
				Game.this.startActivity(new Intent(Game.this, Voice.class));
			}
		});
	}

	/************************************************************************************************************************
	 * Logic Action
	 ************************************************************************************************************************/

	// 玩家页面布局信息
	int[] layoutArray = { R.id.game_player_one_layout,
			R.id.game_player_two_layout, R.id.game_player_three_layout };
	int[] playerNameArray = { R.id.game_player_one_name,
			R.id.game_player_two_name, R.id.game_player_three_name };
	int[] playerScoreArray = { R.id.game_player_one_score,
			R.id.game_player_two_score, R.id.game_player_three_score };
	int[] playerHeaderArray = { R.id.game_player_one_header_image,
			R.id.game_player_two_header_image,
			R.id.game_player_three_header_image };

	Map<Integer, String> positionUidMap = new HashMap<Integer, String>();

	@Override
	public void onMesssageReceived(IMessage msg) {
		if (msg == null) {
			Log.e(tag, "msg is null !");
			return;
		}

		// 刷新房间信息
		if (msg instanceof RefreshRoomResponseMsg) {
			RefreshRoomResponseMsg resp = (RefreshRoomResponseMsg) msg;
			Log.e(tag, "<<<---  RefreshRoomResponseMsg  = " + resp.toString());
			List<User> users = resp.getUsers();
			if (users == null) {
				Log.e(tag, "users null");
				return;
			}

			if (users.size() > Constants.Player.PLAYER_NUM) {
				Log.e(tag, "users.size()>3");
				return;
			}

			if (users.size() == Constants.Player.PLAYER_NUM) {
				// 设置游戏为就绪状态
				this.setGameReady(true);

				// 向服务端发送startNotifyRequest
				StartRequestMsg req = new StartRequestMsg();
				req.setType(Constants.Type.START_REQ);
				req.setGid(gid);
				app.sendMessage(req);
				Log.i(tag, "--->>> send StartRequestMsg = " + req.toString());
			}

			// 显示玩家信息
			showPlayerInfo(users);
		}

		// RoomNotify
		if (msg instanceof RoomNotifyMsg) {

			RoomNotifyMsg resp = (RoomNotifyMsg) msg;
			Log.i(tag, "<<<---  RoomNotifyMsg  = " + resp.toString());

			List<User> users = resp.getUsers();
			if (users.size() > Constants.Player.PLAYER_NUM) {
				Log.e(tag, "users.size()>3");
				return;
			}

			if (users.size() == Constants.Player.PLAYER_NUM) {// 房间未满
				// 设置房间为就绪状态
				this.setGameReady(true);
			} else {
				// 判断当前游戏状态
				if (this.isGameReady()) {// 说明此时有人退出
					// 1. 停表
					this.stopTimer();
					// 2. 提示
					ToastUtil.toastAlert(this, "有人退出房间! 游戏终止!",
							android.R.drawable.ic_dialog_alert);
				}
			}

			// 显示玩家名
			showPlayerInfo(users);
		}

		// 开始游戏通知
		if (msg instanceof StartNotifyMsg) {
			// 判断游戏是否就绪
			if (!this.isGameReady()) {
				Log.e(tag, "revieve StartNotifyMsg , but game is not ready !!");
				ToastUtil.toastAlert(this, "房间未满,不能游戏!",
						android.R.drawable.ic_dialog_alert);
				return;
			}

			StartNotifyMsg startNotify = (StartNotifyMsg) msg;
			Log.i(tag, "<<<---  StartNotifyMsg  = " + startNotify.toString());

			currentUid = startNotify.getFirstuid();
			currentWord = startNotify.getWord();
			List<User> users = startNotify.getUsers();

			if (users.size() != Constants.Player.PLAYER_NUM) {
				Log.e(tag, "users.size() != 3");
			}

			// 记录玩家的性别
			for (User user : users) {
				String temp = user.getUid().substring(
						user.getUid().length() - 1);
				int index = FormatUtil.String2Int(temp);
				genderArray[index] = FormatUtil.String2Int(user.getGender());
			}

			// 显示玩家名
			showPlayerInfo(users);

			// 切换玩家角色背景
			changePlayerBackground(currentUid);

			// 判断是否是自己出牌
			checkPlayOrder(currentUid, myUid);

			// 显示word
			showIdiomThread(0, currentWord);
		}

		// inputResponse
		if (msg instanceof InputResponseMsg) {

			// 判断游戏是否就绪
			if (!this.isGameReady()) {
				Log.e(tag, "revieve InputResponseMsg, but game is not ready !!");
				ToastUtil.toastAlert(this, "房间未满,不能游戏!",
						android.R.drawable.ic_dialog_alert);
				return;
			}

			InputResponseMsg inputRes = (InputResponseMsg) msg;
			Log.i(tag, "<<<---  InputResponseMsg  = " + inputRes.toString());

			// 1. 显示填写的成语
			currentWord = inputRes.getWord();
			currentUid = inputRes.getNextUid();

			// 2.
			if (inputRes.getStatus().equals(Constants.Response.SUCCESS)) {
				// 显示结果正确
				idiomCheckThread(Constants.CheckResultType.CORRECT);
				// voice 判断是否开启
				if (isSpeakerOn()) {
					VoiceUtil.SynthesizerVoice(synPlayer, currentWord,
							genderArray[getLastUid(currentUid)]);
				}

				// 冒泡上一玩家接的成语
				ToastUtil.showIdiomToast(this, currentWord,
						getLastUid(currentUid));

			} else if (inputRes.getStatus().equals(Constants.Response.FAILED)) {
				// 显示玩家的错误成语
				String answer = inputRes.getAnswer();
				showIdiomThread(0, answer);
				idiomCheckThread(Constants.CheckResultType.WORING);
				// voice 判断是否开启
				if (isSpeakerOn()) {
					VoiceUtil.SynthesizerVoice(synPlayer, answer,
							genderArray[getLastUid(currentUid)]);
				}
				// 冒泡上一玩家接的成语
				ToastUtil.showIdiomToast(this, answer, getLastUid(currentUid));
			}

			// 显示玩家信息
			showPlayerInfo(inputRes.getUsers());

			// 成语显示区域显示当前成语
			showIdiomThread(2000, currentWord);

			// 判断是否是自己出牌
			checkPlayOrder(currentUid, myUid);

			// 切换玩家角色背景
			changePlayerBackground(currentUid);
		}

		// 超时响应
		if ((msg instanceof TimeoutResponseMsg)
				|| (msg instanceof HelpResponseMsg)) {

			// 玩家信息
			List<User> users = new ArrayList<User>();

			// 判断游戏是否就绪
			if (!this.isGameReady()) {
				Log.e(tag,
						"revieve TimeoutResponseMsg || HelpResponseMsg, but game is not ready !!");
				ToastUtil.toastAlert(this, "房间未满,不能游戏!",
						android.R.drawable.ic_dialog_alert);
				return;
			}

			if (msg instanceof TimeoutResponseMsg) {
				TimeoutResponseMsg resp = (TimeoutResponseMsg) msg;
				Log.i(tag, "<<<---  TimeoutResponseMsg  = " + resp.toString());
				currentWord = resp.getWord();
				currentUid = resp.getNextuid();
				users = resp.getUsers();
				// 显示超时
				idiomCheckThread(Constants.CheckResultType.TIME_OUT);

				// voice 判断是否开启
				if (isSpeakerOn()) {
					VoiceUtil.SynthesizerVoice(synPlayer, "超时!",
							genderArray[getLastUid(currentUid)]);
				}

				// 冒泡上一个玩家超时
				ToastUtil.showIdiomToast(this, "超时!", getLastUid(currentUid));
			} else if (msg instanceof HelpResponseMsg) {
				HelpResponseMsg resp = (HelpResponseMsg) msg;
				Log.i(tag, "<<<---  HelpResponseMsg  = " + resp.toString());
				currentWord = resp.getWord();
				currentUid = resp.getNextUid();
				users = resp.getUsers();

				// 显示使用锦囊
				idiomCheckThread(Constants.CheckResultType.HELP);

				// voice
				if (isSpeakerOn()) {
					VoiceUtil.SynthesizerVoice(synPlayer, "使用锦囊!",
							genderArray[getLastUid(currentUid)]);
				}
				// 冒泡上一个玩家使用锦囊
				ToastUtil.showIdiomToast(this, "使用锦囊!", getLastUid(currentUid));

			}

			// 显示玩家信息
			showPlayerInfo(users);

			// 显示word
			showIdiomThread(0, currentWord);

			// 切换背景颜色
			changePlayerBackground(currentUid);

			// 判断是否是自己出牌
			checkPlayOrder(currentUid, myUid);
		}

		// 查看玩家信息
		if (msg instanceof GetUserInfoResponseMsg) {

			GetUserInfoResponseMsg resp = (GetUserInfoResponseMsg) msg;
			Log.i(tag, "<<<---  GetUserInfoResponseMsg  = " + resp.toString());

			if (resp.getStatus().equals(Constants.Response.FAILED)) {
				Log.e(tag, " GetUserInfoResponseMsg failed ");
				return;
			}

			// 对话框显示玩家信息
			AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
			LayoutInflater factory = LayoutInflater.from(Game.this);
			View textEntryView = factory.inflate(R.layout.userinfo, null);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("玩家信息");
			builder.setView(textEntryView);

			TextView name_tv = (TextView) textEntryView
					.findViewById(R.id.user_name_show_tv);
			TextView gender_tv = (TextView) textEntryView
					.findViewById(R.id.user_gender_show_tv);
			TextView score_tv = (TextView) textEntryView
					.findViewById(R.id.user_score_show_tv);
			TextView level_tv = (TextView) textEntryView
					.findViewById(R.id.user_level_show_tv);
			name_tv.setText(resp.getName());
			if (resp.getGender().equals(Constants.Player.MAN + "")) {
				gender_tv.setText("男");
			} else if (resp.getGender().equals(Constants.Player.FEMALE + "")) {
				gender_tv.setText("女");
			} else {
				gender_tv.setText("?");
			}
			score_tv.setText(resp.getScore());
			level_tv.setText(resp.getLevel());

			builder.setNegativeButton("返回",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					});
			// 显示对话框
			builder.create().show();
		}
	}

	private int getLastUid(String uid) {
		int last_uid = -1;

		if (uid == null || uid.trim().length() == 0) {
			Log.e(uid, "currentUid is null");
			return last_uid;
		}
		try {
			int current_uid = FormatUtil
					.String2Int(uid.substring(uid.length() - 1));
			switch (current_uid) {
			case Constants.Player.PlAYER_ONE:// 当前玩家1
				last_uid = Constants.Player.PlAYER_THREE;// 上一个玩家应该为3
				break;
			case Constants.Player.PlAYER_TWO:// 当前玩家2
				last_uid = Constants.Player.PlAYER_ONE;// 上一个玩家应该为1
				break;
			case Constants.Player.PlAYER_THREE:// 当前玩家3
				last_uid = Constants.Player.PlAYER_TWO;// 上一个玩家应该为2
				break;
			default:
				break;

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return last_uid;
	}

	private void checkPlayOrder(String currentUid2, String myUid2) {
		// 未轮到自己游戏的时候，将editText设置为不可编辑
		if (currentUid2.equals(myUid2)) {
			idiom_write_et.setFocusableInTouchMode(true);
		} else {
			idiom_write_et.setFocusable(false);
		}
	}

	private void changePlayerBackground(String currentUserId) {
		Log.i(tag, ".........  changePlayerBackground");
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

	private void showPlayerInfo(List<User> userList) {
		Log.i(tag, ".........   showPlayerNames");
		// 1. 首先要清空所有用户信息
		for (int i = 0; i < playerNameArray.length; i++) {
			// 用户名
			TextView playerName = (TextView) findViewById(playerNameArray[i]);
			playerName.setText(" ");
			// 积分
			TextView playerScore = (TextView) findViewById(playerScoreArray[i]);
			playerScore.setText(" ");

			// 头像
			ImageButton headerImage = (ImageButton) findViewById(playerHeaderArray[i]);
			headerImage
					.setImageResource(R.drawable.game_player_space_header_image);

			// 清空map
			positionUidMap.clear();
		}

		// 2. 重新显示用户信息
		for (User user : userList) {
			String tempUid = user.getUid();
			String index = user.getUid().substring(tempUid.length() - 1);
			int position = FormatUtil.String2Int(index);

			// 记录imageBtn与uid的关系
			positionUidMap.put(position, tempUid);

			// 用户名
			TextView playerName = (TextView) findViewById(playerNameArray[position]);
			playerName.setText(user.getName());

			// 积分
			TextView playerScore = (TextView) findViewById(playerScoreArray[position]);
			playerScore.setText(user.getScore());

			// 用户头像
			ImageButton headerImage = (ImageButton) findViewById(playerHeaderArray[position]);
			if (user.getGender() == null) {
				Log.e(tempUid, "gender is null ! uid =" + tempUid);
				return;
			}
			if (user.getHeaderImageId() == null) {
				Log.e(tempUid, "headerImageId is null ! uid =" + tempUid);
				return;
			}

			if (FormatUtil.String2Int(user.getGender()) == Constants.Player.MAN) {
				headerImage.setImageResource(app.getManHeaderIdArray()[Integer
						.parseInt(user.getHeaderImageId())]);
			} else if (FormatUtil.String2Int(user.getGender()) == Constants.Player.FEMALE) {
				headerImage
						.setImageResource(app.getFemaleHeaderIdArray()[Integer
								.parseInt(user.getHeaderImageId())]);
			}

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
		Log.i(tag, "--->>> send TimeoutRequestMsg = " + req.toString());
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
		// 1. 判断是否是自己出成语
		if (!myUid.equals(currentUid)) {
			ToastUtil.toastAlert(this, "未到自己游戏时间!\n\t  稍安勿躁!",
					android.R.drawable.ic_dialog_alert);
			return;
		}

		// 2. 发送提交请求
		String idiom_write = idiom_write_et.getText().toString();
		if (idiom_write == null
				|| idiom_write.trim().length() < Constants.Game.MIN_IDIOM_WORD_NUM
				|| idiom_write.trim().length() > Constants.Game.MAX_IDIOM_WORD_NUM) {// 成语字数范围：
																						// 3-16个字
			ToastUtil.toastAlert(this, "成语格式错误!",
					android.R.drawable.ic_dialog_alert);
			return;
		}
		InputRequestMsg inputReq = new InputRequestMsg();
		inputReq.setGid(gid);
		inputReq.setType(Constants.Type.INPUT_REQ);
		inputReq.setUid(myUid);
		inputReq.setWord(idiom_write.toString());
		app.sendMessage(inputReq);

		// 清空编辑框
		idiom_write_et.setText("");
		Log.i(tag, "--->>> send InputRequestMsg = " + inputReq.toString());
	}

	// 锦囊操作
	private void doHelpAction() {
		// 1. 判断是否是自己出成语
		if (!myUid.equals(currentUid)) {
			ToastUtil.toastAlert(this, "剩余锦囊 " + helpNum + " 个!" + "\n\t稍安勿躁!",
					android.R.drawable.ic_dialog_alert);
			return;
		}

		// 判断锦囊是否有剩余
		if (helpNum <= 0) {
			ToastUtil.toastAlert(this, "锦囊已经用尽！",
					android.R.drawable.ic_dialog_alert);
			return;
		} else {
			// 锦囊数减去1
			helpNum--;
			ToastUtil.toastAlert(this, "剩余锦囊 " + helpNum + " 个!",
					android.R.drawable.ic_dialog_alert);
		}

		// 发送锦囊请求
		HelpRequestMsg req = new HelpRequestMsg();
		req.setType(Constants.Type.HELP_REQ);
		req.setGid(gid);
		req.setUid(myUid);
		app.sendMessage(req);
		Log.i(tag, "--->>> send HelpRequestMsg = " + req.toString());
	}

	/************************************************************************************************************************
	 * Handler
	 ************************************************************************************************************************/
	// showIdiomHandler
	private void showIdiomThread(final int delayms, final String idiom) {
		// 终止当前计时器
		stopTimer();

		// 重新启动新的定时器
		restartTimer();

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
				case Constants.CheckResultType.HELP:
					idiomCheckHandler
							.sendEmptyMessage(Constants.CheckResultType.HELP);
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

				// 显示超时
				idiom_check_iv
						.setImageResource(R.drawable.game_idiom_check_timeout);
				idiom_check_iv.startAnimation(aa);
				break;
			case Constants.CheckResultType.HELP:
				// 显示使用锦囊
				idiom_check_iv
						.setImageResource(R.drawable.game_idiom_check_help);
				idiom_check_iv.startAnimation(aa);
				break;
			default:
				break;
			}
		}
	};

	// clockHandler
	private Runner currentTimerRunner = null;

	private void stopTimer() {
		if (currentTimerRunner != null) {
			currentTimerRunner.shutDownThread();
		}
	}

	private void restartTimer() {
		Runner r = new Runner();
		currentTimerRunner = r;
		new Thread(r).start();
	}

	class Runner implements Runnable {
		private boolean flag = true;

		public void run() {
			int i = Constants.Game.COUNT_DOWN_SECOND;
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
			clock_show_tv.setTextColor(Color.BLACK);
			if (msg.what != 0) {
				clock_show_tv.setText(msg.what + "");
				// Log.i("test", msg.what + "");
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

	// 玩家头像btn监听
	class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			GetUserInfoRequestMsg req = new GetUserInfoRequestMsg();
			req.setType(Constants.Type.GETUSERINFO_REQ);
			req.setGid(gid);

			int tag = (Integer) v.getTag();
			String uid = positionUidMap.get(tag);
			if (uid == null) {
				Log.e("Game", " press space header !!");
				return;
			}
			req.setUid(positionUidMap.get(tag));// tag与uid末尾对应
			app.sendMessage(req);
			Log.i(Game.this.tag,
					"--->>> send GetUserInfoRequestMsg = " + req.toString());
		}
	}

	// GridView监听
	class MyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg2) {
			case Constants.Game.EXIT:
				doExitAction();
				break;
			case Constants.Game.SUBMIT:
				doSubmitAction();
				break;
			case Constants.Game.HELP:
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
				intent.setClass(Game.this, About.class);
				Game.this.startActivity(intent);
			}
		} else {
			Log.e(tag, "onMenuItemSelected item is null ");
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
				Log.e(tag,
						"--->>> send QuitNotifyMsg = " + quitNotify.toString());

				// 2. 终止timer
				stopTimer();

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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setContentView(R.layout.game);
			// 横屏
		} else {
			setContentView(R.layout.game);// 竖屏
		}

		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void exceptionCatch() {
		Log.e(tag, "socket connect exception !");
		ToastUtil.toastAlert(Game.this, "网络连接异常!",
				android.R.drawable.ic_dialog_alert);
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.setCurrentActivity(this);

		// 获得语音识别结果
		if (app.getVoiceIdiom() != null) {
			Log.e(tag, "Game onResume ---" + app.getVoiceIdiom());
			idiom_write_et.setText(app.getVoiceIdiom());
		}
	}

	/********************************************************************************************************************************
	 * getter and setter
	 ********************************************************************************************************************************/
	public boolean isGameReady() {
		return gameReady;
	}

	public void setGameReady(boolean gameReady) {
		this.gameReady = gameReady;
	}

	public boolean isSpeakerOn() {
		return speakerOn;
	}

	public void setSpeakerOn(boolean speakerOn) {
		this.speakerOn = speakerOn;
	}
}
