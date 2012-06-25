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
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.request.RegisterRequestMsg;
import dashanzi.android.dto.response.RegisterResponseMsg;
import dashanzi.android.util.ToastUtil;

public class Register extends Activity implements IMessageHandler {

	private static final String tag = "Register";
	private IdiomGameApp app = null;
	private EditText name = null;
	private EditText password = null;
	private RadioGroup gender = null;
	private int gender_select = -1;
	private Button registerBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.register);

		app = (IdiomGameApp) this.getApplication();
		app.setCurrentActivity(this);
		app.setAboutThreadIsInterrupt(true);//终止about thread

		name = (EditText) findViewById(R.id.register_edittext_username);
		password = (EditText) findViewById(R.id.register_edittext_password);
		gender = (RadioGroup) findViewById(R.id.register_gender);
		gender.setOnCheckedChangeListener(new GenderCheckedChangeListener());
		registerBtn = (Button) findViewById(R.id.register_button_register);
		registerBtn.setOnClickListener(new MyBtnOnClickListener());
	}

	/*******************************************************************************
	 * 按钮监听
	 ******************************************************************************/
	@Override
	public void onMesssageReceived(IMessage msg) {
		if(!(msg instanceof RegisterResponseMsg)){
			Log.e(tag, "msg instanceof RegisterResponseMsg is error !");
			return;
		}
		
		RegisterResponseMsg resp = (RegisterResponseMsg)msg;
		Log.i(tag, "<<<---  RegisterResponseMsg  = " + resp.toString());
		
		if(resp.getStatus().equals(Constants.Response.SUCCESS)){
			ToastUtil.toast(this, "注册成功!", R.drawable.game_idiom_check_correct);
		}else{
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
			RegisterRequestMsg req = new RegisterRequestMsg();
			req.setName(name.getText().toString());
			req.setPassword(password.getText().toString());
			req.setGender(gender_select);
			req.setType(Constants.Type.REGISTER_REQ);
			app.sendMessage(req);
			Log.i(tag, "--->>> send RegisterRequestMsg = " + req.toString());
		}
	}

	class GenderCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.register_gender_man:
				gender_select = 1;// man
				break;
			case R.id.register_gender_female:
				gender_select = 0;// female
				break;
			default:
				// do nothing
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		Log.e("Register", "onResume");
		super.onResume();
		app.setCurrentActivity(this);
		// stop about thread
		app.setAboutThreadIsInterrupt(true);
	}
}
