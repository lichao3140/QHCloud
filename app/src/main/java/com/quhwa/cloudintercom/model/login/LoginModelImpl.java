package com.quhwa.cloudintercom.model.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.Result;
import com.quhwa.cloudintercom.bean.ReturnResult;
import com.quhwa.cloudintercom.bean.UserInfo;
import com.quhwa.cloudintercom.netmanager.OkhttpManager;
import com.quhwa.cloudintercom.netmanager.RequestParamsValues;
import com.quhwa.cloudintercom.utils.Constants;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
/**
 * 登陆model层，获取登陆数据
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class LoginModelImpl implements ILoginModel{
	private static final String ANDROID_TYPE = "1";
	private String Tag = "LoginModelImpl";
	private LoginOnLoadListener loginOnLoadListener;
	private int flag = -1;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG.LOGIN_SUCCESS:
				Result result = (Result) msg.obj;
				loginOnLoadListener.onComplete(result);
				break;
			case MSG.SERVER_EXCEPTION:
				switch (flag){
					case 0:
						loginOnLoadListener.onCompleteFail();
						break;
					case 1:
						sendTokenOnLoadListener.onCompleteFail();
						break;
				}
				break;
			case MSG.NO_NET_MSG://无网络连接
				switch (flag){
					case 0:
						loginOnLoadListener.onNoNet();
						break;
					case 1:
						sendTokenOnLoadListener.onNoNet();
						break;
				}
				break;

			case MSG.SEND_TOKEN_SUCCESS:
				ReturnResult result1 = (ReturnResult) msg.obj;
				sendTokenOnLoadListener.onComplete(result1);
				break;
			}
		};
	};
	private SendTokenOnLoadListener sendTokenOnLoadListener;

	@Override
	public void loadLoginData(LoginOnLoadListener loginOnLoadListener,
			UserInfo userInfo) {
		MyLog.print(Tag, "---userInfo---"+userInfo.toString(), MyLog.PRINT_GREEN);
		flag = 0;
		this.loginOnLoadListener = loginOnLoadListener;
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", userInfo.getUsername());
		map.put("password", userInfo.getPassword());
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
		clsList.add(UserInfo.class);
		clsList.add(Result.class);
		okhttpManager.getData(Constants.LOGIN_URL, addRequestParams,
				clsList, handler, MSG.LOGIN_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
		
	}

	@Override
	public void recoverWidgetShowStatus(RecoverOnloadListener recoverOnloadListener) {
		// 查询数据库userInfo表最后的一条数据
		String name = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_NAME_KEY);
		String password = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_KEY);
		UserInfo userInfo = new UserInfo(name,password);
		recoverOnloadListener.onComplete(userInfo);
	}

	@Override
	public void sendToken(SendTokenOnLoadListener sendTokenOnLoadListener,String token) {
		flag = 1;
		this.sendTokenOnLoadListener = sendTokenOnLoadListener;
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		int userId = MySharedPreferenceManager.queryInt(MyApplication.instance,Table.TAB_USER, Table.TAB_USER_USERID_KEY);
		map.put("userId", userId+"");
		map.put("type", ANDROID_TYPE);
		map.put("token", token);
		MyLog.print(Tag,"----token----"+token,MyLog.PRINT_RED);
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
		clsList.add(ReturnResult.class);
		okhttpManager.getData(Constants.SEND_JPUSH_TOKEN, addRequestParams,
				clsList, handler, MSG.SEND_TOKEN_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
	}

}
