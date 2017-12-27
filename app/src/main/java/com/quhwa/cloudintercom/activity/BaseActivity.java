package com.quhwa.cloudintercom.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.quhwa.cloudintercom.R;

/**
 * Activity父类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class BaseActivity extends AppCompatActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//判断版本,如果[4.4,5.0]就设置状态栏和导航栏为透明
		if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT
				&&android.os.Build.VERSION.SDK_INT<=android.os.Build.VERSION_CODES.LOLLIPOP){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//设置虚拟导航栏为透明
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		/**
		 * 强制设置竖屏
		 */
		 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 }
	}

}
