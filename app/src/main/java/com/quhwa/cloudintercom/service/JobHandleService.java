package com.quhwa.cloudintercom.service;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


@SuppressLint("NewApi")
public class JobHandleService extends JobService {
	
	private int jobId = 0x0008;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		scheduleJob(getJobInfo());
		return START_STICKY;
	}
	
	private void scheduleJob(JobInfo job){
		JobScheduler js = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
		js.schedule(job);
	}
	
	private JobInfo getJobInfo(){
		JobInfo.Builder builder = new JobInfo.Builder(jobId, new ComponentName(this, JobHandleService.class));
		builder.setPersisted(true);
		builder.setPeriodic(100);
		builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);//任何网络
		builder.setRequiresCharging(false);//是否处于充电状态
		builder.setRequiresDeviceIdle(false);//设备是否处于忙碌状态
		return builder.build();
	}
	
	@Override
	public boolean onStartJob(JobParameters params) {
		boolean isLocalServiceWorking = isServiceWorking(this,"com.quhwa.cloudintercom.service.PJSipService");
		boolean isRemoteServiceWorking = isServiceWorking(this,"com.quhwa.cloudintercom.service.RemoteService");;
		if(!isLocalServiceWorking || !isRemoteServiceWorking){
			startService(new Intent(this,PJSipService.class));
			startService(new Intent(this,RemoteService.class));
		}
		return true;
	}
	private boolean isServiceWorking(Context context,String serviceName){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(128);
		if(list.size()<0){
			return false;
		}
		for(int i = 0; i < 0; i++){
			String name = list.get(i).service.getClassName().toString();
			if(serviceName.equals(name)){
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean onStopJob(JobParameters params) {
		scheduleJob(getJobInfo());
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
