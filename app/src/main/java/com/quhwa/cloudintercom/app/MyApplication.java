package com.quhwa.cloudintercom.app;

import android.app.Application;

import com.mob.MobSDK;
import com.quhwa.cloudintercom.utils.JPushManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * 
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class MyApplication extends Application {
	public static MyApplication instance;
	public static boolean debugMode = false;
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		setDebug(true);
		ZXingLibrary.initDisplayOpinion(this);
		JPushManager.initJPush(instance);
		//捕获异常
//		Thread.setDefaultUncaughtExceptionHandler(new CrashUncaughtExceptionHandler(this, Thread.getDefaultUncaughtExceptionHandler()));
		MobSDK.init(getApplicationContext());
	}
	/**
	 * 设置debug状态
	 * @param debugMode
	 */
	public void setDebug(boolean debugMode){
		this.debugMode = debugMode;
	}
	/**
	 * 返回debug状态
	 * @return true：处于调试状态，日志打开   false：处于未调试状态，日志未打开
	 */
	public static boolean getDebug(){
		return debugMode;
	}
}
