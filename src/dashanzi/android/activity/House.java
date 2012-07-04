package dashanzi.android.activity;

import java.util.ArrayList;
import java.util.Collections;
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
import dashanzi.android.dto.GroupComparator;
import dashanzi.android.dto.GroupInfo;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.notify.LogoutNotifyMsg;
import dashanzi.android.dto.request.JoinRequestMsg;
import dashanzi.android.dto.request.RefreshRequestMsg;
import dashanzi.android.dto.response.JoinResponseMsg;
import dashanzi.android.dto.response.RefreshResponseMsg;
import dashanzi.android.util.ToastUtil;

/**
 * 游戏大厅
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class House extends ListActivity implements IMessageHandler,
		IExceptionHandler {

	private final static String tag = "House";

	private IdiomGameApp app;

	// 组件
	private TextView welcomeTv = null;// 欢迎tv
	private Button quickSelectBtn = null;// 快速登陆btn
	private Button houseRefreshBtn = null;// 刷新大厅btn
	// houseList
	private List<HashMap<String, Object>> houseList = new ArrayList<HashMap<String, Object>>();
	private SimpleAdapter houseListAdapter = null;

	// 变量
	private String gid_selected;// 选择的房间号
	private String userName;
	private Map<String, String> groupIdStateMap = new HashMap<String, String>();// 存储gid
																				// 与
																				// state，便于登陆房间时，判断房间是否已满
	private List<GroupInfo> groups;// grouplist
	private boolean refreshBtnPressed = false;

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
		// 5. 设置刷新大厅监听
		houseRefreshBtn = (Button) findViewById(R.id.house_refresh_btn);
		houseRefreshBtn.setOnClickListener(new MyOnClickListener());
	}

	/********************************************************************************************************************************
	 * Logic Action
	 ********************************************************************************************************************************/

	@Override
	public void onMesssageReceived(IMessage msg) {
		if (msg instanceof RefreshResponseMsg) {

			// 清空一下
			houseList.clear();
			// refresh response
			RefreshResponseMsg refreshRes = (RefreshResponseMsg) msg;
			Log.i(tag, "<<<--- get RefreshResponseMsg = " + msg.toString());
			// 1. 获得所有房间信息、初始化房间列表
			groups = refreshRes.getGroupInfoList();

			// 对Groups按从gid从小到大排序
			Collections.sort(groups, new GroupComparator());

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
			
			//4. 显示刷新结果
			if(refreshBtnPressed && refreshRes.getStatus().equals(Constants.Response.SUCCESS)){
				ToastUtil.toastAlert(this, "刷新完毕!", R.drawable.game_idiom_check_correct);
			}else if(refreshBtnPressed && refreshRes.getStatus().equals(Constants.Response.FAILED)){
				ToastUtil.toastAlert(this, "刷新失败!", android.R.drawable.ic_dialog_alert);
			}
			refreshBtnPressed = false;
			
		} else if (msg instanceof JoinResponseMsg) {
			JoinResponseMsg joinRes = (JoinResponseMsg) msg;

			Log.i(tag, "<<<--- get JoinResponseMsg = " + msg.toString());

			if (joinRes.getStatus().equals(Constants.Response.SUCCESS)) {
				// 页面转向Game房间
				Intent intent = new Intent();
				intent.putExtra("gid", gid_selected);
				intent.putExtra("uid", joinRes.getUid());
				intent.putExtra("helpNum", joinRes.getHelpNum());
				intent.setClass(House.this, Game.class);
				startActivity(intent);
			} else {
				// 提示登陆失败
				ToastUtil.toastAlert(this, "登陆失败,请重新登陆!",
						android.R.drawable.ic_dialog_alert);
			}
		}
	}

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
				ToastUtil.toastAlert(House.this, "房间已满,请选择其他房间!",
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
			switch (v.getId()) {
			case R.id.house_quick_select_btn:// 快速登陆
				if (houseList == null || houseList.size() < 1) {
					Log.e(tag, "houseList is null");
					return;
				}

				gid_selected = quickSelectHouseNum();

				if (gid_selected == null) {
					ToastUtil.toastAlert(House.this, "所有房间已满!请稍等!",
							android.R.drawable.ic_dialog_alert);
				} else {
					// 向服务端发送登陆房间请求
					sendJionRequest(gid_selected, userName);
				}
				break;

			case R.id.house_refresh_btn:// 刷新大厅
				refreshBtnPressed = true;
				RefreshRequestMsg refreshReq = new RefreshRequestMsg();
				refreshReq.setType(Constants.Type.REFRESH_REQ);
				app.sendMessage(refreshReq);
				Log.i(tag,
						"--->>> send RefreshRequestMsg ="
								+ refreshReq.toString());
				break;
			default:
				break;

			}
		}

		// 优先顺序： 2/3, 1/3, 0/3 使得玩家尽快能够进入游戏
		private String quickSelectHouseNum() {
			String gid_quick = null;

			// 是否有2/3

			List<GroupInfo> tempList1 = new ArrayList<GroupInfo>();// 保存1/3的group
			List<GroupInfo> tempList2 = new ArrayList<GroupInfo>();// 保存0/3的group
			for (GroupInfo group : groups) {
				String state = group.getState();
				if (state.equals("3/3")) {
					continue;
				}
				if (state.equals("2/3")) {
					gid_quick = group.getGid();
					break;
				} else if (state.equals("1/3")) {
					tempList1.add(group);
				} else if (state.equals("0/3")) {
					tempList2.add(group);
				}

			}

			// 如果没有2/3的，则优先考虑1/3的，再考虑0/3的，都满了的时候，返回null
			if (gid_quick == null) {
				if (tempList1 != null && tempList1.size() > 0) {
					gid_quick = tempList1.get(0).getGid();
				} else if (tempList2 != null && tempList2.size() > 0) {
					gid_quick = tempList2.get(0).getGid();
				}
			}
			return gid_quick;
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

							// 回到新的登陆页面
							Intent intent = new Intent();
							intent.putExtra("name", userName);
							intent.setClass(House.this, IndexSelect.class);
							House.this.startActivity(intent);

							// 发送logoutNotify 退出大厅，准备断开连接
							LogoutNotifyMsg logout = new LogoutNotifyMsg();
							logout.setType(Constants.Type.LOGOUT_NOTIFY);
							logout.setName(userName);
							app.sendMessage(logout);
							Log.i(tag, "--->>> send  LogoutNotifyMsg = "
									+ logout.toString());

							// 断开连接 disconnect
							app.doDisconnect();

							// finish activity
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

		// 3. 此时向服务端发送refresh_request
		RefreshRequestMsg refreshReq = new RefreshRequestMsg();
		refreshReq.setType(Constants.Type.REFRESH_REQ);
		app.sendMessage(refreshReq);
		Log.i(tag, "--->>> send RefreshRequestMsg =" + refreshReq.toString());
	}

	@Override
	public void exceptionCatch() {
		Log.e(tag, "socket connect exception !");
		ToastUtil.toastAlert(House.this, "网络连接异常!",
				android.R.drawable.ic_dialog_alert);
	}
}
