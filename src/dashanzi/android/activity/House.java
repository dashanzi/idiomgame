package dashanzi.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.dto.GroupInfo;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.notify.LogoutNotifyMsg;
import dashanzi.android.dto.request.JoinRequestMsg;
import dashanzi.android.dto.request.RefreshRequestMsg;
import dashanzi.android.dto.response.JoinResponseMsg;
import dashanzi.android.dto.response.RefreshResponseMsg;
import dashanzi.android.util.ToastUtil;

public class House extends ListActivity implements IMessageHandler {

	private final static String tag = "House";

	private IdiomGameApp app;

	// 组件
	private TextView welcomeTv = null;// 欢迎tv
	private Button quickSelectBtn = null;// 快速登陆btn
	// houseList
	private ArrayList<HashMap<String, Object>> houseList = new ArrayList<HashMap<String, Object>>();
	private SimpleAdapter houseListAdapter = null;

	// 变量
	private String gid_selected;// 选择的房间号
	private String userName;
	private Map<String, String> groupIdStateMap = new HashMap<String, String>();// 存储gid
																				// 与
																				// state，便于登陆房间时，判断房间是否已满

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, ".......... House onCreate! ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.house);
		// 0. app
		app = (IdiomGameApp) this.getApplication();
		app.setCurrentActivity(this);

		// 1. 显示欢迎词
		Intent intent = this.getIntent();
		userName = intent.getStringExtra("name");
		welcomeTv = (TextView) findViewById(R.id.house_welcome_user_text);
		if (userName.trim() != null) {
			welcomeTv.setText(userName + ", 欢迎您!");
		}

		// 2. list监听
		House.this.getListView().setOnItemClickListener(item_click);

		// 3. 此时向服务端发送refresh_request
		RefreshRequestMsg refreshReq = new RefreshRequestMsg();
		refreshReq.setType(Constants.Type.REFRESH_REQ);
		app.sendMessage(refreshReq);
		Log.i(tag, "--->>> send RefreshRequestMsg =" + refreshReq.toString());

