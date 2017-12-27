package com.quhwa.cloudintercom.view;

import java.util.List;

import com.quhwa.cloudintercom.bean.Advertisement;

public interface ISmartAreaView {
	/***
	 * 加载轮播图
	 * @param advertisements
	 */
	void loadPicture(List<Advertisement> advertisements);
}
