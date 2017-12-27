package com.quhwa.cloudintercom.presenter;

import android.text.TextUtils;

import com.quhwa.cloudintercom.bean.Result;
import com.quhwa.cloudintercom.bean.UserInfo;
import com.quhwa.cloudintercom.model.register.IRegisterModel;
import com.quhwa.cloudintercom.model.register.RegisterModelImpl;
import com.quhwa.cloudintercom.model.register.IRegisterModel.RegisterOnLoadListener;
import com.quhwa.cloudintercom.utils.Code;
import com.quhwa.cloudintercom.utils.CommonUtil;
import com.quhwa.cloudintercom.view.IRegisterView;
/**
 * 注册表示层
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class RegisterPresenter {
	private IRegisterView iRegisterView;
	private UserInfo userInfo;

	public RegisterPresenter(IRegisterView iRegisterView, UserInfo userInfo) {
		super();
		this.iRegisterView = iRegisterView;
		this.userInfo = userInfo;
	}

	IRegisterModel registerModel = new RegisterModelImpl();

	public void register() {
		if (registerModel != null) {
			if (iRegisterView != null) {
				// iRegisterView.showToastInputIsNull();
				if (TextUtils.isEmpty(userInfo.getPassword())
						|| TextUtils.isEmpty(userInfo.getUsername())
						|| TextUtils.isEmpty(userInfo.getConfirmPassword())) {// 输入框为空
					iRegisterView.showToastInputIsNull();
				} else {// 注册
					if (isMobileNO(userInfo.getUsername())) {
						if (!userInfo.getPassword().equals(
								userInfo.getConfirmPassword())) {// 密码不一致
							iRegisterView.showToastPasswordInconsistency();
						} else {
							iRegisterView.loadDialog();// 加载对话框
							// 加载注册数据
							registerModel.loadRegisterData(
									new RegisterOnLoadListener() {
										@Override
										public void onComplete(Result result) {// 获取数据成功
											if (result.getCode() == Code.RETURN_SUCCESS) {
												//保存token
												CommonUtil.saveToken();
												iRegisterView
														.showToastResiterSuccess(result
																.getUserInfo());
//												//保存userId
//												MySharedPreferenceManager.saveInt(MyApplication.instance, Table.TAB_USER,
//														Table.TAB_USER_USERID_KEY,result.getUserInfo().getId());
											}
											if (result.getCode() == Code.RETURN_ALREADY_EXIST) {
												iRegisterView
														.showToastResiterUsernameExsit();
											}

											iRegisterView.dismissDialog();// 对话框消失
										}

										@Override
										public void onCompleteFail() {// 获取数据失败
											iRegisterView
													.showToastResiterFail();
											iRegisterView.dismissDialog();
										}
									}, userInfo);
						}
					}else{
						iRegisterView.showToastInputRightMobileNum();
					}
				}

			}
		}
	}
	  /**
     * 判断手机格式是否正确
     * @param mobiles
     * @return
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
     * 联通：130、131、132、152、155、156、185、186 
     * 电信：133、153、180、189、（1349卫通） 177
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
     */
    private boolean isMobileNO(String mobiles) {  
        //"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
        String telRegex = "[1][34578]\\d{9}" ;
        return mobiles.matches(telRegex) ;  
    }
}
