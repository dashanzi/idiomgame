package dashanzi.android.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import dashanzi.android.Constants;
import dashanzi.android.IdiomGameApp;
import dashanzi.android.R;
import dashanzi.android.db.DBUtil;
import dashanzi.android.dto.ServerInfo;
import dashanzi.android.util.ToastUtil;

/**
 * 服务器地址配置按钮监听
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class MyConfigOnClickListener implements OnClickListener {
	
	private static final String tag = "MyConfigOnClickListener";
	private Activity context;
	public MyConfigOnClickListener(Activity context ){
		this.context = context;
	}
	@Override
	public void onClick(View v) {
		Log.i(tag, "--->> login btn onClickListener !");
		int viewTag = (Integer) v.getTag();
		if (viewTag == Constants.ButtonTag.SERVER_CONFIG_BTN) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					context);
			LayoutInflater factory = LayoutInflater.from(context);
			View textEntryView = factory.inflate(R.layout.networkconfig,
					null);
			builder.setIcon(R.drawable.login_config_image);
			builder.setTitle("网络配置");
			builder.setView(textEntryView);

			final EditText serverIpEt = (EditText) textEntryView
					.findViewById(R.id.config_server_ip_edit_text);
			
			//显示当前ip
			TextView currentIp = (TextView)textEntryView.findViewById(R.id.current_server_ip_show);
			currentIp.setText(DBUtil.getServerInfo(context).getIp());

			// 操作
			builder.setPositiveButton("完成",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {
							boolean formatCorrect = true;

							if (serverIpEt == null
									|| serverIpEt.getText() == null) {
								Log.e(tag, "ip is null");
								ToastUtil.toastAlert(context,
										"Ip形式错误! 参考：175.41.135.231",
										android.R.drawable.ic_dialog_alert);
								formatCorrect = false;
								return;
							}
							String temp_ip = serverIpEt.getText()
									.toString();

							if (!temp_ip.contains(".")) {
								Log.e(tag, "format error 111 not contains '.' ");
								ToastUtil.toastAlert(context,
										"Ip形式错误! 参考：175.41.135.231",
										android.R.drawable.ic_dialog_alert);
								formatCorrect = false;
								return;
							}
							
							//判断是否为4段
							String temp = temp_ip.replace(".", ":");
							if(temp.contains(":")){
								String[] v = temp.split(":");
								if(v.length!=4){
									Log.e(tag, "format error 222 not four block");
									ToastUtil.toastAlert(context,
											"Ip形式错误! 参考：175.41.135.231",
											android.R.drawable.ic_dialog_alert);
									formatCorrect = false;
									return;
								}
							}

							try {
								InetAddress ip = InetAddress.getByName(temp_ip);

							} catch (UnknownHostException e1) {
								//ip形式错误
								e1.printStackTrace();
								Log.e(tag, "format error 333 ip format error! ");
								ToastUtil.toastAlert(context,
										"Ip形式错误! 参考：175.41.135.231",
										android.R.drawable.ic_dialog_alert);
								formatCorrect = false;
								return;
							}

							if (formatCorrect) {
								Log.e(tag, "---SET ip =" + temp_ip);
								//TODO  这个时候要进行数据库操作了
								ServerInfo dto = new ServerInfo();
								dto.setIp(temp_ip);
								dto.setPort(Constants.DataBase.DEFAULT_PORT);
								boolean actionResult = DBUtil.setServerInfo(context, dto);
								
								//close socket
								IdiomGameApp app = (IdiomGameApp) context.getApplication();
								
								//每次配置ip后，要断开连接
								app.doDisconnect();
								
								if(actionResult){
									ToastUtil.toastAlert(context, "配置成功!", 0);
								}else{
									ToastUtil.toastAlert(context, "配置失败!", android.R.drawable.ic_dialog_alert);
								}
							}
						}
					});
			// 操作
			builder.setNegativeButton("返回",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});
			builder.create().show();
		}
	}
}