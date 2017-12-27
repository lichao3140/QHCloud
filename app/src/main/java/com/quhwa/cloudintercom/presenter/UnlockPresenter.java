package com.quhwa.cloudintercom.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.model.binddevice.DeviceDataModelImpl;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel.DeviceDataOnLoadListener;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IUnlockView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

public class UnlockPresenter {
	private IUnlockView iUnlockView;
	IDeviceDataModel deviceDataModelImpl = new DeviceDataModelImpl();
	private Context context;

	public UnlockPresenter(IUnlockView iUnlockView,Context context) {
		super();
		this.iUnlockView = iUnlockView;
		this.context = context;
	}
	
	/**
	 * 加载本地设备列表
	 */
	public void loadLocalDeviceList(){
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {// 登陆状态
			String username = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_NAME_KEY);
			if (DBManager.getQueryByWhere(MyApplication.instance, "username",
					username)) {
				if (deviceDataModelImpl != null && iUnlockView != null) {
					deviceDataModelImpl
							.loadDeviceData(new DeviceDataOnLoadListener() {
								@Override
								public void onComplete(ArrayList<Device> devices) {
									iUnlockView.loadDeviceList(devices);// 加载设备列表
								}
							});
				}
			}
		}
	}

	/**
	 * 加载网络设备列表
	 */
	public void loadDeviceList(){
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {// 登陆状态
			if(iUnlockView != null && deviceDataModelImpl != null){
				MyDialog.showDialog(context, R.string.loading);
				deviceDataModelImpl.queryDeviceList(new IDeviceDataModel.QueryDeviceListListener() {
					@Override
					public void onComplete(ArrayList<Device> deviceInfoList) {
						if(deviceInfoList != null && deviceInfoList.size()>0){
							iUnlockView.loadDeviceList(deviceInfoList);// 加载设备列表
							MyDialog.dismissDialog();
						}else{
							MyDialog.dismissDialog();
							MyToast.showToast(context,R.string.no_device_bind);
							iUnlockView.finishRefresh();
						}
					}

					@Override
					public void onCompleteFail() {
						iUnlockView.showToastServerBusy();
						iUnlockView.finishRefresh();
						//loadLocalDeviceList();
						MyDialog.dismissDialog();
					}

					@Override
					public void onNoNet() {
						iUnlockView.showToastNoNet();
						iUnlockView.finishRefresh();
						//loadLocalDeviceList();
						MyDialog.dismissDialog();
					}
				});
			}
		}else{
			iUnlockView.finishRefresh();
			MyToast.showToast(context,R.string.please_login_first);
		}
	}
}
