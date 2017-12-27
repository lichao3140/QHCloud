package com.quhwa.cloudintercom.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.DoorDevicesListAdapter;
import com.quhwa.cloudintercom.adapter.ModifyUnlockPswAdapter;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.presenter.VisitorPwdPresenter;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.PopupWindowManager;
import com.quhwa.cloudintercom.utils.ShareUtil;
import com.quhwa.cloudintercom.utils.TimerUtils;
import com.quhwa.cloudintercom.view.IVisitorPwdView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class VisitorPwdActivity extends BaseActivity implements OnClickListener,IVisitorPwdView,OnItemClickListener {
	private ImageView ivShare;
	private LinearLayout llIndoor;
	private AutoRelativeLayout llShowPwd;
	private AutoLinearLayout llShowLastPwd;
	private ListView lvDeviceList;
	private TextView tvBack;
	public static VisitorPwdPresenter visitorPwdPresenter;
	private String Tag = "VisitorPwdActivity";
	private ModifyUnlockPswAdapter adapter;
	private TextView tvShowPwd,tvLastTimePwd,tvShowDoorDevice;
	private String doorNum;
	private PopupWindowManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visitor_pwd);
		setView();
		initListener();
	}
	
	private void initListener() {
		ivShare.setOnClickListener(this);
		tvBack.setOnClickListener(this);
		lvDeviceList.setOnItemClickListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.area_visitor_title);
		TextView tvVisitorPwd = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvVisitorPwd.setText(R.string.visitor_password);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		ivShare = (ImageView) findViewById(R.id.iv_share);
		lvDeviceList = (ListView) findViewById(R.id.lv_visitor_pwd);
		tvShowPwd = (TextView) findViewById(R.id.tv_pwd);
		tvLastTimePwd = (TextView) findViewById(R.id.last_time_pwd);
		tvShowDoorDevice = (TextView) findViewById(R.id.tv_show_door);
		llIndoor = (LinearLayout) findViewById(R.id.ll_choose);
		llShowPwd = (AutoRelativeLayout) findViewById(R.id.ll_show_pwd);
		llShowLastPwd = (AutoLinearLayout) findViewById(R.id.ll_show_last_time_pwd);

		String pwd = null;
		String lastTimePwd = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_VISITOR_PASSWORD_KEY);
		if(lastTimePwd != null){
			pwd = lastTimePwd;
		}else{
			pwd = "0000";
		}
		tvLastTimePwd.setText(pwd);
		getDeviceList();
		visitorPwdPresenter.showLastTimeVisitorPwd();
	}

	private void getDeviceList() {
		visitorPwdPresenter = new VisitorPwdPresenter(this,this);
		visitorPwdPresenter.getDeviceList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			VisitorPwdActivity.this.finish();
			break;
		case R.id.iv_share:
			share();
			break;

		default:
			break;
		}
	}
	private void share() {
		String startTime = TimerUtils.timeLongToDate(System.currentTimeMillis(),"yyyy-MM-dd hh:mm");
		String endTime = TimerUtils.timeLongToDate(System.currentTimeMillis()+600000,"yyyy-MM-dd hh:mm");
		Resources resources = getResources();
		ShareUtil.showShare(this, resources.getString(R.string.visitor_password_)+visitorPwd+"\n"+resources.getString(R.string.effective_time)+startTime+resources.getString(R.string.dao)+endTime);
	}

	/**
	 * 创建密码
	 * @param device
	 * @param doorNum
	 */
	private void createVisitorPwd(Device device, String doorNum) {
//		if(MySharedPreferenceManager.queryBoolean(MyApplication.instance, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)){
//		if(null == devices || devices.isEmpty()){
//			MyToast.showToast(this, R.string.please_bind_device_first);
//			return;
//		}else{
//			visitorPwdPresenter.createVisitorPwd(sipId,doorNum);
//		}
//		}else{
//			MyToast.showToast(this, R.string.please_login_first);
//			startActivity(new Intent(VisitorPwdActivity.this,LoginActivity.class));
//		}
		visitorPwdPresenter.createVisitorPwd(device.getSipid(),doorNum);
	}
	private void setAdapter(List<BoundResult.Device> devices) {
		adapter = new ModifyUnlockPswAdapter(this, devices);
		lvDeviceList.setAdapter(adapter);
	}
	private String visitorPwd;
	@Override
	public void showVisitorPwdSuccess(String visitorPwd) {
		this.visitorPwd = visitorPwd;
		MyToast.showToast(this, R.string.create_success);
		llShowPwd.setVisibility(View.VISIBLE);
		tvShowPwd.setText(visitorPwd);
		String doorStr = getResources().getString(R.string.door_device);
		tvShowDoorDevice.setText(doorNum.substring(doorNum.length()-2, doorNum.length())+doorStr);
		MyAnimation.scaleAnimation1(llShowPwd);
		MyDialog.dismissDialog();
		manager.dismissPopupWindow();
	}
	@Override
	public void randomCreating() {
		MyDialog.showDialog(this, R.string.producing);
	}

	@Override
	public void showVisitorPwdFail() {
		MyToast.showToast(this, R.string.create_fail);
		MyDialog.dismissDialog();
	}

	@Override
	public void showLastTimeVisitorPassword(String lastVisitorPwd) {
	}
	private List<BoundResult.Device> deviceList;
	@Override
	public void loadDeviceList(List<BoundResult.Device> devices) {
		this.deviceList = devices;
		setAdapter(devices);
	}

	@Override
	public void showToastServerIsBusy() {
		MyToast.showToast(this,R.string.server_is_busy);
	}

	@Override
	public void showToastNoNet() {
		MyToast.showToast(this,R.string.no_internet);
	}

	@Override
	public void finishRefreshFail() {

	}

	@Override
	public void showViewIfLogin() {
		if (llShowLastPwd.getVisibility() == View.GONE){
			llShowLastPwd.setVisibility(View.VISIBLE);
		}
		if(llIndoor.getVisibility() == View.GONE){
			llIndoor.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		changeListCheckStatus(position);
		showDoorDeviceList(deviceList.get(position));
	}

	/**
	 * 改变列表状态
	 * @param position
     */
	private void changeListCheckStatus(int position) {
		for(int i = 0;i<deviceList.size();i++){
			deviceList.get(i).setCheck(false);
		}
		deviceList.get(position).setCheck(true);
		adapter.notifyDataSetChanged();
		if(llShowPwd.getVisibility() == View.VISIBLE){
			llShowPwd.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示门口机列表
	 * @param device
     */
	private void showDoorDeviceList(final BoundResult.Device device) {
		llShowLastPwd.setVisibility(View.GONE);
		manager = new PopupWindowManager(this);
		manager.showPopupWindow(this, R.layout.door_device_list, R.layout.activity_visitor_pwd, ViewGroup.LayoutParams.MATCH_PARENT
				, ViewGroup.LayoutParams.WRAP_CONTENT, new PopupWindowManager.LayoutCallBackListener() {
					@Override
					public void layoutCallBack(View popupWindowLayout) {
						ListView lvdoorDeviceList = (ListView) popupWindowLayout.findViewById(R.id.lv_door_device_list);
						String unitDoorNo = device.getUnitDoorNo();
						final List<String> doorList = new ArrayList<String>();
						if(unitDoorNo != null){
							if (!unitDoorNo.contains("_")) {
								doorList.add(unitDoorNo);
							} else {
								while (unitDoorNo.contains("_")) {
									doorList.add(unitDoorNo.substring(0,
											unitDoorNo.indexOf("_")));
									unitDoorNo = unitDoorNo.substring(
											unitDoorNo.indexOf("_") + 1,
											unitDoorNo.length());
								}
								doorList.add(unitDoorNo);
							}
						}

						if(doorList != null && doorList.size()>0){
							lvdoorDeviceList.setAdapter(new DoorDevicesListAdapter(VisitorPwdActivity.this, doorList));
							lvdoorDeviceList.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view,
														int position, long id) {
									doorNum = doorList.get(position);
									createVisitorPwd(device,doorNum);
								}
							});
						}

						TextView tvHideDoorList = (TextView) popupWindowLayout.findViewById(R.id.tv_hide_door_list);
						tvHideDoorList.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								manager.dismissPopupWindow();
							}
						});
					}
				}, false);
	}
}
