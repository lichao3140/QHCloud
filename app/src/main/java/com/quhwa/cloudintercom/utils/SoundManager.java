package com.quhwa.cloudintercom.utils;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.quhwa.cloudintercom.activity.MainActivity;

public class SoundManager {
	private static MediaPlayer mMediaPlayer;
	private static AudioManager mAudioManager;
	/**
	 * 播放assets下音频文件
	 * @param context 上下文对象
	 * @param key 音频名字对应的key
	 */
	public static void play(Context context, String key) {
		setSoundLound(context);
		String soundName = PropertiesUtils.getValue(key);
		AssetFileDescriptor fileDescriptor;
        try {  
            fileDescriptor = context.getAssets().openFd(soundName); 
            if(mMediaPlayer == null){
            	mMediaPlayer = new MediaPlayer();  
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),  
            fileDescriptor.getStartOffset(),  
            fileDescriptor.getLength());  
            mMediaPlayer.prepare();  
            mMediaPlayer.start();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
        mMediaPlayer = null;
	}
	/**
	 * 把播放音频声音设置最大
	 * @param context 上下文对象
	 */
	public static void setSoundLound(Context context){
		if(mAudioManager == null){
			mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);  
		}
        int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量  
        int maxVolume = mAudioManager  
        		.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音  
        if(mVolume<maxVolume){
        	System.out.println("声音未达到最大");
        	mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小 
        }
	}
	
}
