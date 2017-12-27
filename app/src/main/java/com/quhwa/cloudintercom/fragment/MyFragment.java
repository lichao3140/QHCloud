package com.quhwa.cloudintercom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.LoginActivity;
import com.quhwa.cloudintercom.activity.UserInfoActivity;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.UserInfo;
import com.quhwa.cloudintercom.utils.Code;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
/**
 * 我的页面
 *
 * @author lxz
 * @date 2017年3月17日
 */
public class MyFragment extends Fragment implements OnClickListener{
	private String Tag = "MyFragment";
	private View view;
	private ImageView ivLogin;
	private TextView tvLoginStatus;
	private MyFragment instance;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_own, null);
		MyLog.print(Tag, "onCreateView", MyLog.PRINT_GREEN);
		setView();
		return view;
	}
	
	private void setView() {
		ivLogin = (ImageView) view.findViewById(R.id.iv_login);
		ivLogin.setOnClickListener(this);
		tvLoginStatus = (TextView) view.findViewById(R.id.tv_login_status);
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.print(Tag, "onAttach", MyLog.PRINT_GREEN);
		super.onAttach(activity);
		instance = this;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MyLog.print(Tag, "onActivityCreated", MyLog.PRINT_GREEN);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_login:
			startToOtherActivity();
			break;

		default:
			break;
		}
		
	}
	
	private void startToOtherActivity() {
		if(MySharedPreferenceManager.queryBoolean(getActivity(), Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)){
			//已登录状态
			startToUserInfoActivity();
		}else{
			//未登录状态
			startToLoginActivity();
		}
	}
	
	private void startToLoginActivity() {
		startActivityForResult(new Intent(getActivity(), LoginActivity.class), Code.MYFRAGMENT_TO_LOGINACTIVITY_REQUESTCODE);
	}
	
	private void startToUserInfoActivity() {
		Intent intent = new Intent(getActivity(),UserInfoActivity.class);
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Code.MYFRAGMENT_TO_LOGINACTIVITY_REQUESTCODE
				&& resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			UserInfo userInfo = (UserInfo) bundle.getSerializable("userInfo");
			MyLog.print(Tag, "---回传给MyFragment的数据userInfo---"+userInfo.toString(), MyLog.PRINT_GREEN);
			tvLoginStatus.setText(userInfo.getUsername());
		}
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		MyLog.print(Tag, "onResume", MyLog.PRINT_GREEN);
		if(MySharedPreferenceManager.queryBoolean(getActivity(), Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)){
			//登陆状态
			tvLoginStatus.setText(MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_NAME_KEY));
		}else{
			//未登陆
			tvLoginStatus.setText(R.string.logout_status);
		}
	}
	
	
	
	
	
	
	@Override
	public void onStart() {
		super.onStart();
		MyLog.print(Tag, "onStart", MyLog.PRINT_GREEN);
	}
	@Override
	public void onPause() {
		super.onPause();
		MyLog.print(Tag, "onPause", MyLog.PRINT_GREEN);
	}
	@Override
	public void onStop() {
		super.onStop();
		MyLog.print(Tag, "onStop", MyLog.PRINT_GREEN);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		MyLog.print(Tag, "onDestroy", MyLog.PRINT_GREEN);
	}
	@Override
	public void onDetach() {
		super.onDetach();
		MyLog.print(Tag, "onDetach", MyLog.PRINT_GREEN);
	}

	

	



}