		// 4. 设置快速加入监听
		quickSelectBtn = (Button) findViewById(R.id.house_quick_select_btn);
		quickSelectBtn.setOnClickListener(new MyOnClickListener());
	}

	/********************************************************************************************************************************
	 * Logic Action
	 ********************************************************************************************************************************/

	@Override
	public void onMesssageReceived(IMessage msg) {
		if (msg instanceof RefreshResponseMsg) {
			// refresh response
			RefreshResponseMsg refreshRes = (RefreshResponseMsg) msg;
			Log.i(tag, "<<<--- get RefreshResponseMsg = " + msg.toString());
			// 1. 获得所有房间信息、初始化房间列表
			List<GroupInfo> groups = refreshRes.getGroupInfoList();
			for (GroupInfo group : groups) {
				HashMap<String, Object> houseInfo = new HashMap<String, Object>();
				houseInfo.put(Constants.HouseList.HEADER_IMAGE,
						R.drawable.house_list_image);
				houseInfo.put(Constants.HouseList.HOUSE_NUM, group.getGid());
				houseInfo.put(Constants.HouseList.HOUSE_REST_PLACE_NUM,
						group.getState());
				// 存储在map中
				groupIdStateMap.put(group.getGid(), group.getState());

				houseList.add(houseInfo);
			}

			// 2. 生成一个SimpleAdapter类型的变量来填充数据
			String[] col_show = { Constants.HouseList.HEADER_IMAGE,
					Constants.HouseList.HOUSE_NUM,
					Constants.HouseList.HOUSE_REST_PLACE_NUM };
			houseListAdapter = new SimpleAdapter(this, houseList,
					R.layout.houselist, col_show, new int[] {
							R.id.house_list_image, R.id.house_number,
							R.id.house_rest_place_num });
			// 3. 设置显示ListView
			setListAdapter(houseListAdapter);
		} else if (msg instanceof JoinResponseMsg) {
			JoinResponseMsg joinRes = (JoinResponseMsg) msg;

			Log.i(tag, "<<<--- get JoinResponseMsg = " + msg.toString());

			if (joinRes.getStatus().equals(Constants.Response.SUCCESS)) {
				// 页面转向Game房间
				Intent intent = new Intent();
				intent.putExtra("gid", gid_selected);
				intent.putExtra("uid", joinRes.getUid());
				intent.setClass(House.this, Game.class);
				startActivity(intent);
			} else {
				// 提示登陆失败
				ToastUtil.toast(this, "登陆失败,请重新登陆!",
						android.R.drawable.ic_dialog_alert);
			}
		}
	}

	// // 用于生成test数据 TODO
	// private void getHouseList() {
	// for (int i = 0; i < 20; i++) {
	// HashMap<String, Object> houseInfo = new HashMap<String, Object>();
	// houseInfo.put(Constants.HouseList.HEADER_IMAGE,
	// R.drawable.house_list_image);
	// houseInfo.put(Constants.HouseList.HOUSE_NUM, i + 10 + "");
	// houseInfo.put(Constants.HouseList.HOUSE_REST_PLACE_NUM, "3/3");
	// houseList.add(houseInfo);
	// }
	// }

	/********************************************************************************************************************************
	 * 按键监听
	 ********************************************************************************************************************************/
	// list点击监听
	private OnItemClickListener item_click = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			@SuppressWarnings("unchecked")
			HashMap<String, Object> item = (HashMap<String, Object>) houseListAdapter
					.getItem(arg2);
			gid_selected = (String) item.get(Constants.HouseList.HOUSE_NUM);

			// 判断房间是否已满，已满则提示
			String state = groupIdStateMap.get(gid_selected);
			if (state != null && state.trim().equals("3/3")) {
				ToastUtil.toast(House.this, "房间已满,请选择其他房间!",
						android.R.drawable.ic_dialog_alert);
				return;
			}

			// 未满，则提示是否进入
			AlertDialog.Builder builder = new AlertDialog.Builder(House.this);
			builder.setIcon(android.R.drawable.ic_menu_help);
			builder.setTitle("确定进入" + gid_selected + "号房间吗?");

			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {

							// 向服务端发送登陆房间请求
							sendJionRequest(gid_selected, userName);
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

	};

	// 快速加入按钮监听
	class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (houseList == null || houseList.size() < 1) {
				Log.e(tag, "houseList is null");
				return;
			}

			gid_selected = quickSelectHouseNum();

			// 向服务端发送登陆房间请求
			sendJionRequest(gid_selected, userName);
		}

		// TODO 第一个房间号作为选择结果，可以扩展
		private String quickSelectHouseNum() {
			return (String) houseList.get(0).get(Constants.HouseList.HOUSE_NUM);
		}
	}

	// 监听back键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(House.this);

			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("确定退出大厅吗?");

			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {

							// 1. 发送退出游戏通知
							LogoutNotifyMsg logout = new LogoutNotifyMsg();
							logout.setType(Constants.Type.LOGOUT_NOTIFY);
							logout.setName(userName);
							app.sendMessage(logout);
							Log.i(tag, "--->>> send  LogoutNotifyMsg = " + logout.toString());
							
							//回到新的登陆页面
							startActivity(new Intent(House.this, IndexSelect.class));
							
							// 2. finish activity
							House.this.finish();
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

	private void sendJionRequest(String gid_selected, String userName) {
		JoinRequestMsg joinReq = new JoinRequestMsg();
		joinReq.setType(Constants.Type.JOIN_REQ);
		joinReq.setGid(gid_selected);
		joinReq.setName(userName);
		app.sendMessage(joinReq);
		Log.i(tag, "--->>> send JoinRequestMsg = " + joinReq.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.setCurrentActivity(this);
	}
}
