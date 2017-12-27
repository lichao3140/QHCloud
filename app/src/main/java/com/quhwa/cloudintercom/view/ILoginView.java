package com.quhwa.cloudintercom.view;

import java.util.List;

import com.quhwa.cloudintercom.bean.UserInfo;
/**
 * 登陆界面接口
 *
 * @author lxz
 * @date 2017年3月23日
 */
public interface ILoginView {
	/**
	 * 提示登陆成功
	 * @param userInfo
	 */
	void showToastLoginSuccess(UserInfo userInfo);
	
	/**
	 * 提示登陆失败
	 */
	void showToastLoginFail();
	
	/**
	 * 登陆加载对话框
	 */
	void loadDialog();
	
	/**
	 * 对话框销毁
	 */
	void dismissDialog();
	
	/**
	 * 提示输入框为空
	 */
	void showToastInputIsNull();
	
	/**
	 * 提示登陆密码错误
	 */
	void showToastPasswordError();
	
	/**
	 * 提示用户名不存在
	 */
	void showToastUsernameIsNotExist();
	
	/**
	 * 保存数据
	 * @param userInfo
	 */
	void saveData(UserInfo userInfo);
	
	/**
	 * 更新数据
	 * @param userInfo
	 */
	void updateData(UserInfo userInfo);
	
	/**
	 * 恢复控件显示状态(即记住用户名和密码)
	 */
	void recoverWidgetShowStatus(UserInfo userInfos);
	
}
