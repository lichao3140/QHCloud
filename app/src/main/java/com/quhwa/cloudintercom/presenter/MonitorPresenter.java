package com.quhwa.cloudintercom.presenter;


import android.content.Context;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.model.binddevice.DeviceDataModelImpl;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel.DeviceDataOnLoadListener;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.Constants;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IMonitorView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

import java.util.ArrayList;

public class MonitorPresenter {
	private IMonitorView iMonitorView;
	private Context context;

	public MonitorPresenter(Context context,IMonitorView iMonitorView) {
		super();
		this.iMonitorView = iMonitorView;
		this.context = context;
	}
	private IDeviceDataModel deviceDataModelImpl = new DeviceDataModelImpl();
	private String Tag = "MonitorPresenter";
	/**
	 * 加载房间列表
	 */
	public void loadLocalRoomNumList(){
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {//登陆状态
			String username = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_NAME_KEY);
			if (DBManager.getQueryByWhere(MyApplication.instance, "username",
					username)) {
				if(iMonitorView != null && deviceDataModelImpl != null){
					deviceDataModelImpl.loadDeviceData(new DeviceDataOnLoadListener() {
						@Override
						public void onComplete(ArrayList<BoundResult.Device> devices) {
							iMonitorView.loadRoomNumList(devices);
						}
					});
				}
			}
		}
	}

	public void loadRoomNumList(){
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {//登陆状态
			if(iMonitorView != null && deviceDataModelImpl !=null){
				MyDialog.showDialog(context, R.string.loading);
				deviceDataModelImpl.queryDeviceList(new IDeviceDataModel.QueryDeviceListListener() {
					@Override
					public void onComplete(ArrayList<BoundResult.Device> deviceInfoList) {
						if(deviceInfoList != null && deviceInfoList.size()>0){
							iMonitorView.loadRoomNumList(deviceInfoList);
							iMonitorView.finishRefresh();
							MyDialog.dismissDialog();
						}else{
							iMonitorView.showToastNoRoom();
							iMonitorView.finishRefreshFail();
							MyDialog.dismissDialog();
						}
					}

					@Override
					public void onCompleteFail() {
						iMonitorView.showToastServerBusy();
						//loadLocalRoomNumList();
						iMonitorView.finishRefreshFail();
						MyDialog.dismissDialog();
					}

					@Override
					public void onNoNet() {
						iMonitorView.showToastNoNet();
						//loadLocalRoomNumList();
						iMonitorView.finishRefreshFail();
						MyDialog.dismissDialog();
					}
				});
			}
		}else{
			iMonitorView.finishRefreshFail();
			MyToast.showToast(MyApplication.instance, R.string.please_login_first);
		}
	}


	/**
	 * 开始监控
	 */
	public void startMonitor(String sip){
		if(iMonitorView != null){
			iMonitorView.dimissListView();
			iMonitorView.showSurfaceView();
			iMonitorView.showConnectingDialog();
			String sipId = "sip:"+sip+"@"+Constants.SERVER_IP;
			MyLog.print(Tag , "sipId:"+sipId, MyLog.PRINT_BLACK);
			if(PJSipService.instance != null){
				PJSipService.instance.makeCall(sipId,0);
			}
		}
	}
}
