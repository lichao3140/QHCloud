package com.quhwa.cloudintercom.model.login;

import java.util.List;

import com.quhwa.cloudintercom.bean.Result;
import com.quhwa.cloudintercom.bean.ReturnResult;
import com.quhwa.cloudintercom.bean.UserInfo;
/**
 * 登陆model层接口
 *
 * @author lxz
 * @date 2017年3月23日
 */
public interface ILoginModel {
	void loadLoginData(LoginOnLoadListener loginOnLoadListener,UserInfo userInfo);
	interface LoginOnLoadListener{
		void onComplete(Result result);//可获取到数据
		void onCompleteFail();//获取不到数据
		void onNoNet();
	}
	
	void recoverWidgetShowStatus(RecoverOnloadListener recoverOnloadListener);
	interface RecoverOnloadListener{
		void onComplete(UserInfo userInfo);
	}

	void sendToken(SendTokenOnLoadListener sendTokenOnLoadListener,String token);
	interface SendTokenOnLoadListener{
		void onComplete(ReturnResult result);//可获取到数据
		void onCompleteFail();//获取不到数据
		void onNoNet();
	}
}
