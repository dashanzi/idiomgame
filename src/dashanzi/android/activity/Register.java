package dashanzi.android.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.db.DBUtil;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.ServerInfo;
import dashanzi.android.dto.request.RegisterRequestMsg;
import dashanzi.android.dto.response.RegisterResponseMsg;
import dashanzi.android.listener.MyConfigOnClickListener;
import dashanzi.android.util.ToastUtil;

public class Register extends Activity implements IMessageHandler,
		IExceptionHandler {

	private static final String tag = "Register";
	private IdiomGameApp app = null;
	
	private RegisterRequestMsg req = new RegisterRequestMsg();
	private EditText name = null;
	private EditText password = null;
	private RadioGroup gender = null;
	private int gender_select = Constants.Player.MAN;// 默认为man
	private String headerImageId = "-1";

	private Button registerBtn = null;
	private Button configBtn = null;
	private Button headerSelectBtn = null;
	private SimpleAdapter manHeaderAdapter;
	private SimpleAdapter femaleHeaderAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.register);

		app = (IdiomGameApp) this.getApplication();
		app.setCurrentActivity(this);
		app.setAboutThreadIsInterrupt(true);// 终止about thread

		name = (EditText) findViewById(R.id.register_edittext_username);
		password = (EditText) findViewById(R.id.register_edittext_password);
		gender = (RadioGroup) findViewById(R.id.register_gender);
		gender.setOnCheckedChangeListener(new GenderCheckedChangeListener());
		registerBtn = (Button) findViewById(R.id.register_button_register);
		registerBtn.setOnClickListener(new MyBtnOnClickListener());
		configBtn = (Button) findViewById(R.id.register_server_config_btn);
		configBtn.setTag(Constants.ButtonTag.SERVER_CONFIG_BTN);
		configBtn.setOnClickListener(new MyConfigOnClickListener(this));
		headerSelectBtn = (Button) findViewById(R.id.register_header_select_btn);
		headerSelectBtn.setOnClickListener(new MyHeaderSelectOnClickListener());

		// 男生头像
		ArrayList<HashMap<String, Object>> manHeaders = new ArrayList<HashMap<String, Object>>();
		int[] manHeaderArray = app.getManHeaderIdArray();
		for (int i = 0; i < manHeaderArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("HeaderImage", manHeaderArray[i]);// 添加图像资源的ID
			map.put("ItemText", "NO." + String.valueOf(i + 1));// 按序号做ItemText
			manHeaders.add(map);
		}
		// 女生头像
		int[] femaleHeaderArray = app.getFemaleHeaderIdArray();
		ArrayList<HashMap<String, Object>> femaleHeaders = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < femaleHeaderArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("HeaderImage", femaleHeaderArray[i]);// 添加图像资源的ID
			map.put("ItemText", "NO." + String.valueOf(i + 1));// 按序号做ItemText
			femaleHeaders.add(map);
		}

		// 男生头像adapter
		manHeaderAdapter = new SimpleAdapter(this,
				manHeaders,// 数据来源
				R.layout.header_item,
				new String[] { "HeaderImage", "ItemText" }, new int[] {
						R.id.HeaderImage, R.id.ItemText });

		femaleHeaderAdapter = new SimpleAdapter(this,
				femaleHeaders,// 数据来源
				R.layout.header_item,
				new String[] { "HeaderImage", "ItemText" }, new int[] {
						R.id.HeaderImage, R.id.ItemText });
	}

	class MyHeaderSelectOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
			builder.setTitle("请选择您的头像");
			LayoutInflater factory = LayoutInflater.from(Register.this);
			View dialog = factory.inflate(R.layout.headerselect, null);

			GridView gridView = (GridView) dialog
					.findViewById(R.id.dialogGridView);
			builder.setView(dialog);

			// 判断一下gender
			if (gender_select == Constants.Player.MAN) {
				// 添加并且显示
				gridView.setAdapter(manHeaderAdapter);
			} else {
				// 添加并且显示
				gridView.setAdapter(femaleHeaderAdapter);
			}

			// 添加消息处理
			gridView.setOnItemClickListener(new ItemClickListener());

			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {
							
							//设置headerImageId
							req.setHeaderImageId(headerImageId);
						}
					});
			builder.create().show();
		}

		// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
		class ItemClickListener implements OnItemClickListener {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				ToastUtil.toast(Register.this, "您选择了 "+ (arg2+1)+" 号头像", 0);
				headerImageId = arg2+"";
			}

		}
	}

	/*******************************************************************************
	 * 按钮监听
	 ******************************************************************************/
	@Override
	public void onMesssageReceived(IMessage msg) {
		if (!(msg instanceof RegisterResponseMsg)) {
			Log.e(tag, "msg instanceof RegisterResponseMsg is error !");
			return;
		}

		RegisterResponseMsg resp = (RegisterResponseMsg) msg;
		Log.i(tag, "<<<---  RegisterResponseMsg  = " + resp.toString());

		if (resp.getStatus().equals(Constants.Response.SUCCESS)) {
			// 在app中记录lastRegisterName
			app.setLastRegisterName(name.getText().toString());

			ToastUtil.toast(this, "注册成功!", R.drawable.game_idiom_check_correct);
		} else if(resp.getStatus().equals(Constants.Response.FAILED)){
			ToastUtil.toast(this, "账号已被注册!请选择其他账号!", android.R.drawable.ic_dialog_alert);
		}
	}

	/*******************************************************************************
	 * 按钮监听
	 ******************************************************************************/

	class MyBtnOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (name.getText().toString() == null
					|| name.getText().toString().trim().length() == 0) {
				ToastUtil.toast(Register.this, "亲~账号不能为空~",
						android.R.drawable.ic_dialog_alert);
				return;
			}
			if (password.getText().toString() == null
					|| password.getText().toString().trim().length() == 0) {
				ToastUtil.toast(Register.this, "亲~密码不能为空~",
						android.R.drawable.ic_dialog_alert);
				return;
			}
			// 封装注册信息，向服务端发送注册请求
			ServerInfo dto = DBUtil.getServerInfo(Register.this);
			if (dto == null || dto.getIp() == null || dto.getPort() == 0) {
				Log.e(tag, " DBUtil.getServerInfo error !");
				return;
			}
			app.setServerIp(dto.getIp());
			app.setServerPort(dto.getPort());
			Log.e(tag,
					" Register  IP = " + dto.getIp() + ": PORT = "
							+ dto.getPort());

			app.connect(new IConnectHandler() {

				@Override
				public void handle() {
					req.setName(name.getText().toString());
					req.setPassword(password.getText().toString());
					req.setGender(gender_select);
					req.setType(Constants.Type.REGISTER_REQ);
					if(req.getHeaderImageId()==null){
						//说明没有主动设置头像
						req.setHeaderImageId("0");//设置默认第一个
					}
					app.sendMessage(req);
					Log.i(tag,
							"--->>> send RegisterRequestMsg = "
									+ req.toString());
				}
			});
		}
	}

	class GenderCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.register_gender_man:
				gender_select = Constants.Player.MAN;// man
				break;
			case R.id.register_gender_female:
				gender_select = Constants.Player.FEMALE;// female
				break;
			default:
				// do nothing
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		Log.i("Register", "onResume");
		super.onResume();
		app.setCurrentActivity(this);
		// stop about thread
		app.setAboutThreadIsInterrupt(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 关闭dbHelper
		DBUtil.closeDBHelper();
	}

	@Override
	public void exceptionCatch() {
		Log.e(tag, "socket connect exception !");
		ToastUtil.toast(Register.this, "网络连接异常!",
				android.R.drawable.ic_dialog_alert);
	}
}
