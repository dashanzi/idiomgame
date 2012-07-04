package dashanzi.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import dashanzi.android.Constants;

/**
 * 数据库创建
 * 
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 * 
 */

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String tag = "DatabaseHelper";

	// 在SQLiteOepnHelper的子类当中，必须有该构造函数
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, Constants.DataBase.DB_VERSION);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	// 该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 建立数据库表
		String tab = " ";
		String server_config_table_create_str = "create table" + tab
				+ Constants.DataBase.SERVER_CONFIG_TABLE + "("
				+ Constants.DataBase.ID + tab + "varchar(20),"
				+ Constants.DataBase.VALUE_IP + tab + "varchar(20),"
				+ Constants.DataBase.VALUE_PORT + tab + "varchar(20)" + ");";

		Log.i(tag, "server_config_table_create_str = "
				+ server_config_table_create_str);

		db.execSQL(server_config_table_create_str);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(tag, "update a Database");
	}
}
