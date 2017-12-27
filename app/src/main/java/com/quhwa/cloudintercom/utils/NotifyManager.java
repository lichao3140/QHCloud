package com.quhwa.cloudintercom.utils;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.MainActivity;
import com.quhwa.cloudintercom.app.MyApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotifyManager {
	public static void showNotify(String title,String content){
		NotificationManager notificationManager = (NotificationManager) MyApplication.instance.getSystemService(MyApplication.instance.NOTIFICATION_SERVICE); 
		NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.instance); 
		builder.setContentTitle(title)//设置通知栏标题  
	    .setContentText(content)//设置通知栏显示内容 
//	    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图  
//	    .setNumber(3) //设置通知集合的数量  
	    .setTicker("侨华开门推送") //通知首次出现在通知栏，带上升动画效果的  
	    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
	    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级  
	    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消    
	    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
	    .setDefaults(Notification.DEFAULT_VIBRATE|NotificationCompat.DEFAULT_SOUND|NotificationCompat.DEFAULT_LIGHTS)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合  
	    //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission  
	    .setSmallIcon(R.drawable.logo);//设置通知小ICON  
		Notification notification = builder.build(); 
		int id = (int) (System.currentTimeMillis() / 1000);
		Intent intent = new Intent(MyApplication.instance, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pi = PendingIntent.getActivity(
           MyApplication.instance, id, intent,PendingIntent.FLAG_CANCEL_CURRENT  
        ); 
		notification.contentIntent = pi;
		notificationManager.notify(id, notification);  
	}
}
