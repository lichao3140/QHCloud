package com.quhwa.cloudintercom.view;

import java.util.List;

import android.support.v4.app.Fragment;
/**
 * Fragment页面接口
 *
 * @author lxz
 * @date 2017年3月17日
 */
public interface IPagerView {
	/**
	 * 加载各页面
	 * @param fragments
	 */
	void load(List<Fragment> fragments);
}
