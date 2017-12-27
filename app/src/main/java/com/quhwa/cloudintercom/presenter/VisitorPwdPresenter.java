package com.quhwa.cloudintercom.presenter;

import android.content.Context;

import org.pjsip.pjsua2.app.MSG_TYPE;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.model.binddevice.DeviceDataModelImpl;
import com.quhwa.cloudintercom.model.binddevice.IDeviceDataModel;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.ISipMessageReturnListener;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.SipMsgManager;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.utils.SoundManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IVisitorPwdView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

import java.util.ArrayList;

public class VisitorPwdPresenter implements ISipMessageReturnListener{
	private IVisitorPwdView iVisitorPwdView;
	private String Tag = "VisitorPwdPresenter";
	private int visitorPwd;
	private IDeviceDataModel iDeviceDataModel = new DeviceDataModelImpl();
	private Context context;

	public VisitorPwdPresenter(IVisitorPwdView iVisitorPwdView,Context context) {
		super();
		this.iVisitorPwdView = iVisitorPwdView;
		this.context = context;
	}
	/**
	 * 创建访客密码
	 * @param sipId
	 */
	public void createVisitorPwd(String sipId,String doorNum){
		if(iVisitorPwdView != null){
			iVisitorPwdView.randomCreating();
			PJSipService.currentMsgType = MSG_TYPE.MSG_SEND_VISITOR_PASSWORD;
			visitorPwd = (int) (Math.random()*9000+1000);
			MyLog.print(Tag, visitorPwd+"", MyLog.PRINT_RED);
			SipMsgManager.sendInstantMessage(sipId, SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.SEND_VISITOR_PWD_TYPE, visitorPwd+"_"+doorNum, "")), PJSipService.account);
		}
	}
	/**
	 * 显示上次访客密码
	 */
	public void showLastTimeVisitorPwd(){
		if(iVisitorPwdView != null){
			String lastVisitorPwd = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_VISITOR_PASSWORD_KEY);
			if(lastVisitorPwd != null && lastVisitorPwd.length()>0){
				iVisitorPwdView.showLastTimeVisitorPassword(lastVisitorPwd);
			}
		}
	}
	
	@Override
	public void sipMessageReturnSuccess() {
		if(iVisitorPwdView != null){
			iVisitorPwdView.showVisitorPwdSuccess(visitorPwd+"");
			MySharedPreferenceManager.saveString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_VISITOR_PASSWORD_KEY, visitorPwd+"");
			SoundManager.play(MyApplication.instance, "random_pwd_audio_name");
		}
	}
	@Override
	public void sipMessageReturnFail() {
		if(iVisitorPwdView != null){
			iVisitorPwdView.showVisitorPwdFail();
		}
	}

	public void getDeviceList(){
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {//登陆状态
			if(iVisitorPwdView != null && iDeviceDataModel != null){
				MyDialog.showDialog(context, R.string.loading);
				iVisitorPwdView.showViewIfLogin();
				iDeviceDataModel.queryDeviceList(new IDeviceDataModel.QueryDeviceListListener() {
					@Override
					public void onComplete(ArrayList<BoundResult.Device> deviceInfoList) {
						if(deviceInfoList != null && deviceInfoList.size()>0){
							iVisitorPwdView.loadDeviceList(deviceInfoList);
							MyDialog.dismissDialog();
						}else{
							MyDialog.dismissDialog();
							MyToast.showToast(context,R.string.no_device_bind);
						}
					}

					@Override
					public void onCompleteFail() {
						MyDialog.dismissDialog();
						iVisitorPwdView.showVisitorPwdFail();
					}

					@Override
					public void onNoNet() {
						MyDialog.dismissDialog();
					}
				});
			}

		}else{
			iVisitorPwdView.finishRefreshFail();
			MyToast.showToast(MyApplication.instance, R.string.please_login_first);
		}
	}
}
