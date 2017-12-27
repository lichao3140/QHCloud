package com.quhwa.cloudintercom.view;

import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 拨号界面View层
 *
 * @author lxz
 * @date 2017年4月5日
 */
public interface IDialerView {
	/**
	 * 删除最后一个，隐藏删除按钮
	 */
	void hideImageDelete();
	/**
	 * 显示删除按钮
	 */
	void showImageDelete(ImageButton ib);
	/**
	 * 返回
	 * @param ib
	 */
	void back(ImageButton ib);
	/**
	 * 拨号
	 * @param etShowNum
	 * @param sbShowNum
	 */
	void call(EditText etShowNum,StringBuffer sbShowNum);
	
	/**
	 * 提示输入框为空
	 */
	void showToastInputIsNull();
	
	/**
	 * 提示先登陆方可使用此功能
	 */
	void showToastLoginFirst();
	
	/**
	 * 跳转到登陆界面
	 */
	void startToLoginActivity();
	
	
}
