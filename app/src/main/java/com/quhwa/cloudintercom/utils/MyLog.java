package com.quhwa.cloudintercom.utils;

import com.quhwa.cloudintercom.app.MyApplication;

import android.util.Log;
/**
 * 日志打印类
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class MyLog {
	/**字体红色*/
	public static final int PRINT_RED = 0;
	/**字体橙色*/
	public static final int PRINT_ORANGE = 1;
	/**字体绿色*/
	public static final int PRINT_GREEN = 2;
	/**字体黑色*/
	public static final int PRINT_BLACK = 3;
	/**字体蓝色*/
	public static final int PRINT_BLUE = 4;
	private String Tag = "MyLog";
	/**
	 * 打印
	 * @param Tag 标识
	 * @param msg 打印内容
	 * @param printType 输出字体颜色
	 */
	public static void print(String Tag, String msg, int printType) {
		if (MyApplication.getDebug()) {
			switch (printType) {
			case PRINT_RED:
				Log.e(Tag, msg);
				break;
			case PRINT_ORANGE:
				Log.w(Tag, msg);
				break;
			case PRINT_GREEN:
				Log.i(Tag, msg);
				break;
			case PRINT_BLACK:
				Log.v(Tag, msg);
				break;
			case PRINT_BLUE:
				Log.d(Tag, msg);
				break;
			default:// PRINT_RED
				Log.e(Tag, msg);
				break;
			}
		}
	}
}
