package com.quhwa.cloudintercom.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;
/**
 * 处理viewPager滑动速度类
 *
 * @author lxz
 * @date 2017年3月17日
 */
public class ViewPagerScroller extends Scroller{
//	private String Tag = "ViewPagerScroller";
	private int mDuration = 0;
	public ViewPagerScroller(Context context) {
		super(context);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator,
			boolean flywheel) {
		super(context, interpolator, flywheel);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}
	
	@Override  
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		if (duration == 200 || duration == 300 || duration == 400) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		} else {
			super.startScroll(startX, startY, dx, dy, 300);
		}
    }  
  
//    @Override  
//    public void startScroll(int startX, int startY, int dx, int dy) {  
//    	super.startScroll(startX, startY, dx, dy, 200);  
//    } 
    public void initViewPagerScroll(ViewPager viewPager) {
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			mScroller.set(viewPager, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
