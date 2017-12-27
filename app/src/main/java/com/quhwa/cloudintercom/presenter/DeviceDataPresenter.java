package com.quhwa.cloudintercom.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.DeviceListAdapter;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.ReturnResult;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.model.binddevice.DeviceDataModelImpl;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel.DeviceDataOnLoadListener;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.Code;
import com.quhwa.cloudintercom.utils.CommonUtil;
import com.quhwa.cloudintercom.utils.Encrypt;
import com.quhwa.cloudintercom.utils.ISipMessageReturnListener;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.SipMsgManager;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.utils.SoundManager;
import com.quhwa.cloudintercom.utils.TimerUtils;
import com.quhwa.cloudintercom.view.IBindDeviceView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.pjsip.pjsua2.app.MSG_TYPE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
/**
 * 处理设备数据逻辑类
 *
 * @author lxz
 * @date 2017年4月15日
 */
public class DeviceDataPresenter implements ISipMessageReturnListener{
	private static final String Tag = "DeviceDataPresenter";
	private IBindDeviceView iBindDeviceView;
	private Intent data;
	IDeviceDataModel deviceDataModelImpl = new DeviceDataModelImpl();
	private static String deviceCode;

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	private BoundResult.Device device1;

	public void setDevice1(BoundResult.Device device) {
		this.device1 = device;
	}
	public BoundResult.Device getDevice1() {
		return device1;
	}

	private static Handler handler;
	public DeviceDataPresenter(IBindDeviceView iBindDeviceView, Intent data) {
		super();
		this.iBindDeviceView = iBindDeviceView;
		this.data = data;
	}
	
	
	public DeviceDataPresenter(IBindDeviceView iBindDeviceView) {
		super();
		this.iBindDeviceView = iBindDeviceView;
	}
	/**
	 * 二维码扫描
	 */
	public void scanQRCode(){
		if(iBindDeviceView != null){
			if(MySharedPreferenceManager.queryBoolean(MyApplication.instance, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)){
				//登陆方可扫描
				iBindDeviceView.startToCaptureActivity();
			}else{
				//未登陆不可扫描
				iBindDeviceView.showToastLoginFirst();
				iBindDeviceView.startToLoginActivity();
			}
		}
	}
	
