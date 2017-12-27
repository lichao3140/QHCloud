package com.quhwa.cloudintercom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.UserInfo;
import com.quhwa.cloudintercom.presenter.RegisterPresenter;
import com.quhwa.cloudintercom.view.IRegisterView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;


/**
 * 注册界面
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class RegisterActivity extends BaseActivity implements OnClickListener,IRegisterView{
	private EditText etRegisterUsername,etRegisterPassword,etRegisterPasswordConfirm;
	private TextView tvBack;
	private AppCompatButton btnRegister;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			RegisterActivity.this.finish();
			break;
		case R.id.btn_register:
			register();
			break;
		default:
			break;
		}
		
	}
	/**
	 * 注册前判断
	 */
	private void register() {
		String username = etRegisterUsername.getText().toString().trim();
		String password = etRegisterPassword.getText().toString().trim();
		String passwordConfirm = etRegisterPasswordConfirm.getText().toString().trim();
		UserInfo userInfo = new UserInfo(username,password,passwordConfirm);
		registerToServer(userInfo);
	}
	/**
	 * 注册
	 * @param userInfo
	 */
	private void registerToServer(UserInfo userInfo) {
		new RegisterPresenter(RegisterActivity.this, userInfo).register();
	}
	@Override
	public void loadDialog() {
		MyDialog.showDialog(this,R.string.registering);
		
	}
	@Override
	public void showToastResiterSuccess(UserInfo userInfo) {
		MyToast.showToast(RegisterActivity.this, R.string.register_success);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		userInfo.setPassword(etRegisterPassword.getText().toString().trim());
		bundle.putSerializable("userInfo", userInfo);
		intent.putExtras(bundle);
		setResult(Activity.RESULT_OK, intent);
		RegisterActivity.this.finish();
	}
	@Override
	public void showToastResiterFail() {
		MyToast.showToast(RegisterActivity.this, R.string.server_is_busy);
	}
	@Override
	public void showToastResiterUsernameExsit() {
		MyToast.showToast(RegisterActivity.this, R.string.username_already_exist);
	}
	@Override
	public void dismissDialog() {
		MyDialog.dismissDialog();
		
	}
	@Override
	public void showToastInputIsNull() {
		MyToast.showToast(RegisterActivity.this, R.string.input_no_null);
	}
	@Override
	public void showToastPasswordInconsistency() {
		MyToast.showToast(RegisterActivity.this, R.string.password_inconsistency);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.register_title);
		TextView tvLogin =(TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.register);
		LinearLayout llInputUsernameView = (LinearLayout) findViewById(R.id.register_username);
		etRegisterUsername = (EditText) llInputUsernameView.findViewById(R.id.et_text);
		etRegisterUsername.setHint(R.string.phone_num);
		etRegisterUsername.setInputType(InputType.TYPE_CLASS_NUMBER);
		LinearLayout llInputPasswordView = (LinearLayout) findViewById(R.id.register_password);
		etRegisterPassword = (EditText) llInputPasswordView.findViewById(R.id.et_text);
		etRegisterPassword.setHint(R.string.password);
		etRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		LinearLayout llInputllInputPasswordConfirmViewView = (LinearLayout) findViewById(R.id.register_password_confirm);
		etRegisterPasswordConfirm = (EditText) llInputllInputPasswordConfirmViewView.findViewById(R.id.et_text);
		etRegisterPasswordConfirm.setHint(R.string.confirm_password);
		etRegisterPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		tvBack.setOnClickListener(this);
		btnRegister = (AppCompatButton) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void showToastInputRightMobileNum() {
		MyToast.showToast(this, R.string.input_right_mobile_num);
	}
}
