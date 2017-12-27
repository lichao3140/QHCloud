package com.quhwa.cloudintercom.view;
/**
 * 用户信息设置view层接口
 *
 * @author lxz
 * @date 2017年4月17日
 */
public interface IUserInfoView {
	/**显示sip登陆状态*/
	void showSipRegisterStatus(boolean isRegistered);
	void deleteLocalData();
}
