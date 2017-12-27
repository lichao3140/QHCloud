package com.quhwa.cloudintercom.widget;

import android.view.View;

/**
 * AlertDialog 中确定按钮和取消按钮的监听器
 *
 * @author lxz
 * @date 2017年4月12日
 */
public interface AlertDialogChooseStatusListener {
	/**
	 * 选择确定按钮
	 */
	void choosePositiveButton();
	
	/**
	 * 选择取消按钮
	 */
	void chooseNegativeButton();
	
	/**
	 * 选择确定按钮
	 * @param userPassword 用户密码
	 * @param newPassword 新密码
	 * @param confirmPassword 确认密码
	 */
	void choosePositiveButton(String userPassword, String newPassword,String confirmPassword,String sipId,String doorList);
	
	/**
	 * 重命名/选择门口机选择确定按钮
	 * @param newName 输入新名称
	 */
	void choosePositiveButton(String newName);

	void layoutCallBack(View dialogView);

}
