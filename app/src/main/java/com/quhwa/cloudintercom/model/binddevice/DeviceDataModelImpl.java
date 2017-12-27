package com.quhwa.cloudintercom.model.binddevice;

import android.os.Handler;
import android.os.Message;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.ReturnResult;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.netmanager.OkhttpManager;
import com.quhwa.cloudintercom.netmanager.RequestParamsValues;
import com.quhwa.cloudintercom.utils.Constants;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DeviceDataModelImpl implements IDeviceDataModel {
	private int flag = -1;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG.BOUND_UPLOAD_DATA_SUCCESS:
					BoundResult result = (BoundResult) msg.obj;
					bindDeviceListener.onComplete(result);
					break;
				case MSG.SERVER_EXCEPTION:
					switch (flag){
						case 0://绑定失败
							bindDeviceListener.onCompleteFail();
							break;
						case 1://解绑失败
							unbindDeviceListener.onCompleteFail();
							break;
						case 2://获取列表失败
							queryDeviceListListener.onCompleteFail();
							break;
						case 3://修改名称失败
							alterDeviceAliasListener.onCompleteFail();
							break;
					}

					break;
				case MSG.NO_NET_MSG:
					switch (flag){
						case 0://绑定无网络
							bindDeviceListener.onNoNet();
							break;
						case 1://解绑无网络
							unbindDeviceListener.onNoNet();
							break;
						case 2://获取列表无网络
							queryDeviceListListener.onNoNet();
							break;
						case 3://重命名无网络
							alterDeviceAliasListener.onNoNet();
							break;
					}
					break;
				case MSG.QUERY_DEVICE_LIST_SUCCESS:
					ArrayList<BoundResult.Device> deviceInfoList = (ArrayList<BoundResult.Device>) msg.obj;
					queryDeviceListListener.onComplete(deviceInfoList);
					break;
				case MSG.UNBOUND_SUCCESS:
					ReturnResult result1 = (ReturnResult) msg.obj;
					unbindDeviceListener.onComplete(result1);
					break;
				case MSG.ALTER_DEVICE_ALIAS_SUCCESS:
					ReturnResult result2 = (ReturnResult) msg.obj;
					alterDeviceAliasListener.onComplete(result2);
					break;
			}
		};
	};
	private BindDeviceListener bindDeviceListener;
	private QueryDeviceListListener queryDeviceListListener;
	private UnbindDeviceListener unbindDeviceListener;
	private AlterDeviceAliasListener alterDeviceAliasListener;
	private String Tag = DeviceDataModelImpl.class.getSimpleName();

	@Override
	public void loadDeviceData(DeviceDataOnLoadListener deviceDataOnLoadListener) {
//		List<Device> devices = DBManager.queryAll(MyApplication.instance);
		String username = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_NAME_KEY);
		ArrayList<BoundResult.Device> devices = DBManager.queryByUsername(MyApplication.instance, Table.DEVICELIST_COLUMN_USERNAME, username);
		if(devices != null && devices.size()>0){
			deviceDataOnLoadListener.onComplete(devices);
		}

	}

	@Override
	public void bindDevice(BindDeviceListener bindDeviceListener,String deviceCode) {
		flag = 0;
		this.bindDeviceListener = bindDeviceListener;
		int userId = MySharedPreferenceManager.queryInt(MyApplication.instance,Table.TAB_USER,Table.TAB_USER_USERID_KEY);
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", userId+"");
		map.put("deviceCode", deviceCode);
		MyLog.print(Tag,"userId:"+userId+"\ndeviceCode:"+deviceCode,MyLog.PRINT_RED);
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
		//clsList.add(BoundResult.Device.class);
		clsList.add(BoundResult.class);
		okhttpManager.getData(Constants.BOUND_URL, addRequestParams,
				clsList, handler, MSG.BOUND_UPLOAD_DATA_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
	}

	@Override
	public void unbindDevice(UnbindDeviceListener unbindDeviceListener, String deviceCode) {
		flag = 1;
		this.unbindDeviceListener = unbindDeviceListener;
		int userId = MySharedPreferenceManager.queryInt(MyApplication.instance,Table.TAB_USER,Table.TAB_USER_USERID_KEY);
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", userId+"");
		map.put("deviceCode", deviceCode);
		MyLog.print(Tag,"userId:"+userId+"\ndeviceCode:"+deviceCode,MyLog.PRINT_RED);
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
		clsList.add(ReturnResult.class);
		okhttpManager.getData(Constants.UNBOUND_URL, addRequestParams,
				clsList, handler, MSG.UNBOUND_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
	}

	@Override
	public void queryDeviceList(QueryDeviceListListener queryDeviceListListener) {
		flag = 2;
		this.queryDeviceListListener = queryDeviceListListener;
		int userId = MySharedPreferenceManager.queryInt(MyApplication.instance,Table.TAB_USER,Table.TAB_USER_USERID_KEY);
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", userId+"");
		//map.put("doorDeviceStr", doorDeviceStr);
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
		clsList.add(BoundResult.Device.class);
		okhttpManager.getData(Constants.QUERY_DEVICE_LIST_URL, addRequestParams,
				clsList, handler, MSG.QUERY_DEVICE_LIST_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,1);
	}

	@Override
	public void alterDeviceAlias(AlterDeviceAliasListener alterDeviceAliasListener,String newDeviceAlias,int deviceId) {
		flag = 3;
		this.alterDeviceAliasListener = alterDeviceAliasListener;
		int userId = MySharedPreferenceManager.queryInt(MyApplication.instance,Table.TAB_USER,Table.TAB_USER_USERID_KEY);
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("deviceId", deviceId+"");
		map.put("userId", userId+"");
		map.put("deviceAlias", newDeviceAlias);
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
		clsList.add(ReturnResult.class);
		okhttpManager.getData(Constants.ALTER_DEVICE_ALIAS_URL, addRequestParams,
				clsList, handler, MSG.ALTER_DEVICE_ALIAS_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
	}


}
