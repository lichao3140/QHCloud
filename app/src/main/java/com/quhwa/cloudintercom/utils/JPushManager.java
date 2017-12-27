package com.quhwa.cloudintercom.utils;

import android.content.Context;
import cn.jpush.android.api.JPushInterface;

public class JPushManager {
	private static String Tag = "JPushManager";

	/**
	 * 极光推送初始化
	 * @param context 上下文对象
	 */
	public static void initJPush(Context context){
		MyLog.print(Tag ,"极光推送初始化", MyLog.PRINT_RED);
		JPushInterface.setDebugMode(true);
        JPushInterface.init(context);
	}
	/**
	 * 极光推送注册Id即token
	 * @param context 上下文对象
	 * @return token
	 */
	public static String getRegisterId(Context context){
		String registerId = JPushInterface.getRegistrationID(context);
		MyLog.print(Tag ,"registerId:"+registerId, MyLog.PRINT_RED);
		return registerId;
	}
}
