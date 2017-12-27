package com.quhwa.cloudintercom.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.quhwa.cloudintercom.utils.ProgressUtil;

import cloudintercom.quhwa.com.qhcloud.IServiceAidl;

public class RemoteService extends Service {
	private RemoteServiceConnection remoteServiceCon;
	private RemoteServiceBinder remoteServiceBinder;
	@Override
	public IBinder onBind(Intent intent) {
		return remoteServiceBinder;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(remoteServiceBinder != null){
			remoteServiceBinder = new RemoteServiceBinder();
		}
		remoteServiceCon = new RemoteServiceConnection();
	}
	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ProgressUtil.startForeground(this, 1);
		return START_STICKY;
	}
	
	class RemoteServiceConnection implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			startService(new Intent(getApplicationContext(),PJSipService.class));
			bindService(new Intent(getApplicationContext(),PJSipService.class), remoteServiceCon, Context.BIND_IMPORTANT);
		}
		
	}
	
	class RemoteServiceBinder extends IServiceAidl.Stub{

		@Override
		public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent("action.start.pjsipService");
		sendBroadcast(intent);
	}

}
