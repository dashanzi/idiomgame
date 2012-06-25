package dashanzi.android.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dashanzi.android.Constants;
import dashanzi.android.dto.GroupInfo;
import dashanzi.android.dto.IMessage;
import dashanzi.android.dto.User;
import dashanzi.android.dto.notify.QuitNotifyMsg;
import dashanzi.android.dto.notify.RoomNotifyMsg;
import dashanzi.android.dto.notify.StartNotifyMsg;
import dashanzi.android.dto.response.GetUserInfoResponseMsg;
import dashanzi.android.dto.response.HelpResponseMsg;
import dashanzi.android.dto.response.InputResponseMsg;
import dashanzi.android.dto.response.JoinResponseMsg;
import dashanzi.android.dto.response.LoginResponseMsg;
import dashanzi.android.dto.response.RefreshResponseMsg;
import dashanzi.android.dto.response.RefreshRoomResponseMsg;
import dashanzi.android.dto.response.RegisterResponseMsg;
import dashanzi.android.dto.response.TimeoutResponseMsg;

public class Json2BeansUtil {
	public static IMessage getMessageFromJsonStr(String jsonStr)
			throws JSONException {
		IMessage msg = null;
		String type = null;
		type = getType(jsonStr);
		if (type.equals(Constants.Type.REGISTER_RESP)) {
			msg = getRegisterResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.LOGIN_RESP)) {
			msg = getLoginResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.REFRESH_RESP)) {
			msg = getRefreshResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.REFRESHROOM_RESP)) {
			msg = getRefreshRoomResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.JOIN_RESP)) {
			msg = getJoinResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.INPUT_RESP)) {
			msg = getInputResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.HELP_RESP)) {
			msg = getHelpResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.TIMEOUT_RESP)) {
			msg = getTimeoutResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.ROOM_NOTIFY)) {
			msg = getRoomNotifyFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.START_NOTIFY)) {
			msg = getStartNotifyFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.GETUSERINFO_RESP)) {
			msg = getGetUserInfoResponseFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.QUIT_NOTIFY)) {
			msg = getQuitNotifyFromJsonStr(jsonStr);
		} else if (type.equals(Constants.Type.LOGOUT_NOTIFY)) {

		}
		return msg;
	}

	private static String getType(String jsonStr) throws JSONException {
		if (jsonStr == null) {
			return null;
		}
		JSONObject dataJson = new JSONObject(jsonStr);
		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		return header.getString("type");
	}

	private static RegisterResponseMsg getRegisterResponseFromJsonStr(
			String jsonStr) throws JSONException {
		if (jsonStr == null) {
			return null;
		}
		RegisterResponseMsg result = new RegisterResponseMsg();

		JSONObject dataJson = new JSONObject(jsonStr);

		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		return result;
	}

	/**
	 * 获取LoginResponseMsg
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private static LoginResponseMsg getLoginResponseFromJsonStr(String jsonStr)
			throws JSONException {
		if (jsonStr == null) {
			return null;
		}
		LoginResponseMsg result = new LoginResponseMsg();

		JSONObject dataJson = new JSONObject(jsonStr);

		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		// List<GroupInfo>
		JSONArray groups = body.getJSONArray("groups");
		List<GroupInfo> groupList = new ArrayList<GroupInfo>();

		for (int i = 0; i < groups.length(); i++) {

			JSONObject groupJson = groups.optJSONObject(i);
			GroupInfo group = new GroupInfo();
			group.setGid(groupJson.getString("gid"));
			group.setState(groupJson.getString("state"));
			groupList.add(group);
		}
		result.setGroupInfoList(groupList);
		return result;
	}

	private static JoinResponseMsg getJoinResponseFromJsonStr(String jsonStr)
			throws JSONException {

		if (jsonStr == null) {
			return null;
		}

		JoinResponseMsg result = new JoinResponseMsg();

		JSONObject dataJson = new JSONObject(jsonStr);
		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		result.setGid(body.getString("gid"));
		result.setUid(body.getString("uid"));
		result.setHelpNum(body.getString("helpNum"));

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
			user.setScore(userJson.getString("score"));
			users.add(user);
		}
		result.setUsers(users);

		return result;
	}

	private static InputResponseMsg getInputResponseFromJsonStr(String jsonStr)
			throws JSONException {
		if (jsonStr == null) {
			return null;
		}

		InputResponseMsg result = new InputResponseMsg();

		JSONObject dataJson = new JSONObject(jsonStr);
		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		result.setGid(body.getString("gid"));
		result.setWord(body.getString("word"));
		result.setAnswer(body.getString("answer"));
		result.setUid(body.getString("uid"));
		result.setNextUid(body.getString("nextuid"));

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
			user.setScore(userJson.getString("score"));
			users.add(user);
		}
		result.setUsers(users);

		return result;
	}

	private static HelpResponseMsg getHelpResponseFromJsonStr(String jsonStr)
			throws JSONException {
		if (jsonStr == null) {
			return null;
		}

		HelpResponseMsg result = new HelpResponseMsg();

		JSONObject dataJson = new JSONObject(jsonStr);
		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		result.setGid(body.getString("gid"));
		result.setWord(body.getString("word"));
		result.setUid(body.getString("uid"));
		result.setHelpNum(body.getString("helpNum"));
		result.setNextUid(body.getString("nextuid"));

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
			user.setScore(userJson.getString("score"));
			users.add(user);
		}
		result.setUsers(users);

		return result;
	}

	private static RoomNotifyMsg getRoomNotifyFromJsonStr(String jsonStr)
			throws JSONException {
		if (jsonStr == null) {
			return null;
		}

		RoomNotifyMsg result = new RoomNotifyMsg();

		JSONObject dataJson = new JSONObject(jsonStr);
		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
			user.setScore(userJson.getString("score"));
			users.add(user);
		}
		result.setUsers(users);

		return result;
	}

	/**
	 * 获取RefreshResponseMsg
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private static RefreshResponseMsg getRefreshResponseFromJsonStr(
			String jsonStr) throws JSONException {
		if (jsonStr == null) {
			return null;
		}
		RefreshResponseMsg result = new RefreshResponseMsg();

		JSONObject dataJson = new JSONObject(jsonStr);

		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		// List<GroupInfo>
		JSONArray groups = body.getJSONArray("groups");
		List<GroupInfo> groupList = new ArrayList<GroupInfo>();

		for (int i = 0; i < groups.length(); i++) {

			JSONObject groupJson = groups.optJSONObject(i);
			GroupInfo group = new GroupInfo();
			group.setGid(groupJson.getString("gid"));
			group.setState(groupJson.getString("state"));
			groupList.add(group);
		}
		result.setGroupInfoList(groupList);
		return result;
	}

	/**
	 * 获取RefreshRoomResponseMsg
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private static RefreshRoomResponseMsg getRefreshRoomResponseFromJsonStr(
			String jsonStr) throws JSONException {
		if (jsonStr == null) {
			return null;
		}

		RefreshRoomResponseMsg result = new RefreshRoomResponseMsg();

		JSONObject dataJson = new JSONObject(jsonStr);
		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
			user.setScore(userJson.getString("score"));
			users.add(user);
		}
		result.setUsers(users);

		return result;
	}

	/**
	 * 获取StartNotifyMsg
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private static StartNotifyMsg getStartNotifyFromJsonStr(String jsonStr)
			throws JSONException {
		if (jsonStr == null) {
			return null;
		}
		StartNotifyMsg result = new StartNotifyMsg();

		JSONObject dataJson = new JSONObject(jsonStr);

		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);
		result.setGid(body.getString("gid"));
		result.setFirstuid(body.getString("firstuid"));
		result.setWord(body.getString("firstword"));

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
			user.setScore(userJson.getString("score"));
			users.add(user);
		}
		result.setUsers(users);

		return result;
	}

	private static GetUserInfoResponseMsg getGetUserInfoResponseFromJsonStr(
			String jsonStr) throws JSONException {
		if (jsonStr == null) {
			return null;
		}

		GetUserInfoResponseMsg result = new GetUserInfoResponseMsg();
		JSONObject dataJson = new JSONObject(jsonStr);

		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		result.setName(body.getString("name"));
		result.setGender(body.getString("gender"));
		result.setScore(body.getString("score"));
		result.setLevel(body.getString("level"));

		return result;
	}

	/**
	 * 获取TimeoutResponseMsg
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private static TimeoutResponseMsg getTimeoutResponseFromJsonStr(
			String jsonStr) throws JSONException {
		if (jsonStr == null) {
			return null;
		}

		TimeoutResponseMsg result = new TimeoutResponseMsg();
		JSONObject dataJson = new JSONObject(jsonStr);

		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));
		result.setStatus(header.getString("status"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		result.setGid(body.getString("gid"));
		result.setWord(body.getString("word"));
		result.setUid(body.getString("uid"));
		result.setNextuid(body.getString("nextuid"));

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
			user.setScore(userJson.getString("score"));
			users.add(user);
		}
		result.setUsers(users);

		return result;
	}

	private static QuitNotifyMsg getQuitNotifyFromJsonStr(String jsonStr)
			throws JSONException {
		if (jsonStr == null) {
			return null;
		}
		QuitNotifyMsg result = new QuitNotifyMsg();

		JSONObject dataJson = new JSONObject(jsonStr);

		JSONObject header = dataJson.getJSONObject(Constants.JSON.HEADER);
		result.setType(header.getString("type"));

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);
		result.setGid(body.getString("gid"));
		result.setUid(body.getString("uid"));

		return result;
	}
}
