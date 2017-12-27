package com.quhwa.cloudintercom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.UserInfo;
import com.quhwa.cloudintercom.presenter.LoginPresenter;
import com.quhwa.cloudintercom.utils.Code;
import com.quhwa.cloudintercom.utils.CommonUtil;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.Status;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.ILoginView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;
/**
 * 登陆界面
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class LoginActivity extends BaseActivity implements OnClickListener,ILoginView{

	private EditText etUsername,etPassword;
	private AppCompatButton btnLogin;
	private TextView tvForgetPsw,tvRegister,tvBack;
	private UserInfo userInfo;
	private String Tag = "LoginActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setView();
		recoverWidgetStatus();
	}
	/**
	 * 恢复用户名和密码输入状态
	 */
	private void recoverWidgetStatus() {
		new LoginPresenter(LoginActivity.this).recoverWidgetShowStatus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_fortget_password:
			
			break;
		case R.id.back:
			LoginActivity.this.finish();
			break;
		case R.id.btn_login:
			login();
			break;
		case R.id.tv_register:
			startToRegisterActivity();
			break;
		}
	}
	private void login() {
		String username = etUsername.getText().toString().trim();
		String password = etPassword.getText().toString().trim();
		userInfo.setPassword(password);
		userInfo.setUsername(username);
		loginToServer(userInfo);
	}
	private void loginToServer(UserInfo userInfo) {
		new LoginPresenter(LoginActivity.this, userInfo).login();
	}
	/**
	 * 启动注册界面
	 */
	private void startToRegisterActivity() {
		startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class), Code.LOGINACTIVITY_TO_REGISTERACTIVITY_REQUESTCODE);
	}
	@Override
	public void loadDialog() {
		MyDialog.showDialog(LoginActivity.this, R.string.logining);
	}
	@Override
	public void showToastLoginSuccess(UserInfo userInfo) {
		MyToast.showToast(LoginActivity.this, R.string.login_success);
		Status.LOGIN_STATUS = true;
		MySharedPreferenceManager.saveBoolean(this, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY, Status.LOGIN_STATUS);
		finishLoginActivity(userInfo);
	}

	private void finishLoginActivity(UserInfo userInfo){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("userInfo", userInfo);
		intent.putExtras(bundle);
		setResult(Activity.RESULT_OK, intent);
		LoginActivity.this.finish();
	}
	@Override
	public void showToastLoginFail() {
		MyToast.showToast(LoginActivity.this, R.string.server_is_busy);
	}
	@Override
	public void dismissDialog() {
		MyDialog.dismissDialog();
	}
	@Override
	public void showToastInputIsNull() {
		MyToast.showToast(LoginActivity.this, R.string.input_no_null);
	}
	@Override
	public void showToastPasswordError() {
		MyToast.showToast(LoginActivity.this, R.string.password_error);
	}
	@Override
	public void showToastUsernameIsNotExist() {
		MyToast.showToast(LoginActivity.this, R.string.username_not_exist);
	}
	
	@Override
	public void saveData(UserInfo userInfo) {
	}

	@Override
	public void updateData(UserInfo userInfo) {
	}
	
	@Override
	public void recoverWidgetShowStatus(UserInfo userInfo) {
		if(userInfo != null){
			etUsername.setText(userInfo.getUsername());
			etPassword.setText(userInfo.getPassword());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Code.LOGINACTIVITY_TO_REGISTERACTIVITY_REQUESTCODE && resultCode == Activity.RESULT_OK){
			Bundle bundle = data.getExtras();
			UserInfo userInfo = (UserInfo) bundle.getSerializable("userInfo");
			MyLog.print(Tag, "---回传给LoginActivity的数据userInfo---"+userInfo.toString(), MyLog.PRINT_GREEN);
			etUsername.setText(userInfo.getUsername());
			etPassword.setText(userInfo.getPassword());
		}
		
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		//标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.login_title);
		TextView tvLogin =(TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.login);
		LinearLayout llInputUsernameView = (LinearLayout) findViewById(R.id.login_username);
//		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)llInputUsernameView.getLayoutParams();
//		layoutParams.setMargins(55,60,55, 0);
//		llInputUsernameView.setLayoutParams(layoutParams);
		llInputUsernameView.findViewById(R.id.iv_input).setBackgroundResource(R.drawable.login_user);
		etUsername = (EditText) llInputUsernameView.findViewById(R.id.et_text);
		etUsername.setHint(R.string.phone_num);
		LinearLayout llInputPasswordView = (LinearLayout) findViewById(R.id.login_password);
//		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)llInputPasswordView.getLayoutParams();
//		lp.setMargins(55, 20,55, 0);
//		llInputPasswordView.setLayoutParams(lp);
		llInputPasswordView.findViewById(R.id.iv_input).setBackgroundResource(R.drawable.login_pwd);
		etPassword = (EditText) llInputPasswordView.findViewById(R.id.et_text);
		etPassword.setHint(R.string.password);
		etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
		tvForgetPsw = (TextView) findViewById(R.id.tv_fortget_password);
		tvRegister = (TextView) findViewById(R.id.tv_register);
		tvForgetPsw.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		tvBack.setOnClickListener(this);
		String username = etUsername.getText().toString().trim();
		String password = etPassword.getText().toString().trim();
		userInfo = new UserInfo(username,password);
	}

}
