package com.quhwa.cloudintercom.view;

import com.quhwa.cloudintercom.bean.BoundResult;

import java.util.List;

public interface IVisitorPwdView {
	/**随机密码生成中*/
	void randomCreating();
	/**显示随机密码*/
	void showVisitorPwdSuccess(String visitorPwd);
	/**获取随机密码失败*/
	void showVisitorPwdFail();
	/**显示上次访客密码*/
	void showLastTimeVisitorPassword(String lastVisitorPwd);

	/**
	 * 加载设备列表
	 * @param devices
     */
	void loadDeviceList(List<BoundResult.Device> devices);

	void showToastServerIsBusy();

	void showToastNoNet();

	void finishRefreshFail();

	void showViewIfLogin();
}
