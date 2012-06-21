package dashanzi.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import dashanzi.android.dto.request.RefreshRequestMsg;
import dashanzi.android.dto.response.RefreshResponseMsg;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.house);

		// 1. 显示欢迎词
		Intent intent = this.getIntent();
		String userName = intent.getStringExtra("name");
		welcomeTv = (TextView) findViewById(R.id.house_welcome_user_text);
		if (userName.trim() != null) {
			welcomeTv.setText(userName + ", 欢迎您!");
		}

		// 2. list监听
		House.this.getListView().setOnItemClickListener(item_click);

		// 3. 此时向服务端发送refresh_request
		app = (IdiomGameApp) this.getApplication();
		RefreshRequestMsg refreshReq = new RefreshRequestMsg();
		refreshReq.setType(Constants.Type.REFRESH_REQ);
		app.sendMessage(refreshReq);

		// 4. 设置快速加入监听
		quickSelectBtn = (Button) findViewById(R.id.house_quick_select_btn);
		quickSelectBtn.setOnClickListener(new MyOnClickListener());
	}

	/********************************************************************************************************************************
	 * Logic Action
	 ********************************************************************************************************************************/

	@Override
	public void onMesssageReceived(IMessage msg) {
		// TODO Auto-generated method stub
		RefreshResponseMsg refreshRes = (RefreshResponseMsg) msg;
		if (refreshRes == null) {
			Log.e(tag, "refreshRes si null");
			return;
		}
		// 1. 获得所有房间信息、初始化房间列表
		List<GroupInfo> groups = refreshRes.getGroupInfoList();
		for (GroupInfo group : groups) {
			HashMap<String, Object> houseInfo = new HashMap<String, Object>();
			houseInfo.put(Constants.HouseList.HEADER_IMAGE,
					R.drawable.house_list_image);
			houseInfo.put(Constants.HouseList.HOUSE_NUM, group.getGid());
			houseInfo.put(Constants.HouseList.HOUSE_REST_PLACE_NUM,
					group.getState());
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
		// 设置显示ListView
		setListAdapter(houseListAdapter);

	}

	// 用于生成test数据 TODO
	private void getHouseList() {
		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> houseInfo = new HashMap<String, Object>();
			houseInfo.put(Constants.HouseList.HEADER_IMAGE,
					R.drawable.house_list_image);
			houseInfo.put(Constants.HouseList.HOUSE_NUM, i + 10 + "");
			houseInfo.put(Constants.HouseList.HOUSE_REST_PLACE_NUM, "3/3");
			houseList.add(houseInfo);
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

			AlertDialog.Builder builder = new AlertDialog.Builder(House.this);
			builder.setIcon(android.R.drawable.ic_menu_help);

			HashMap<String, Object> item = (HashMap<String, Object>) houseListAdapter
					.getItem(arg2);
			gid_selected = (String) item.get(Constants.HouseList.HOUSE_NUM);

			builder.setTitle("确定进入" + gid_selected + "号房间吗?");

			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {

							// 1.向服务端发送登陆房间请求 TODO

							// 2. 页面转向Game
							Intent intent = new Intent();
							intent.putExtra("gid", gid_selected);
							intent.setClass(House.this, Game.class);
							startActivity(intent);
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
			// TODO Auto-generated method stub
			if (houseList == null || houseList.size() < 1) {
				Log.e(tag, "houseList is null");
				return;
			}

			gid_selected = quickSelectHouseNum();

			// 1. 向服务端发送登陆房间请求

			// 2. 页面转向Game
			Intent intent = new Intent();
			intent.putExtra("gid", gid_selected);
			intent.setClass(House.this, Game.class);
			startActivity(intent);
		}

		// TODO 第一个房间号作为选择结果，可以扩展
		private String quickSelectHouseNum() {
			return (String) houseList.get(0).get(Constants.HouseList.HOUSE_NUM);
		}
	}
}
