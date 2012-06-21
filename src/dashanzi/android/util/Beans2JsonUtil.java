package dashanzi.android.util;

import org.json.JSONException;
import org.json.JSONObject;

import dashanzi.android.Constants;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.notify.LogoutNotifyMsg;
import dashanzi.android.dto.notify.QuitNotifyMsg;
import dashanzi.android.dto.request.InputRequestMsg;
import dashanzi.android.dto.request.JoinRequestMsg;
import dashanzi.android.dto.request.LoginRequestMsg;
import dashanzi.android.dto.request.RefreshRequestMsg;
import dashanzi.android.dto.request.TimeoutRequestMsg;

public class Beans2JsonUtil {

	private static String getJsonStrFromLoginRequest(LoginRequestMsg bean)
			throws JSONException {

		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("name", bean.getName());
		body.put("possword", bean.getPassword());
		json.put(Constants.JSON.BODY, body);
		return json.toString();

	}

	private static String getJsonStrFromRefreshRequest(RefreshRequestMsg bean)
			throws JSONException {

		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		return json.toString();
	}

	private static String getJsonStrFromJoinRequest(JoinRequestMsg bean)
			throws JSONException {

		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("gid", bean.getGid());
		body.put("name", bean.getName());
		json.put(Constants.JSON.BODY, body);
		return json.toString();
	}

	private static String getJsonStrFromInputRequest(InputRequestMsg bean)
			throws JSONException {

		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("gid", bean.getGid());
		body.put("uid", bean.getUid());
		body.put("word", bean.getWord());
		json.put(Constants.JSON.BODY, body);
		return json.toString();
	}

	private static String getJsonStrFromTimeOutRequest(TimeoutRequestMsg bean)
			throws JSONException {
		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("gid", bean.getGid());
		body.put("uid", bean.getUid());
		json.put(Constants.JSON.BODY, body);
		return json.toString();
	}

	private static String getJsonStrFromQuitNotify(QuitNotifyMsg bean)
			throws JSONException {
		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("gid", bean.getGid());
		body.put("uid", bean.getUid());
		json.put(Constants.JSON.BODY, body);
		return json.toString();
	}

	private static String getJsonStrFromLogoutNotify(LogoutNotifyMsg bean)
			throws JSONException {
		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("name", bean.getName());
		json.put(Constants.JSON.BODY, body);
		return json.toString();
	}

	public static String getJsonStr(IMessage msg) {
		String s = null;
		if (msg instanceof LoginRequestMsg) {
			try {
				s = getJsonStrFromLoginRequest((LoginRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else {
			return null;
		}

		return s;

	}
}
