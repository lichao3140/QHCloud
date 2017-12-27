package com.quhwa.cloudintercom.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.presenter.UserInfoPresenter;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.Status;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IUserInfoView;
import com.quhwa.cloudintercom.widget.MyDialog;

/**
 * 用户信息设置界面
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener,IUserInfoView{

	private TextView tvBack,tvUsername,tvSipId,tvSipIdRegisterStatus;
	private AppCompatButton btnLogout,btnSipRegister;
	private UserInfoPresenter userInfoPresenter;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG.MSG_REGISTER_SIP_SERVER:
				try {
					PJSipService.registerToSipServer(MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID), 
							MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_PASSWORD));
					userInfoPresenter.startCheck();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		setView();
		setListener();
		checkSipRegisterStatus();
	}
	/**
	 * 定时检查sip注册状态
	 */
	private void checkSipRegisterStatus() {
		userInfoPresenter = new UserInfoPresenter(this);
		userInfoPresenter.checkSipRegisterStatus(handler);
	}
	/**
	 * 初始化监听器
	 */
	private void setListener() {
		tvBack.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnSipRegister.setOnClickListener(this);
	}
	/**
	 * 初始化控件
	 */
	private void setView() {
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.userinfo_title);
		TextView tvUserInfo =(TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvUserInfo.setText(R.string.userinfo_title);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvUsername.setText(MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_NAME_KEY));
		btnLogout = (AppCompatButton) findViewById(R.id.btn_logout);
		tvSipId = (TextView) findViewById(R.id.tv_sipid);
		tvSipId.setText(MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID));
		tvSipIdRegisterStatus = (TextView) findViewById(R.id.tv_sip_login_status);
		btnSipRegister = (AppCompatButton) findViewById(R.id.btn_sip_register);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.btn_logout:
			logout();
			break;
		case R.id.btn_sip_register:
			register();
			break;

		default:
			break;
		}
		
	}
	private void back() {
		UserInfoActivity.this.finish();
		userInfoPresenter.stopCheck();
	}
	private void logout() {
		userInfoPresenter.deleteToken();
		MyDialog.showDialog(this,R.string.logouting);
	}

	private void deleteData() {
		if(PJSipService.account != null){
			PJSipService.account.delete();
		}
		userInfoPresenter.stopCheck();
		Status.LOGIN_STATUS = false;
		MySharedPreferenceManager.deleteData(this, Table.TAB_USER, Table.TAB_USER_SESSION_KEY);
		MySharedPreferenceManager.saveBoolean(this, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY, Status.LOGIN_STATUS);
		UserInfoActivity.this.finish();
	}

	private void register() {
		MyAnimation.scaleAnimation(btnSipRegister);
		PJSipService.registerToSipServer(MySharedPreferenceManager.queryString(UserInfoActivity.this, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID), 
				MySharedPreferenceManager.queryString(UserInfoActivity.this, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID));
	}
	@Override
	public void showSipRegisterStatus(final boolean isRegistered) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(isRegistered){
					tvSipIdRegisterStatus.setText(R.string.sip_is_registered);
				}else{
					tvSipIdRegisterStatus.setText(R.string.sip_is_not_registered);
				}
			}
		});
	}

	@Override
	public void deleteLocalData() {
		deleteData();
		MyDialog.dismissDialog();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		userInfoPresenter.stopCheck();
	}

}
