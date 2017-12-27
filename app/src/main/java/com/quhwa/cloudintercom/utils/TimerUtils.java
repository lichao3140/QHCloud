package com.quhwa.cloudintercom.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.quhwa.cloudintercom.widget.MyDialog;

public class TimerUtils {
	private static TimerTask startTask;
	/**
	 * 发送sip消息定时器
	 * @param sipId sip账号
	 * @param handler 消息处理
	 * @param msg_type 消息类型
	 * @param intevalPeriod 定时执行时间
	 * @param isTimingSend 是否定时发送
	 */
	public static void sendSipMsgTimer(final String sipId,final Handler handler,final int msg_type,long intevalPeriod,boolean isTimingSend){
		startTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = msg_type;
				msg.obj = sipId;
				handler.sendMessage(msg);
			}
		};
		Timer timer = new Timer();
		long delay = 0;
		timer.scheduleAtFixedRate(startTask, delay, intevalPeriod);
		if(isTimingSend){
			return;
		}else{
			stopSendSipMsgTimer(2000);
		}
//		timer.schedule(task, delay);
	}
	/**
	 * 停止发送
	 */
	public static void stopToSend(){
		if(startTask != null){
			startTask.cancel();
		}
	}

	private static TimerTask stopTask;
	/**
	 * 延迟设置的时间执行停止发送sipmsg
	 * @param delay 延迟的时间
	 */
	public static void stopSendSipMsgTimer(long delay){
		stopTask = new TimerTask() {
			@Override
			public void run() {
				stopToSend();
			}
		};
		Timer timer = new Timer();
		timer.schedule(stopTask, delay);
	}
	/**
	 * 时间格式化
	 * @param time 当前时间毫秒
	 * @param pattern 转换的格式
	 * @return 转换后的日期字符串
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeLongToDate(long time,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(time));
	}

	private static TimerTask dialogTimer;

	/**
	 * 延迟时间关闭加载对话框
	 * @param delayTime
     */
	public static void closeDialogTimer(long delayTime,final Handler handler){
		dialogTimer = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(100);
			}
		};
		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(dialogTimer,delayTime,0);
		timer.scheduleAtFixedRate(dialogTimer,0,10000);
	}

	/**
	 * 停止关闭加载对话框任务
	 */
	public static void stopDialogTimer(){
		if(dialogTimer != null){
			dialogTimer.cancel();
		}
	}



}





