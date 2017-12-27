package com.quhwa.cloudintercom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.db.Table;

/**
 * 偏好设置管理类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class MySharedPreferenceManager {
//	private Context context;
//	private SharedPreferences mPref;
	/**
	 * 保存value为字符串的方法
	 * @param context
	 * @param tabName
	 * @param key
	 * @param value 字符串
	 */
	public static void saveString(Context context,String tabName,String key,String value){
		SharedPreferences mPref = context.getSharedPreferences(tabName, context.MODE_PRIVATE);
		mPref.edit().putString(key, value).commit();
	}
	public static String queryString(Context context,String tabName,String key){
		SharedPreferences mPref = context.getSharedPreferences(tabName, context.MODE_PRIVATE);
		return mPref.getString(key, null);
	}
	/**
	 * 删除偏好表中key对应的value
	 * @param context
	 * @param tabName
	 * @param key
	 */
	public static void deleteData(Context context,String tabName,String key){
		SharedPreferences mPref = context.getSharedPreferences(tabName, context.MODE_PRIVATE);
		mPref.edit().remove(key);
	}
	
	/**
	 * 保存value为布尔值的方法
	 * @param context
	 * @param tabName
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context,String tabName,String key,boolean value){
		SharedPreferences mPref = context.getSharedPreferences(tabName, context.MODE_PRIVATE);
		mPref.edit().putBoolean(key, value).commit();
	}
	public static boolean queryBoolean(Context context,String tabName,String key){
		SharedPreferences mPref = context.getSharedPreferences(tabName, context.MODE_PRIVATE);
		return mPref.getBoolean(key, false);
	}

	public static void saveInt(Context context,String tabName,String key,int value){
		SharedPreferences mPref = context.getSharedPreferences(tabName, context.MODE_PRIVATE);
		mPref.edit().putInt(key, value).commit();
	}
	public static int queryInt(Context context,String tabName,String key){
		SharedPreferences mPref = context.getSharedPreferences(tabName, context.MODE_PRIVATE);
		return mPref.getInt(key, 0);
	}
	
	public static String getUsername(){
		return queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_NAME_KEY);
	}
}
