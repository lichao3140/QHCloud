package com.quhwa.cloudintercom.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Fragment页面加载适配器
 *
 * @author lxz
 * @date 2017年3月17日
 */
public class MainPagerAdapter extends FragmentPagerAdapter{
	private List<Fragment> fragments;
	public MainPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
