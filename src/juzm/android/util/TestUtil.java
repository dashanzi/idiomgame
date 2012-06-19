package juzm.android.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TestUtil {
	
	public static void detectJSON() {
		
		String TAG = "test";
		Log.d(TAG, "start---------------");
		String str = "{" +

		"\"日期\" : \"2011-06-06\"," +

		// Like 是 JSONObject

				"\"Like\" : {" +

				"\"Name\" : \"加内特\"," +

				"\"Height\" : \"2.11cm\"," +

				"\"Age\" : 35" + "}," +

				// LikeList 就是一个 JSONObject

				"\"LikeList\":" +

				"{\"List\": " +

				"[" +

				// 这里也是JSONObject

				"{" +

				"\"Name\" : \"Rose\"," +

				"\"Height\" : \"190cm\"," +

				"\"Age\" : 23" +

				"}," +

				// 这里也是JSONObject

				"{" +

				"\"Name\" : \"科比\"," +

				"\"Height\" : \"198cm\"," +

				"\"Age\" : 33" +

				"}" +

				"]" +

				"}" +

				"}";

		try {

			JSONObject dataJson = new JSONObject(str);

			Log.d(TAG, dataJson.getString("日期"));

			JSONObject nbaJson = dataJson.getJSONObject("Like");

			Log.d("ddd", nbaJson.getString("Name"));

			Log.d("ddd", nbaJson.getString("Height"));

			Log.d("ddd", nbaJson.get("Age").toString());

			JSONObject listJson = dataJson.getJSONObject("LikeList");

			JSONArray arrayJson = listJson.getJSONArray("List");

			for (int i = 0; i < arrayJson.length(); i++) {

				JSONObject tempJson = arrayJson.optJSONObject(i);

				Log.d(TAG, tempJson.getString("Name"));

				Log.d(TAG, tempJson.getString("Height"));

				Log.d(TAG, tempJson.getString("Age").toString());

			}

		} catch (JSONException e) {

			System.out.println("Something wrong...");

			e.printStackTrace();

		}

	}

}
