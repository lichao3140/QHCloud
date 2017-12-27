package com.quhwa.cloudintercom.presenter;

import android.text.TextUtils;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.model.binddevice.DeviceDataModelImpl;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.SipMsgManager;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.view.ISettingView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

import org.pjsip.pjsua2.app.MSG_TYPE;

import java.util.ArrayList;

public class SettingPresenter {
	public ISettingView iSettingView;
	private IDeviceDataModel iDeviceDataModel = new DeviceDataModelImpl();
	public SettingPresenter(ISettingView iSettingView) {
		super();
		this.iSettingView = iSettingView;
	}
	
	
	/**
	 * 显示修改对话框
	 */
//	public void showDefineDialog(){
//		if(iSettingView != null){
//			iSettingView.showModifyPswDialog();
//		}
//	}
	/**
	 * 修改开锁密码
	 * @param userPassword 用户密码
	 * @param newPassword 新密码
	 * @param confirmPassword 确认密码
	 */
	public void modify(String userPassword, String newPassword, String confirmPassword,String sipId,String doorNo){
		if(iSettingView != null){
			if(TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(newPassword)){//输入为null
				iSettingView.showToastInputIsNotNull();
			}else{//输入不为null
				if(!userPassword.equals(MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_KEY))){
					//用户密码错误
					iSettingView.showToastUserPasswordError();
				}else{//用户密码正确
					if(!newPassword.equals(confirmPassword)){//设置密码不一致
						iSettingView.showToastPasswordNotSame();
					}else{//设置密码一致
						if(newPassword.length() == 6){
							iSettingView.showOnModifyingDialog();
							iSettingView.dismissModifyPswDialog();
							PJSipService.currentMsgType = MSG_TYPE.MSG_SEND_UNLOCK_PASSWORD;
							SipMsgManager.sendInstantMessage(sipId, SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.SEND_UNLOCK_PWD_TYPE, newPassword+"_"+doorNo, "")), PJSipService.account);
						}else{
							iSettingView.showToastPleaseInputSixNumPassword();//提示设置6位数字密码
						}
					}
				}
			}
		}
	}

	/**
	 * 清理缓存：删除所有设备数据
	 */
	public void deleteAll() {
		if(iSettingView != null){
			boolean isDeleteSuccess = DBManager.deleteDevicesByUser(MyApplication.instance, MySharedPreferenceManager.getUsername());
			if(isDeleteSuccess){
				iSettingView.showToastClearSuccess();
			}else{
				iSettingView.showToastClearFail();
			}
		}
	}

	public void queryDeviceList(final int dialogType){
		if(iSettingView != null && iDeviceDataModel != null){
			iDeviceDataModel.queryDeviceList(new IDeviceDataModel.QueryDeviceListListener() {
				@Override
				public void onComplete(ArrayList<BoundResult.Device> deviceInfoList) {
					if(dialogType == 0){//显示修改密码对话框
						if (deviceInfoList != null && deviceInfoList.size()>0){
							iSettingView.showModifyPswDialog(deviceInfoList);
							MyDialog.dismissDialog();
						}else{
							iSettingView.showToastNoDevice();
							MyDialog.dismissDialog();
						}
					}else if(dialogType == 1){//显示防打扰菜单
						if(deviceInfoList != null && deviceInfoList.size()>0){
							iSettingView.showPopumenu(deviceInfoList);
							MyDialog.dismissDialog();
						}else{
							iSettingView.showToastNoDevice();
							MyDialog.dismissDialog();
						}
					}
				}

				@Override
				public void onCompleteFail() {
					MyDialog.dismissDialog();
					MyToast.showToast(MyApplication.instance, R.string.server_is_busy);
				}

				@Override
				public void onNoNet() {
					MyDialog.dismissDialog();
					MyToast.showToast(MyApplication.instance, R.string.no_internet);
				}
			});
		}
	}


}
