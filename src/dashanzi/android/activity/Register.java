package dashanzi.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
	private EditText name = null;
	private EditText password = null;
	private RadioGroup gender = null;
	private int gender_select = Constants.Player.MAN;// 默认为man
	private Button registerBtn = null;
	private Button configBtn = null;

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
			//在app中记录lastRegisterName
			app.setLastRegisterName(name.getText().toString());
			
			ToastUtil.toast(this, "注册成功!", R.drawable.game_idiom_check_correct);
		} else {
			ToastUtil.toast(this, "注册失败!", android.R.drawable.ic_dialog_alert);
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
					RegisterRequestMsg req = new RegisterRequestMsg();
					req.setName(name.getText().toString());
					req.setPassword(password.getText().toString());
					req.setGender(gender_select);
					req.setType(Constants.Type.REGISTER_REQ);
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
