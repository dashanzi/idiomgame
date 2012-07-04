package dashanzi.android.util;

import org.json.JSONException;
import org.json.JSONObject;

import dashanzi.android.Constants;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.notify.LogoutNotifyMsg;
import dashanzi.android.dto.notify.QuitNotifyMsg;
import dashanzi.android.dto.request.GetUserInfoRequestMsg;
import dashanzi.android.dto.request.HeartbeatRequestMsg;
import dashanzi.android.dto.request.HelpRequestMsg;
import dashanzi.android.dto.request.InputRequestMsg;
import dashanzi.android.dto.request.JoinRequestMsg;
import dashanzi.android.dto.request.LoginRequestMsg;
import dashanzi.android.dto.request.RefreshRequestMsg;
import dashanzi.android.dto.request.RefreshRoomRequestMsg;
import dashanzi.android.dto.request.RegisterRequestMsg;
import dashanzi.android.dto.request.StartRequestMsg;
import dashanzi.android.dto.request.TimeoutRequestMsg;

/**
 * 实体向Json转换工具类
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class Beans2JsonUtil {

	private static String getJsonStrFromRegisterequest(RegisterRequestMsg bean)
			throws JSONException {
		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("name", bean.getName());
		body.put("password", bean.getPassword());
		body.put("gender", bean.getGender());
		body.put("headerImageId", bean.getHeaderImageId());
		json.put(Constants.JSON.BODY, body);
		return json.toString();
	}

	private static String getJsonStrFromLoginRequest(LoginRequestMsg bean)
			throws JSONException {

		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("name", bean.getName());
		body.put("password", bean.getPassword());
		json.put(Constants.JSON.BODY, body);
		return json.toString();

	}

	private static String getJsonStrFromRefreshRequest(RefreshRequestMsg bean)
			throws JSONException {

		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);
		JSONObject body = new JSONObject();
		json.put(Constants.JSON.BODY, body);

		return json.toString();
	}

	private static String getJsonStrFromRefreshRoomRequest(
			RefreshRoomRequestMsg bean) throws JSONException {

		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);
		JSONObject body = new JSONObject();
		body.put("gid", bean.getGid());
		json.put(Constants.JSON.BODY, body);

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

	private static String getJsonStrFromStartRequest(StartRequestMsg bean)
			throws JSONException {
		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("gid", bean.getGid());
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

	private static String getJsonStrFromHelpRequest(HelpRequestMsg bean)
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

	private static String getJsonStrFromGetUserInfoRequest(
			GetUserInfoRequestMsg bean) throws JSONException {
		JSONObject json = new JSONObject();

		JSONObject header = new JSONObject();
		header.put(Constants.JSON_REQ_HEADER.TYPE, bean.getType());
		json.put(Constants.JSON.HEADER, header);

		JSONObject body = new JSONObject();
		body.put("uid", bean.getUid());
		body.put("gid", bean.getGid());
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

	private static String getJsonStrFromHeartbeatRequest(
			HeartbeatRequestMsg bean) throws JSONException {
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
		if (msg instanceof RegisterRequestMsg) {
			try {
				s = getJsonStrFromRegisterequest((RegisterRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof LoginRequestMsg) {
			try {
				s = getJsonStrFromLoginRequest((LoginRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof JoinRequestMsg) {
			try {
				s = getJsonStrFromJoinRequest((JoinRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof RefreshRequestMsg) {
			try {
				s = getJsonStrFromRefreshRequest((RefreshRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof RefreshRoomRequestMsg) {
			try {
				s = getJsonStrFromRefreshRoomRequest((RefreshRoomRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof StartRequestMsg) {
			try {
				s = getJsonStrFromStartRequest((StartRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof InputRequestMsg) {
			try {
				s = getJsonStrFromInputRequest((InputRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof HelpRequestMsg) {
			try {
				s = getJsonStrFromHelpRequest((HelpRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof TimeoutRequestMsg) {
			try {
				s = getJsonStrFromTimeOutRequest((TimeoutRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof GetUserInfoRequestMsg) {
			try {
				s = getJsonStrFromGetUserInfoRequest((GetUserInfoRequestMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof LogoutNotifyMsg) {
			try {
				s = getJsonStrFromLogoutNotify((LogoutNotifyMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof QuitNotifyMsg) {
			try {
				s = getJsonStrFromQuitNotify((QuitNotifyMsg) msg);
				s += "\r\n\r\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

			}
		} else if (msg instanceof HeartbeatRequestMsg) {
			try {
				s = getJsonStrFromHeartbeatRequest((HeartbeatRequestMsg) msg);
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
