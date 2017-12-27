package com.quhwa.cloudintercom.model.binddevice;

import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.DeviceInfo;
import com.quhwa.cloudintercom.bean.ReturnResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理设备数据Model层接口 
 *
 * @author lxz
 * @date 2017年4月11日
 */
public interface IDeviceDataModel {
	/**
	 * 加载设备数据
	 * @param deviceDataOnLoadListener
	 */
	void loadDeviceData(DeviceDataOnLoadListener deviceDataOnLoadListener);
	/**
	 * 
	 * 设备数据加载监听器
	 * @author lxz
	 * @date 2017年4月11日
	 */
	interface DeviceDataOnLoadListener{
		void onComplete(ArrayList<BoundResult.Device> devices);
	}

	/**
	 * 绑定设备
	 * @param bindDeviceListener
     */
	void bindDevice(BindDeviceListener bindDeviceListener,String deviceCode);

	/**
	 * 绑定设备成功监听器
	 */
	interface BindDeviceListener{
		void onComplete(BoundResult result);
		void onCompleteFail();
		void onNoNet();
	}

	/**
	 * 解绑设备
	 * @param
	 */
	void unbindDevice(UnbindDeviceListener unbindDeviceListener, String deviceCode);

	/**
	 * 解绑设备成功监听器
	 */
	interface UnbindDeviceListener{
		void onComplete(ReturnResult result);
		void onCompleteFail();
		void onNoNet();
	}


	/**
	 * 查询设备列表
	 * @param
     */
	void queryDeviceList(QueryDeviceListListener queryDeviceListListener);

	/**
	 * 查询设备列表监听器
	 */
	interface QueryDeviceListListener{
		void onComplete(ArrayList<BoundResult.Device> deviceInfoList);
		void onCompleteFail();
		void onNoNet();
	}
	/**
	 * 修改设备别名
	 * @param
     */
	void alterDeviceAlias(AlterDeviceAliasListener alterDeviceAliasListener,String newDeviceAlias,int deviceId);

	/**
	 *修改设备别名监听器
	 */
	interface AlterDeviceAliasListener{
		void onComplete(ReturnResult result);
		void onCompleteFail();
		void onNoNet();
	}



}
