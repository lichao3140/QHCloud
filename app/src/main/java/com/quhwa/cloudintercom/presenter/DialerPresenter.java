package com.quhwa.cloudintercom.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.Constants;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IDialerView;
/**
 * 拨号界面presenter层
 *
 * @author lxz
 * @date 2017年4月5日
 */
public class DialerPresenter{
	private IDialerView dialerView;
	public DialerPresenter(IDialerView dialerView){
		this.dialerView = dialerView;
	}
	/**
	 * 删除号码
	 * @param sbShowNum
	 */
	public void deleteNum(StringBuffer sbShowNum){
		if(dialerView != null){
			if(sbShowNum.length() != 0){
				sbShowNum.deleteCharAt(sbShowNum.length()-1);
				if(sbShowNum.length() == 0){
					dialerView.hideImageDelete();
				}
			}
		}
	}
	/**
	 * 显示删除按钮
	 * @param ib
	 */
	public void showImageDelete(ImageButton ib){
		if(dialerView != null){
			if(ib.getVisibility() == View.GONE){
				dialerView.showImageDelete(ib);
			}
		}
	}
	/**
	 * 返回
	 * @param ib
	 */
	public void back(ImageButton ib){
		if(dialerView != null){
			if(ib.getVisibility() == View.VISIBLE){
				dialerView.back(ib);
			}
		}
	}
	/**
	 * 拨号前判断
	 * @param etShowNum
	 * @param sbShowNum
	 */
	public void call(EditText etShowNum,StringBuffer sbShowNum){
		if(dialerView != null){
			if(sbShowNum.length() == 0){
				dialerView.hideImageDelete();
			}
			if(MySharedPreferenceManager.queryBoolean(MyApplication.instance, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)){
				//登陆状态：可打电话
				if(TextUtils.isEmpty(etShowNum.getText().toString())){
					dialerView.showToastInputIsNull();
				}else{
					makeCall(etShowNum);
				}
			}else{
				// 未登录状态：不可打电话，跳转到登陆界面
				dialerView.showToastLoginFirst();
				dialerView.startToLoginActivity();
			}
			
			
		}
	}
	/**
	 * 呼叫
	 * @param etShowNum
	 */
	private void makeCall(EditText etShowNum) {
//		PJSipService.CURRENT_CALL_TYPE = PJSipService.CALL_TYPE_DOOR_TO_DOOR;
		String username = etShowNum.getText().toString().trim();
		String sipId = "sip:"+username+"@"+Constants.SERVER_IP;
		PJSipService.instance.makeCall(sipId,1);
	}
}
