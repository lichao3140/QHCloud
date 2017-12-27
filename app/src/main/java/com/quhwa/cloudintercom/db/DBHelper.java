package com.quhwa.cloudintercom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.quhwa.cloudintercom.utils.MyLog;

/**
 * 数据库类
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class DBHelper extends SQLiteOpenHelper {
	/**数据库名称*/
	private final static String DATABASE_NAME = "quhwa_cloud";
	private static int DATABASE_VERSION = 2;
	private static DBHelper dbHelper;
	private String Tag = "DBHelper";
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public static DBHelper getInstance(Context context){
		if(dbHelper == null){
			dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Table.CREATE_DEVICE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MyLog.print(Tag , "数据库旧版本:"+oldVersion+","+"数据库新版本:"+newVersion, MyLog.PRINT_RED);
		if(newVersion == DATABASE_VERSION){
			//db version:2
			db.execSQL(Table.ADD_SHIELD_STATUS_COLUMN);
			//db version:3
			db.execSQL(Table.ADD_DEVICE_NAME_COLUMN);
			//db version:4
			db.execSQL(Table.ADD_DOOR_DEVICE_STR_COLUMN);
		}
	}
}
