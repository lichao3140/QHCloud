package com.quhwa.cloudintercom.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AdvertisementViewPager extends ViewPager {
	public AdvertisementViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	private float xDistance,yDistance,xLast,yLast,mLeft;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance=yDistance=0f;
			xLast=ev.getX();
			yLast=ev.getY();
			mLeft=ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX=ev.getX();
			final float curY=ev.getY();
			xDistance+=Math.abs(curX-xLast);
			yDistance+=Math.abs(curY-yLast);
			xLast=curX;
			yLast=curY;
			if(mLeft<100||xDistance<yDistance){
				getParent().requestDisallowInterceptTouchEvent(false);
			}else{
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;

		}
		return super.dispatchTouchEvent(ev);
	}
}
