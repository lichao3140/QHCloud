package com.quhwa.cloudintercom.utils;

import java.io.IOException;

import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.pjsip_status_code;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.widget.MyToast;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Button;

public class PhoneManager {
	private static MediaPlayer mediaPlayer;
	private static AudioManager audioManager= null;
	private static String Tag = "PhoneManager"; 

	/**
	 * 开始响铃
	 * @param context
	 */
	public static void startPlayRingtone(Context context) {
		
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		if(mediaPlayer == null){
			mediaPlayer = MediaPlayer.create(context, uri);
		}
		mediaPlayer.setLooping(true);
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.start();
	}
	
	/**
	 * 停止响铃
	 */
	public static void stopPlayRingtone() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	/**
	 * 接听电话
	 */
	public static void answer(){
		CallOpParam prm = new CallOpParam();
		prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
		try {
			PJSipService.currentCall.answer(prm);
		} catch (Exception e) {
		    System.out.println(e);
		}
	}
	/**
	 * 挂断
	 */
	public static void handup(){
		if (PJSipService.currentCall != null) {
		    CallOpParam prm = new CallOpParam();
		    prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
		    try {
		    	PJSipService.currentCall.hangup(prm);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	System.out.println(e);
		    }
		}
	}
	
//	public static void speaking(Context context){
//		if(mAudioManager == null){
//			mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE); 
//		}
//		mAudioManager.setSpeakerphoneOn(!mAudioManager.isSpeakerphoneOn());
//	}
//	public static void mute(Context context){
//		if(mAudioManager == null){
//			mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE); 
//		}
//		mAudioManager.setMicrophoneMute(!mAudioManager.isMicrophoneMute());
//		MyLog.print(Tag , "打开静音", MyLog.PRINT_RED);
//	}
	/**
	 * 设置麦克风
	 * @param close true：表示关闭，false：表示打开
	 * @param context 上下文对象
	 * @param button 麦克风按钮
	 */
    public static void setMicroPhone(boolean close,Context context,Button button) {
    	if(close){
    		MyLog.print(Tag , "禁止麦克风", MyLog.PRINT_RED);
    		if(button != null){
    			button.setBackgroundResource(R.drawable.ic_close_microphone);
    		}
    	}else{
    		MyLog.print(Tag , "打开麦克风", MyLog.PRINT_RED);
    		if(button != null){
    			button.setBackgroundResource(R.drawable.ic_open_microphone);
    		}
    	}
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(close);
    }
    
    public static boolean getMicroPhoneStatus(Context context){
    	AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//    	audioManager.
		return false;
    }
    
    /**
     * 设置扬声或者听筒模式
     * @param on true 扬声 false 听筒
     * @param context
     */
    @SuppressWarnings({ "deprecation", "unused" })
	public static void setSpeakerphoneOn(boolean on,Context context) {
		if(audioManager == null){
			audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE); 
	}
        if(on) {
                audioManager.setSpeakerphoneOn(true);
        } else {
                audioManager.setSpeakerphoneOn(false);//关闭扬声器
                audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
                //把声音设定成Earpiece（听筒）出来，设定为正在通话中
                audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
}
}









