package com.quhwa.cloudintercom.utils;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;

public class PowerAndScreenManager {
	/**
	 * 点亮屏幕
	 * @param context
	 */
	public static void lightScreen(Context context){
		//获取电源管理器对象  
		PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);  
		//获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag  
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");  
        //点亮屏幕  
        wl.acquire();  
   
//        //得到键盘锁管理器对象  
//        KeyguardManager  km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);  
//        //参数是LogCat里用的Tag  
//        KeyguardLock kl = km.newKeyguardLock("unLock");    
//        //解锁  
//        kl.disableKeyguard();   
   
        /* 
         * 这里写程序的其他代码 
         *  
         * */  
   
        //重新启用自动加锁  
//        kl.reenableKeyguard();  
//        //释放  
        wl.release();  
	}
}
