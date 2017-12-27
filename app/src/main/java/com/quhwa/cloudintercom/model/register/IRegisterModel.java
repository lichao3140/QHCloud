package com.quhwa.cloudintercom.model.register;

import com.quhwa.cloudintercom.bean.Result;
import com.quhwa.cloudintercom.bean.UserInfo;
/**
 * 注册model层接口
 *
 * @author lxz
 * @date 2017年3月23日
 */
public interface IRegisterModel {
	void loadRegisterData(RegisterOnLoadListener registerOnLoadListener,UserInfo userInfo);
	interface RegisterOnLoadListener{
		void onComplete(Result result);//可获取到数据
		void onCompleteFail();//获取不到数据
	}
}
