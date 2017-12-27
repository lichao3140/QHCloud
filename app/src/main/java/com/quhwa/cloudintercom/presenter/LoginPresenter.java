package com.quhwa.cloudintercom.presenter;

import android.text.TextUtils;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.Result;
import com.quhwa.cloudintercom.bean.ReturnResult;
import com.quhwa.cloudintercom.bean.UserInfo;
import com.quhwa.cloudintercom.model.login.ILoginModel;
import com.quhwa.cloudintercom.model.login.ILoginModel.LoginOnLoadListener;
import com.quhwa.cloudintercom.model.login.ILoginModel.RecoverOnloadListener;
import com.quhwa.cloudintercom.model.login.LoginModelImpl;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.Code;
import com.quhwa.cloudintercom.utils.CommonUtil;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.ILoginView;
/**
 * 登陆表示层
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class LoginPresenter {
	private ILoginView iLoginView;
	private UserInfo userInfo;
	private String Tag = "LoginPresenter";
	public LoginPresenter(ILoginView iLoginView) {
		this.iLoginView = iLoginView;
	}
	public LoginPresenter(ILoginView iLoginView,UserInfo userInfo){
		this.iLoginView = iLoginView;
		this.userInfo = userInfo;
	}
	ILoginModel loginModel = new LoginModelImpl();
	/**
	 * 登陆
	 */
	public void login(){
		if(loginModel != null && iLoginView != null) {
			if (TextUtils.isEmpty(userInfo.getUsername()) || TextUtils.isEmpty(userInfo.getPassword())) {
				iLoginView.showToastInputIsNull();
			} else {
				String token = CommonUtil.getJpushToken();
				if(token != null){
					sendTokenToServer(token);
				}else {
					MyLog.print(Tag,"token为null，开始登陆",MyLog.PRINT_RED);
					loginToServer();
				}
			}
		}

	}

	/**
	 * 上传token
	 * @param token
     */
	private void sendTokenToServer(String token) {
		if(loginModel != null && iLoginView != null){
			iLoginView.loadDialog();
			loginModel.sendToken(new ILoginModel.SendTokenOnLoadListener() {
				@Override
				public void onComplete(ReturnResult result) {
					if(result.getCode() == Code.RETURN_SUCCESS){
						MyLog.print(Tag,"上传token成功，开始登陆",MyLog.PRINT_RED);
						loginToServer();
					}else{
//						iLoginView.showToastLoginFail();
//						iLoginView.dismissDialog();
						MyLog.print(Tag,"上传token失败，开始登陆",MyLog.PRINT_RED);
						loginToServer();
					}
				}

				@Override
				public void onCompleteFail() {
					iLoginView.showToastLoginFail();
					iLoginView.dismissDialog();
				}

				@Override
				public void onNoNet() {

				}
			},token);
		}
	}

	/**
	 * 登陆到服务器
	 */
	private void loginToServer() {
		loginModel.loadLoginData(new LoginOnLoadListener() {

			@Override
			public void onCompleteFail() {
				iLoginView.showToastLoginFail();
				iLoginView.dismissDialog();
			}

			@Override
			public void onNoNet() {

			}

			@Override
			public void onComplete(Result result) {
				if(result.getCode() == Code.RETURN_SUCCESS){
					UserInfo userInfoFrom = result.getUserInfo();
					iLoginView.showToastLoginSuccess(userInfoFrom);
//							long count = DatabaseManager.INSTANCE.queryCount(UserInfo.class);
					userInfoFrom.setPassword(userInfo.getPassword());
//							Status.LOGIN_STATUS = true;
//							MySharedPreferenceManager.saveBoolean(MyApplication.instance, Table.TAB_LOGIN_STATUS, Table.TAB_LOGIN_STATUS, Status.LOGIN_STATUS);
					MySharedPreferenceManager.saveString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_NAME_KEY, userInfoFrom.getUsername());
					MySharedPreferenceManager.saveString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_KEY, userInfoFrom.getPassword());
					MySharedPreferenceManager.saveString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID, userInfoFrom.getSipid());
					MySharedPreferenceManager.saveString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_PASSWORD, userInfoFrom.getSipPasswd());
					MySharedPreferenceManager.saveString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_SESSION_KEY, userInfoFrom.getSessionKey());
					//保存userId
					MySharedPreferenceManager.saveInt(MyApplication.instance, Table.TAB_USER,
							Table.TAB_USER_USERID_KEY,userInfoFrom.getId());

					//sip注册
					PJSipService.registerToSipServer(userInfoFrom.getSipid(), userInfoFrom.getSipPasswd());
					MyLog.print(Tag, "登陆成功后注册sip账号", MyLog.PRINT_RED);
				}
				if(result.getCode() == Code.RETURN_PASSWD_ERROR){
					iLoginView.showToastPasswordError();
				}
				if(result.getCode() == Code.RETURN_USERNAME_NOT_EXIST){
					iLoginView.showToastUsernameIsNotExist();
				}
				iLoginView.dismissDialog();

			}
		}, userInfo);
	}

	/**
	 * 恢复控件显示状态
	 */
	public void recoverWidgetShowStatus(){
		if(loginModel != null && iLoginView != null){
			loginModel.recoverWidgetShowStatus(new RecoverOnloadListener() {
				@Override
				public void onComplete(UserInfo userInfo) {
					if(userInfo != null){
						iLoginView.recoverWidgetShowStatus(userInfo);
					}
				}
			});
		}
	}
}
