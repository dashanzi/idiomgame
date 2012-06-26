package dashanzi.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import dashanzi.android.Constants;
import dashanzi.android.dto.ServerInfo;

public class DBUtil {

	private static final String tag = "DBUtil";

	private static DatabaseHelper dbHelper;
	
	public static void initDB(Context context) {
		
		dbHelper = new DatabaseHelper(context,
				Constants.DataBase.DB_NAME);
		
		// 生成ContentValues对象
		ContentValues values = new ContentValues();
		values.put(Constants.DataBase.ID, Constants.DataBase.DEFAULT_ID);
		values.put(Constants.DataBase.VALUE_IP, Constants.DataBase.DEFAULT_IP);
		values.put(Constants.DataBase.VALUE_PORT,
				Constants.DataBase.DEFAULT_PORT);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// 调用insert方法，就可以将数据插入到数据库当中
		db.insert(Constants.DataBase.SERVER_CONFIG_TABLE, null, values);
		//close
		db.close();
	}

	public static ServerInfo getServerInfo(Context context) {
		
		dbHelper = new DatabaseHelper(context,
				Constants.DataBase.DB_NAME);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		String[] col_query = { Constants.DataBase.ID,
				Constants.DataBase.VALUE_IP, Constants.DataBase.VALUE_PORT };
		
		Cursor cursor = db.query(Constants.DataBase.SERVER_CONFIG_TABLE, col_query, null, null, null,
				null, null);
		
		ServerInfo dto = new ServerInfo();
		while (cursor.moveToNext()) {
			String ip = cursor.getString(cursor
					.getColumnIndex(Constants.DataBase.VALUE_IP));
			int port = cursor.getInt(cursor
					.getColumnIndex(Constants.DataBase.VALUE_PORT));
			dto.setIp(ip);
			dto.setPort(port);
			break;
		}
		//close
		cursor.close();
		db.close();
		return dto;
	}

	public static boolean setServerInfo(Context context, ServerInfo dto) {
		// 生成ContentValues对象
		ContentValues values = new ContentValues();
		values.put(Constants.DataBase.ID, Constants.DataBase.DEFAULT_ID);
		values.put(Constants.DataBase.VALUE_IP, dto.getIp());
		values.put(Constants.DataBase.VALUE_PORT, dto.getPort());

		DatabaseHelper dbHelper = new DatabaseHelper(context,
				Constants.DataBase.DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// update data
		long result = db.update(Constants.DataBase.SERVER_CONFIG_TABLE, values,
				"id=?", new String[] { Constants.DataBase.DEFAULT_ID });
		//close
		db.close();

		Log.d(tag, " insert return :" + result + "");
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void closeDBHelper() {
	    if (dbHelper != null) {
	    	dbHelper.close();
	    }
	}
}
