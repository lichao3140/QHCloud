package com.quhwa.cloudintercom.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.service.RemoteService;


public class ProgressUtil {
	@SuppressLint("NewApi")
	public static void startForeground(Service service,int type){
		Notification.Builder builder = new Notification.Builder(service);
		PendingIntent pi = null;
		if(type == 0){
			pi = PendingIntent.getActivity(service, 0, new Intent(service,RemoteService.class), 0);
		}else{
			pi = PendingIntent.getActivity(service, 0, new Intent(service,PJSipService.class), 0);
		}
		builder.setContentIntent(pi);
		Notification notification = builder.build();
		service.startForeground(0, notification);
		System.out.println("-----前台运行------");
	}
	public static void stopForeground(int type,Service service){
		if(type == 1){
			service.stopForeground(true);
			System.out.println("-----停止前台运行------");
		}
	}
}
