package com.quhwa.cloudintercom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.service.JobHandleService;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.service.RemoteService;
import com.quhwa.cloudintercom.utils.MyLog;
/**
 * 闪屏页
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class SplashActivity extends BaseActivity {
	private String Tag = "SplashActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		MyLog.print(Tag, "启动PJSipService", MyLog.PRINT_RED);
		startService(new Intent(this,PJSipService.class));
		startService(new Intent(this,RemoteService.class));
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
//				Class<? extends Activity> classToStart;
//				if(true){
//					classToStart = MainActivity.class;
//				}else{
//					classToStart = LoginActivity.class;
//				}
				startActivity(new Intent(SplashActivity.this,MainActivity.class));
				SplashActivity.this.finish();
			}
		}, 2000);
		try {
			Class<?> cls = Class.forName("android.app.job.JobService");
			if(null != cls){
				Log.e(Tag, "类名"+cls.toString());
				startService(new Intent(this,JobHandleService.class));
			}
		} catch (ClassNotFoundException e) {
			Log.e(Tag, "找不到JobService类");
		}
	}
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

}
