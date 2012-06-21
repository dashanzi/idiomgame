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
import dashanzi.android.dto.notify.StartNotifyMsg;
import dashanzi.android.dto.response.JoinResponseMsg;
import dashanzi.android.dto.response.LoginResponseMsg;
import dashanzi.android.dto.response.RefreshResponseMsg;
import dashanzi.android.dto.response.TimeoutResponseMsg;

public class Json2BeansUtil {
	public static IMessage getMessageFromJsonStr(String jsonStr)
			throws JSONException {
		IMessage msg = null;
		String type = null;
		type = getType(jsonStr);
		if (type.equals(Constants.Type.JOIN_RESP)) {
			msg = getLoginResponseFromJsonStr(jsonStr);
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

		JSONObject body = dataJson.getJSONObject(Constants.JSON.BODY);

		result.setGid(body.getString("gid"));
		result.setUid(body.getString("uid"));

		// List<User>
		JSONArray listJson = body.getJSONArray("users");
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < listJson.length(); i++) {

			JSONObject userJson = listJson.optJSONObject(i);
			User user = new User();
			user.setUid(userJson.getString("uid"));
			user.setName(userJson.getString("name"));
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
	 * 获取StartNotifyMsg
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private static StartNotifyMsg getStartNotifyMsgFromJsonStr(String jsonStr)
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
			users.add(user);
		}
		result.setUsers(users);

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
		result.setType(dataJson.getString("type"));
		result.setStatus(dataJson.getString("status"));
		result.setGid(dataJson.getString("gid"));
		result.setWord(dataJson.getString("word"));
		result.setUid(dataJson.getString("uid"));
		result.setNextuid(dataJson.getString("nextuid"));

		return result;
	}
}
