package com.quhwa.cloudintercom.view;

import com.quhwa.cloudintercom.bean.BoundResult;

import java.util.ArrayList;

public interface ISettingView {
	/**
	 * 显示修改开锁密码对话框
	 */
	void showModifyPswDialog(ArrayList<BoundResult.Device> deviceInfoList);
	/**
	 * 取消修改开锁密码对话框
	 */
	void dismissModifyPswDialog();
	/**
	 * 输入不为null
	 */
	void showToastInputIsNotNull();
	/**
	 * 设置密码不一致
	 */
	void showToastPasswordNotSame();
	/**
	 * 用户密码错误
	 */
	void showToastUserPasswordError();
	/**
	 * 显示正在修改对话框
	 */
	void showOnModifyingDialog();
	/**
	 * 请输入6位数字密码
	 */
	void showToastPleaseInputSixNumPassword();
	/**
	 * 清除缓存成功
	 */
	void showToastClearSuccess();
	/**
	 * 清除缓存失败
	 */
	void showToastClearFail();

	/**
	 * 显示防打扰菜单
	 */
	void showPopumenu(ArrayList<BoundResult.Device> devices);

	/**
	 * 提示没有设备数据
	 */
	void showToastNoDevice();

	/**
	 * 设置失败
	 */
	void showToastSetFail();
	/**
	 * 设置成功
	 */
	void showToastSetSuccess();
}
