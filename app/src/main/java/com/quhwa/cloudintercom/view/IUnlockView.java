package com.quhwa.cloudintercom.view;

import java.util.List;

import com.quhwa.cloudintercom.bean.BoundResult.Device;

public interface IUnlockView {
	/**
	 * 加载设备列表
	 * @param devices
	 */
	void loadDeviceList(List<Device> devices);
	/**
	 * 显示开锁中对话框
	 */
	void showUnlockingDialog();

	void showToastServerBusy();

	void showToastNoNet();

	void finishRefresh();
}
