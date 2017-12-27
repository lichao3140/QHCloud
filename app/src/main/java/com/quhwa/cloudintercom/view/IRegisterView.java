package com.quhwa.cloudintercom.view;

import com.quhwa.cloudintercom.bean.UserInfo;
/**
 * 注册界面接口
 *
 * @author lxz
 * @date 2017年3月23日
 */
public interface IRegisterView {
	/**
	 * 提示注册成功
	 * @param userInfo
	 */
	void showToastResiterSuccess(UserInfo userInfo);
	/**
	 * 提示注册失败
	 */
	void showToastResiterFail();
	/**
	 * 提示注册用户名已存在
	 */
	void showToastResiterUsernameExsit();
	/**
	 * 注册加载对话框
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
	 * 输入密码不一致
	 */
	void showToastPasswordInconsistency();
	/**
	 * 输入正确的手机号码
	 */
	void showToastInputRightMobileNum();
}
