package com.quhwa.cloudintercom.view;

import java.util.ArrayList;
import java.util.List;

import com.quhwa.cloudintercom.bean.BoundResult;

/**
 * 绑定设备view层接口
 *
 * @author lxz
 * @date 2017年4月15日
 */
public interface IBindDeviceView {
	/**
	 * 加载设备列表
	 * @param devices
	 */
	void loadDeviceList(ArrayList<BoundResult.Device> devices);
	
	/**
	 * 提示二维码解析失败
	 */
	void showToastDecodeFail();
	
	/**
	 * 提示设备已绑定
	 */
	void showToastDeviceIsBind();
	
	/**
	 * 提示设备绑定成功
	 */
	void showToastDeviceIsBindSuccess();
	
	/**
	 * 提示设备绑定失败
	 */
	void showToastDeviceIsBindFail();
	/**
	 * 提示设备绑定更新成功
	 */
	void showToastDeviceIsBindUpdateSuccess();
	
	/**
	 * 提示二维码并不是从设备获取
	 */
	void showToastQRCodeIsNotGetFromDevice();
	
	/**
	 * 提示先登陆方可使用此功能
	 */
	void showToastLoginFirst();
	
	/**
	 * 跳转到扫描页面
	 */
	void startToCaptureActivity();
	
	/**
	 * 跳转到登陆页面
	 */
	void startToLoginActivity();
	
	/**
	 * 显示删除菜单
	 */
	void showDeleteMenu();
	
	/**
	 * 隐藏删除菜单
	 */
	void dismissDeleteMenu();
	
	/**
	 * 提示选择删除选项
	 */
	void showToastChooseItemDelete();
	/**
	 * 显示正在绑定对话框
	 */
	void showBindingDialog();
	/**
	 * 显示正在解绑对话框
	 */
	void showUnBindingDialog();
	/**
	 * 提示解绑失败
	 */
	void showUnBindingFail();
	/**
	 * 重命名成功
	 */
	void showToastRenameSuccess();
	/**
	 * 重命名失败
	 */
	void showToastRenameFail();

	/**
	 * 提示先设置室内机房号
	 */
	void showToastSetIndoorRoomNum();

	/**
	 * 弹出修改别名对话框
	 */
	void showAlertDeviceAliasDailog();
	/**
	 * 关闭修改别名对话框
	 */
	void dismissAlertDeviceAliasDailog();

	/**
	 * 提示无网络
	 */
	void showToastNoNet();

	/**
	 * 提示服务器繁忙
	 */
	void showToastServerBusy();

	/**
	 * 停止刷新
	 */
	void stopRefresh();

	/**
	 * 提示没有设备
	 */
	void showToastNoDevice();

	/**
	 * 关闭设置设备对话框
	 */
	void dimissSetDeviceDialog();

	void finishRefresh();
}
