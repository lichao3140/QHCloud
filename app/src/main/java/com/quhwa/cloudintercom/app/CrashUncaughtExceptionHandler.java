package com.quhwa.cloudintercom.app;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

import com.quhwa.cloudintercom.activity.MainActivity;
import com.quhwa.cloudintercom.activity.SplashActivity;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.SoundManager;



/**
 * Create By lxz on 2016/7/24 异常处理类，将异常信息上传到服务器
 */
public class CrashUncaughtExceptionHandler implements UncaughtExceptionHandler {
		
	//private static final String TAG = "UncaughtExceptionHandler";
	private Context mContext;
	private UncaughtExceptionHandler mDefaultHandler;
	private String Tag = "CrashUncaughtExceptionHandler";

	public CrashUncaughtExceptionHandler(Context context,UncaughtExceptionHandler defaultHandler) {
		this.mDefaultHandler = defaultHandler;
		this.mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		MyLog.print(Tag, "------------捕获到异常信息开始-------------", MyLog.PRINT_RED);
		String exceptionInformation = getExceptionInformation(thread, ex);
		MyLog.print(Tag, exceptionInformation, MyLog.PRINT_RED);
		MyLog.print(Tag, "------------捕获到异常信息结束-------------", MyLog.PRINT_RED);
//		SoundManager.play(mContext, "program_occur_bug_audio_name");
//		new UploadExceptionDao().getUploadExceptionData(1, 1, ex.getCause().toString(), exceptionInformation);
		if (ex == null && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
//			new Thread() {
//				public void run() {
//					Looper.prepare();
//					// 主线程中自动有looper.looper负责从queue取消息
//					// 工作线程没有looper,需要prepare和loop方法
//					// .show service.enqueueToast
//					Toast.makeText(mContext, "网络不稳定，程序即将重启", Toast.LENGTH_LONG).show();//TODO
//					Looper.loop();
//				};
//			}.start();

//			try {
//				Thread.currentThread().sleep(5000);
//			} catch (Exception e) {
//			}
			//重启程序
			restartApp();
		}
	}
	/**
	 * 重启APP
	 */
	private void restartApp() {
		Intent intent = new Intent(mContext, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	/**
	 * 获取异常信息
	 * 
	 * @param thread
	 * @param ex
	 * @return
	 */
	private String getExceptionInformation(Thread thread, Throwable ex) {
		long current = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder().append('\n');
		sb.append("THREAD: ").append(thread).append('\n');
		sb.append("BOARD: ").append(Build.BOARD).append('\n');
		sb.append("BOOTLOADER: ").append(Build.BOOTLOADER).append('\n');
		sb.append("BRAND: ").append(Build.BRAND).append('\n');
		sb.append("CPU_ABI: ").append(Build.CPU_ABI).append('\n');
		sb.append("CPU_ABI2: ").append(Build.CPU_ABI2).append('\n');
		sb.append("DEVICE: ").append(Build.DEVICE).append('\n');
		sb.append("DISPLAY: ").append(Build.DISPLAY).append('\n');
		sb.append("FINGERPRINT: ").append(Build.FINGERPRINT).append('\n');
		sb.append("HARDWARE: ").append(Build.HARDWARE).append('\n');
		sb.append("HOST: ").append(Build.HOST).append('\n');
		sb.append("ID: ").append(Build.ID).append('\n');
		sb.append("MANUFACTURER: ").append(Build.MANUFACTURER).append('\n');
		sb.append("MODEL: ").append(Build.MODEL).append('\n');
		sb.append("PRODUCT: ").append(Build.PRODUCT).append('\n');
		sb.append("SERIAL: ").append(Build.SERIAL).append('\n');
		sb.append("TAGS: ").append(Build.TAGS).append('\n');
		sb.append("TIME: ").append(Build.TIME).append(' ')
				.append(toDateString(Build.TIME)).append('\n');
		sb.append("TYPE: ").append(Build.TYPE).append('\n');
		sb.append("USER: ").append(Build.USER).append('\n');
		sb.append("VERSION.CODENAME: ").append(Build.VERSION.CODENAME)
				.append('\n');
		sb.append("VERSION.INCREMENTAL: ").append(Build.VERSION.INCREMENTAL)
				.append('\n');
		sb.append("VERSION.RELEASE: ").append(Build.VERSION.RELEASE)
				.append('\n');
		sb.append("VERSION.SDK_INT: ").append(Build.VERSION.SDK_INT)
				.append('\n');
		sb.append("LANG: ")
				.append(mContext.getResources().getConfiguration().locale
						.getLanguage()).append('\n');
//		sb.append("APP.VERSION.NAME: ").append(VersionUpdateUtils.getVersionName(mContext)).append('\n');
//		sb.append("APP.VERSION.CODE: ").append(VersionUpdateUtils.getVersionCode(mContext)).append('\n');
		sb.append("CURRENT: ").append(current).append(' ')
				.append(toDateString(current)).append('\n');

		sb.append(getErrorInformation(ex));
		return sb.toString();
	}


	/**
	 * 获取错误信息
	 * 
	 * @param t
	 * @return
	 */
	private String getErrorInformation(Throwable t) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter writter = new PrintWriter(baos);
		t.printStackTrace(writter);
		writter.flush();
		String result = new String(baos.toByteArray());
		writter.close();
		return result;
	}

	private String toDateString(long timeMilli) {
		Calendar calc = Calendar.getInstance();
		calc.setTimeInMillis(timeMilli);
		return String.format(Locale.CHINESE,
				"%04d.%02d.%02d %02d:%02d:%02d:%03d", calc.get(Calendar.YEAR),
				calc.get(Calendar.MONTH) + 1, calc.get(Calendar.DAY_OF_MONTH),
				calc.get(Calendar.HOUR_OF_DAY), calc.get(Calendar.MINUTE),
				calc.get(Calendar.SECOND), calc.get(Calendar.MILLISECOND));
	}

}
