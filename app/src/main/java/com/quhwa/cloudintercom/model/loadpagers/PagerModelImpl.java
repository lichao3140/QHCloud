package com.quhwa.cloudintercom.model.loadpagers;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.quhwa.cloudintercom.fragment.LifeCircleFragment;
import com.quhwa.cloudintercom.fragment.MyFragment;
import com.quhwa.cloudintercom.fragment.SmartAreaFragment;
import com.quhwa.cloudintercom.fragment.SmartAreaFragment_;
import com.quhwa.cloudintercom.fragment.SmartFamilyFragment;
/**
 * 加载Fragment页面数据
 *
 * @author lxz
 * @date 2017年3月17日
 */
public class PagerModelImpl implements IPagerModel{
	@Override
	public void loadPagersData(PagerOnLoadListener pagerOnLoadListener) {
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new SmartAreaFragment_());
		fragments.add(new LifeCircleFragment());
		fragments.add(new SmartFamilyFragment());
		fragments.add(new MyFragment());
		pagerOnLoadListener.onComplete(fragments);
	}

}
