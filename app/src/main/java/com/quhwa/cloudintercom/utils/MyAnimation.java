package com.quhwa.cloudintercom.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import com.quhwa.cloudintercom.R;

public class MyAnimation {
	private static RotateAnimation rotateAnimation;
	/**
	 * 从下到上动画
	 * @param context 上下文对象
	 * @param v 产生动画控件
	 */
	public static void fromDownToUpAnim(Context context,View v){
		v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.from_down_to_up));
	}
	/**
	 * 从上到下动画
	 * @param context 上下文对象
	 * @param v 产生动画控件
	 */
	public static void fromUpToDownAnim(Context context,View v){
		v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.from_up_to_down));
	}
	/**
	 * 从左到右动画
	 * @param context 上下文对象
	 * @param v 产生动画控件
	 */
	public static void fromLeftToRightAnim(Context context,View v){
		v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.from_left_to_right));
	}
	/**
	 * 从右到左动画
	 * @param context 上下文对象
	 * @param v 产生动画控件
	 */
	public static void fromRightToLeftAnim(Context context,View v){
		v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.from_right_to_left));
	}
	/**
	 * 缩放动画
	 * @param v 产生动画的控件
	 */
	public static void scaleAnimation(View v){
//		PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
		PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1f,0.5f,1f);
		PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1f,0.5f,1f);
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(v,pvh2,pvh3);
		objectAnimator.setDuration(500);
		objectAnimator.start();
	}
	public static void scaleAnimation1(View v){
//		PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
		PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleX", 0.5f,1f);
		PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("scaleY", 0.5f,1f);
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(v,pvh2,pvh3);
		objectAnimator.setDuration(1000);
		objectAnimator.start();
	}
	/**
	 * 透明动画
	 * @param v 产生动画的控件
	 */
	public static void alphaAnimation(View v){
		PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(v,pvh1);
		objectAnimator.setDuration(500);
		objectAnimator.start();
	}
	public static void rotationAnimation(View v){
		rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(5000);
		rotateAnimation.setRepeatCount(2);
		v.setAnimation(rotateAnimation);
	}
	public static void stopRotationAnimation(){
		if(rotateAnimation != null){
			rotateAnimation.cancel();
		}
	}
}








