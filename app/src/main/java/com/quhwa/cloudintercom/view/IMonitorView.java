package com.quhwa.cloudintercom.view;

import java.util.ArrayList;
import java.util.List;

import com.quhwa.cloudintercom.bean.BoundResult;

public interface IMonitorView {
	/**
	 * 加载设备列表
	 * @param devices
	 */
	void loadRoomNumList(ArrayList<BoundResult.Device> devices);
	/**
	 * 隐藏设备列表
	 */
	void dimissListView();
	/**
	 * 显示SurfaceView
	 */
	void showSurfaceView();
	/**
	 * 显示设备列表
	 */
	void showListView();
	/**
	 * 隐藏SurfaceView
	 */
	void dimissSurfaceView();
	/**
	 * 显示连接对话框
	 */
	void showConnectingDialog();
	/**
	 * 消除连接对话框
	 */
	void dimissConnectingDialog();

	/**
	 * 暂无房间号
	 */
	void showToastNoRoom();

	/**
	 * 服务器繁忙
	 */
	void showToastServerBusy();

	/**
	 * 没有网络
	 */
	void showToastNoNet();

	void finishRefresh();

	void finishRefreshFail();
}
