package com.quhwa.cloudintercom.widget;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.quhwa.cloudintercom.R;
/**
 * 我的提示类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class MyToast {
	private static TextView mTextView;

	// private static ImageView mImageView;

	public static void showToast(Context context, int message) {
		// 加载Toast布局
		View toastRoot = LayoutInflater.from(context).inflate(R.layout.mytoast,
				null);
		// 初始化布局控件
		mTextView = (TextView) toastRoot.findViewById(R.id.tv_message);
		// mImageView = (ImageView) toastRoot.findViewById(R.id.imageView);
		// 为控件设置属性
		mTextView.setText(message);
		// mImageView.setImageResource(R.drawable.toast);
		// Toast的初始化
		Toast toastStart = new Toast(context);
		// 获取屏幕高度
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		// Toast的Y坐标是屏幕高度的4/5，不会出现不适配的问题
		toastStart.setGravity(Gravity.TOP, 0, height * 2/3);
		toastStart.setDuration(Toast.LENGTH_SHORT);
		toastStart.setView(toastRoot);
		toastStart.show();
	}
//	public static void showToastStr(Context context, String message) {
//		// 加载Toast布局
//		View toastRoot = LayoutInflater.from(context).inflate(R.layout.mytoast,
//				null);
//		// 初始化布局控件
//		mTextView = (TextView) toastRoot.findViewById(R.id.tv_message);
//		// mImageView = (ImageView) toastRoot.findViewById(R.id.imageView);
//		// 为控件设置属性
//		mTextView.setText(message);
//		// mImageView.setImageResource(R.drawable.toast);
//		// Toast的初始化
//		Toast toastStart = new Toast(context);
//		// 获取屏幕高度
//		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		int height = wm.getDefaultDisplay().getHeight();
//		// Toast的Y坐标是屏幕高度的4/5，不会出现不适配的问题
//		toastStart.setGravity(Gravity.TOP, 0, height * 4/ 5);
//		toastStart.setDuration(Toast.LENGTH_SHORT);
//		toastStart.setView(toastRoot);
//		toastStart.show();
//	}
}