	/**
	 * 处理二维码结果
	 */
	public void fetchQRCodeResult(Context context,Handler handler){
		if(iBindDeviceView != null){
			if (null != data) {
				Bundle bundle = data.getExtras();
				if (bundle == null) {
					return;
				}
				if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
					String result = bundle.getString(CodeUtils.RESULT_STRING);
					try {
						result = new String(Encrypt.deBase(result.getBytes(Encrypt.CHARSET)), Encrypt.CHARSET);
					} catch (UnsupportedEncodingException e) {
						MyLog.print(Tag, "解密失败", MyLog.PRINT_RED);
						e.printStackTrace();
					}
					if(result.contains("QUHWA")){
						//处理二维码扫描结果
						String sipId = result.substring(result.lastIndexOf("_")+1, result.length());
						String str = result.substring(result.indexOf("_")+1,result.length());
						String deviceCode = str.substring(0,str.indexOf("_"));
						setDeviceCode(deviceCode);
						MyLog.print(Tag,"-----deviceCode-----"+deviceCode,MyLog.PRINT_RED);
						iBindDeviceView.showBindingDialog();
						sendSipIdToIndoor(sipId,handler);
					}else{
						MyLog.print(Tag, "请从室内机上扫描二维码", MyLog.PRINT_RED);
						iBindDeviceView.showToastQRCodeIsNotGetFromDevice();
						SoundManager.play(context, "scan_from_device_audio_name");
					}
				} else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
					iBindDeviceView.showToastDecodeFail();//二维码解析失败
				}
			}
			
		}
	}

	/**
	 * 绑定
	 * @param deviceCode
     */
	private void boundToServer(String deviceCode,final Handler handler) {
		deviceDataModelImpl.bindDevice(new IDeviceDataModel.BindDeviceListener() {
            @Override
            public void onComplete(BoundResult result) {
                if(result.getCode() == Code.RETURN_SUCCESS){//绑定成功
					MyLog.print(Tag,"---BoundResult---"+result,MyLog.PRINT_RED);
						BoundResult.Device device = result.getDevice();
						if(device != null){
							String username = MySharedPreferenceManager.getUsername();
							device.setUsername(username);
							device.setShieldStatus(1);
							device1 = device;
							queryDeviceList();
							insertData(MyApplication.instance,device);
							iBindDeviceView.showToastDeviceIsBindSuccess();//设备绑定成功
							MyDialog.dismissDialog();
							SoundManager.play(MyApplication.instance, "bind_device_success_audio_name");
						}
//					}
                }else if(result.getCode() == Code.RETURN_ALREADY_EXIST){//设备已绑定
					//再发送sipId给室内机，如果调用绑定接口成功，发送sipId失败，重新绑定时，只需把sipId发给室内机即可
					if(PJSipService.account != null){
						BoundResult.Device device = result.getDevice();
						if(device != null){
							iBindDeviceView.showToastDeviceIsBind();
							SoundManager.play(MyApplication.instance, "device_is_exsist_audio_name");
							MyDialog.dismissDialog();
						}
					}
					MyLog.print(Tag,"---BoundResult---"+result,MyLog.PRINT_RED);
                }else if(result.getCode() == Code.RETURN_NOT_EXIST){//设备不存在绑定失败
					MyLog.print(Tag,"设备不存在，绑定失败",MyLog.PRINT_RED);
					toastBindFail();
				}else {//绑定失败
					MyLog.print(Tag,"绑定设备，服务器返回code为"+result.getCode(),MyLog.PRINT_RED);
					toastBindFail();
				}
            }

            @Override
            public void onCompleteFail() {//服务器异常绑定失败
				MyLog.print(Tag,"服务器异常绑定失败",MyLog.PRINT_RED);
				toastBindFail();
            }

            @Override
            public void onNoNet() {//无网络绑定失败
				MyLog.print(Tag,"无网络绑定失败",MyLog.PRINT_RED);
				toastBindFail();
            }
        },deviceCode);
	}

	private void toastBindFail(){
		if(iBindDeviceView != null){
			MyDialog.dismissDialog();
			iBindDeviceView.showToastDeviceIsBindFail();
			SoundManager.play(MyApplication.instance, "bind_divice_fail_audio_name");
		}
	}

	/**
	 * 数据库操作
	 * @param context
	 * @param device
     */
	private void insertData(Context context,BoundResult.Device device) {
		if(device.getRoomNo() != null && device.getUnitDoorNo() != null){
			String username = MySharedPreferenceManager.getUsername();
			if(DBManager.getQueryByWhere(context, "username", username)) {//该用户在数据库已存在
				if(DBManager.getQueryByWhere(context, Table.DEVICELIST_COLUMN_ID, device.getDeviceId()+"")) {//该设备已存在在数据库
					MyLog.print(Tag,"同一个用户除了存在设备，没有其他设备",MyLog.PRINT_RED);
				}else{//该设备不存在在数据库
					DBManager.insertDeviceList(MyApplication.instance, device);
				}
			}else{//该用户没有在数据库
				DBManager.insertDeviceList(MyApplication.instance, device);
			}
		}else{
			MyLog.print(Tag,"房号和门口机号为null",MyLog.PRINT_RED);
			//请先设置室内机房号再绑定
			iBindDeviceView.showToastSetIndoorRoomNum();
			MyDialog.dismissDialog();
		}
	}


	/**
	 * 给室内机发送本机sipId
	 * @param sipId
	 */
	private void sendSipIdToIndoor(final String sipId,final Handler handler) {
		if(PJSipService.account != null){
			this.handler = handler;
			TimerUtils.sendSipMsgTimer(sipId, handler, MSG.MSG_SEND_SIPID_AND_TOKEN_TO_INDOOR,5000,false);
		}
	}

	private static List<BoundResult.Device> devices;
	public void setDevices(List<BoundResult.Device> devices){
		this.devices = devices;
	}
	public List<BoundResult.Device> getDevices(){
		return devices;
	}
	/**
	 * 加载本地设备列表
	 */
	public void loadDeviceList(){
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {// 登陆状态
			String username = MySharedPreferenceManager.getUsername();
			if (DBManager.getQueryByWhere(MyApplication.instance, "username",
					username)) {//查询当前用户是否有有绑定过设备，有的话，数据库有数据
				if (deviceDataModelImpl != null && iBindDeviceView != null) {
					deviceDataModelImpl
							.loadDeviceData(new DeviceDataOnLoadListener() {
								@Override
								public void onComplete(ArrayList<BoundResult.Device> devices) {
									if(devices != null && devices.size()>0){
										setDevices(devices);
										iBindDeviceView.loadDeviceList(devices);// 加载设备列表
									}else {
										iBindDeviceView.stopRefresh();

									}
								}
							});
				}
			}
		}
	}

	private void loadLocalDevicelist(){
		if (deviceDataModelImpl != null && iBindDeviceView != null) {
			deviceDataModelImpl
					.loadDeviceData(new DeviceDataOnLoadListener() {
						@Override
						public void onComplete(ArrayList<BoundResult.Device> devices) {
							if(devices != null && devices.size()>0){
								setDevices(devices);
								iBindDeviceView.loadDeviceList(devices);// 加载设备列表
							}else {
								iBindDeviceView.stopRefresh();

							}
						}
					});
		}
	}

	/**
	 * 查询设备列表
	 */
	public void queryDeviceList() {
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {// 登陆状态
			if(iBindDeviceView != null && deviceDataModelImpl != null){
				deviceDataModelImpl.queryDeviceList(new IDeviceDataModel.QueryDeviceListListener() {
					@Override
					public void onComplete(ArrayList<BoundResult.Device> deviceInfoList) {
						MyLog.print(Tag,"-----deviceInfoList-----"+deviceInfoList,MyLog.PRINT_RED);
							if(deviceInfoList != null && deviceInfoList.size()>0){
								setDevices(deviceInfoList);
								iBindDeviceView.loadDeviceList(deviceInfoList);// 加载设备列表
							}else{
								iBindDeviceView.stopRefresh();
								iBindDeviceView.showToastNoDevice();
								iBindDeviceView.loadDeviceList(deviceInfoList);
							}
					}

					@Override
					public void onCompleteFail() {
						showToastServerBusy();
						//loadLocalDevicelist();
						//iBindDeviceView.stopRefresh();
						iBindDeviceView.finishRefresh();
					}

					@Override
					public void onNoNet() {
						showToastNoNet();
						//loadLocalDevicelist();
						//iBindDeviceView.stopRefresh();
						iBindDeviceView.finishRefresh();
					}
				});
			}
		}else{
			iBindDeviceView.finishRefresh();
			MyToast.showToast(MyApplication.instance, R.string.please_login_first);
		}
	}

	private int position = 0;

	/**
	 * 删除设备
	 * @param adapter
	 */
	public void deleteDevie(DeviceListAdapter adapter,BoundResult.Device device) {
		if(iBindDeviceView != null){
			iBindDeviceView.showUnBindingDialog();
			if (adapter != null) {
				//解绑
				unbindToServer(device);
			}
		}
	}

	/**
	 * 向服务器解绑设备
	 */
	private void unbindToServer(final BoundResult.Device device) {
		if(deviceDataModelImpl != null){
			deviceDataModelImpl.unbindDevice(new IDeviceDataModel.UnbindDeviceListener() {
				@Override
				public void onComplete(ReturnResult result) {
					MyLog.print(Tag,"解绑返回数据："+result,MyLog.PRINT_RED);
					if(result.getCode() == Code.RETURN_SUCCESS){
//						deleteData(device);
						String username = MySharedPreferenceManager.getUsername();
						ArrayList<BoundResult.Device> devicesAfterDelete = DBManager.deleteDevicByRoomNumAndUsername(MyApplication.instance, Table.DEVICELIST_COLUMN_ROOMNUM, getDevices().get(position).getRoomNo(), Table.DEVICELIST_COLUMN_USERNAME, username);
						//iBindDeviceView.loadDeviceList(devicesAfterDelete);
						queryDeviceList();
						MyDialog.dismissDialog();
						SoundManager.play(MyApplication.instance, "ubind_device_success_audio_name");
						//iBindDeviceView.showBindingDialog();
						iBindDeviceView.dimissSetDeviceDialog();
						MyDialog.dismissDialog();
					}
				}

				@Override
				public void onCompleteFail() {//服务器异常解绑设备
					showToastServerBusy();
				}

				@Override
				public void onNoNet() {//无网络解绑设备
					showToastNoNet();
				}
			},device.getDeviceCode());
		}
	}

	private void showToastNoNet() {
		iBindDeviceView.dismissAlertDeviceAliasDailog();
		iBindDeviceView.showToastNoNet();
	}

	@Override
	public void sipMessageReturnFail() {
		if(iBindDeviceView != null) {
			switch (PJSipService.currentMsgType) {
				case MSG_TYPE.MSG_BIND:
					iBindDeviceView.showToastDeviceIsBindFail();
					MyDialog.dismissDialog();
					SoundManager.play(MyApplication.instance, "bind_divice_fail_audio_name");
					break;
				case MSG_TYPE.MSG_SEND_UNBIND_DEVICE:
					iBindDeviceView.showUnBindingFail();
					MyDialog.dismissDialog();
					SoundManager.play(MyApplication.instance, "ubind_divice_fail_audio_name");
					break;
			}
		}
	}


	public void deleteData(BoundResult.Device device) {
		if(device != null){
			setDevice1(device);
			PJSipService.currentMsgType = MSG_TYPE.MSG_SEND_UNBIND_DEVICE;//把当前消息设为解绑消息（即向室内机发送sipId消息）
			String mySipId = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID);
			String token = CommonUtil.getJpushToken();
			String sipId = device.getSipid();

			if(sipId != null){
				String message = "sipId="+mySipId+","+"token="+token+","+"mobileType="+ SipMsgType.MOBILETYPE;
				MyLog.print(Tag, "message:"+message, MyLog.PRINT_RED);
				SipMsgManager.sendInstantMessage(sipId, SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.RECEIVE_UNBIND_DEVICE_TYPE, message, "")), PJSipService.account);
				MyLog.print(Tag, "把sipId和token发给室内机", MyLog.PRINT_RED);
			}else{
				MyLog.print(Tag, "获取极光推送registerId失败token为null或sipId为null", MyLog.PRINT_RED);
				MyDialog.dismissDialog();
				iBindDeviceView.showUnBindingFail();
			}
		}
	}


	@Override
	public void sipMessageReturnSuccess() {
		if(iBindDeviceView != null){
			ArrayList<BoundResult.Device> devicesAfterDelete;
			switch (PJSipService.currentMsgType) {
			case MSG_TYPE.MSG_BIND:
				if(getDeviceCode() != null && handler != null){
					boundToServer(getDeviceCode(),handler);
				}
				break;
			case MSG_TYPE.MSG_SEND_UNBIND_DEVICE:
				unbindToServer(getDevice1());
				break;
			default:
				break;
			}
		}
	}


	/**
	 * 把新名称上传到服务器修改
	 * @param newName
	 * @param device
     */
	public void renameServerData(final String newName, final BoundResult.Device device) {
		if(deviceDataModelImpl != null){
			setDevice1(device);
				iBindDeviceView.showAlertDeviceAliasDailog();
				deviceDataModelImpl.alterDeviceAlias(new IDeviceDataModel.AlterDeviceAliasListener() {
					@Override
					public void onComplete(ReturnResult result) {
						if(result.getCode() == Code.RETURN_SUCCESS){
							DBManager.updateDeviceNameByUserAndRoomNum(MyApplication.instance, newName, getDevice1());
							iBindDeviceView.showToastRenameSuccess();
							iBindDeviceView.dismissAlertDeviceAliasDailog();
							MyDialog.dimissSetDeviceDialog();
							//loadLocalDevicelist();
							queryDeviceList();
							//queryDeviceList();
						}else{//修改失败
							showToastAlertFail();
						}
					}

					@Override
					public void onCompleteFail() {
						showToastServerBusy();
					}

					@Override
					public void onNoNet() {
						showToastNoNet();
					}
				},newName,device.getDeviceId());
		}
	}

	private void showToastServerBusy() {
		iBindDeviceView.dismissAlertDeviceAliasDailog();
		iBindDeviceView.showToastServerBusy();
	}

	private void showToastAlertFail() {
		iBindDeviceView.showToastRenameFail();
		iBindDeviceView.dismissAlertDeviceAliasDailog();
	}
}
