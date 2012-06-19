package dashanzi.android.util;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dashanzi.android.dto.notify.StartNotifyMsg;
import dashanzi.android.dto.notify.User;
import dashanzi.android.dto.response.LoginResponseMsg;
import dashanzi.android.dto.response.TimeoutResponseMsg;

public class Json2BeansUtil {

	/**
	 * 获取LoginResponseMsg
	 * @param jsonStr 
	 * @return
	 * @throws JSONException 
	 */
	public static LoginResponseMsg getLoginResponseFromJsonStr(String jsonStr) throws JSONException {

		if (jsonStr == null) {
			return null;
		}
		
		LoginResponseMsg result = new LoginResponseMsg();
		
		JSONObject dataJson = new JSONObject(jsonStr);
		result.setType(dataJson.getString("type"));
		result.setStatus(dataJson.getString("status"));
		result.setGid(dataJson.getString("gid"));
		result.setUid(dataJson.getString("uid"));
		
		return result;
	}

	/**
	 * 获取StartNotifyMsg
	 * @param jsonStr
	 * @return
	 * @throws JSONException 
	 */
	public static StartNotifyMsg getStartNotifyMsgFromJsonStr(String jsonStr) throws JSONException {
		if (jsonStr == null) {
			return null;
		}
		StartNotifyMsg result = new StartNotifyMsg();
		
		JSONObject dataJson = new JSONObject(jsonStr);
		result.setType(dataJson.getString("type"));
		result.setGid(dataJson.getString("gid"));
		result.setFirstuid(dataJson.getString("firstuid"));
		result.setFirstword(dataJson.getString("firstword"));
		
		//List<User>
		JSONArray listJson = dataJson.getJSONArray("users");
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
	 * @param jsonStr
	 * @return
	 * @throws JSONException 
	 */
	public static TimeoutResponseMsg getTimeoutResponseFromJsonStr(String jsonStr) throws JSONException{
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
