package com.quhwa.cloudintercom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MyLog;

public class MyReceiver extends BroadcastReceiver {
	private String Tag = "NetStatusChangeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals("android.intent.action.USER_PRESENT") || action.equals("android.intent.action.BOOT_COMPLETED")){
			 context.startService(new Intent(context, PJSipService.class));
        	 MyLog.print(Tag , "收到解锁或开机广播", MyLog.PRINT_RED);
		}
		
	}

}
