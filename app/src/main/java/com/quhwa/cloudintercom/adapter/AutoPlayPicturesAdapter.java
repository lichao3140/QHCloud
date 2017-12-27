package com.quhwa.cloudintercom.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.Advertisement;

public class AutoPlayPicturesAdapter extends PagerAdapter{
	private List<Advertisement> advertisements;
	public AutoPlayPicturesAdapter(List<Advertisement> advertisements) {
		super();
		this.advertisements = advertisements;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(MyApplication.instance, R.layout.adapter_ad, null);
		ImageView imageShow = (ImageView) view.findViewById(R.id.iv_show);
		Advertisement ad = advertisements.get(position % advertisements.size());
		imageShow.setImageResource(ad.getIcon());
		container.addView(view);//一定不能少，将view加入到viewPager中
		return view;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
