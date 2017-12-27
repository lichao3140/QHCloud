package com.quhwa.cloudintercom.activity;

import java.util.List;

import org.pjsip.pjsua2.app.MSG_TYPE;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.DeviceListAdapter;
import com.quhwa.cloudintercom.adapter.UnlockDeviceListAdapter;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.presenter.UnlockPresenter;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.SipMsgManager;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.view.IUnlockView;
import com.quhwa.cloudintercom.widget.AlertDialogChooseStatusListener;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class UnlockActivity extends BaseActivity implements OnClickListener,OnRefreshListener,IUnlockView,OnItemClickListener,AlertDialogChooseStatusListener{

	private TextView tvBack;
	private ListView lvDeviceList;
	private List<Device> devices = null;
	private int currentPosition;
	private String Tag = "UnlockActivity";
	private SmartRefreshLayout refreshView;
	private UnlockPresenter unLockPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unlock);
		setView();
		initListener();
		loadDevieList();
	}
	private void loadDevieList() {
		unLockPresenter = new UnlockPresenter(this,this);
		unLockPresenter.loadDeviceList();
	}
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvBack.setOnClickListener(this);
		lvDeviceList.setOnItemClickListener(this);
		refreshView.setOnRefreshListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.unlock_title);
		TextView tvLogin = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.unlock);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		lvDeviceList = (ListView) findViewById(R.id.lv_unlock_device_list);
		refreshView = (SmartRefreshLayout) findViewById(R.id.smart_refresh_device_list);
		refreshView.setPrimaryColorsId(R.color.app_base_color,R.color.white);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			UnlockActivity.this.finish();
			break;

		default:
			break;
		}
	}
	@Override
	public void loadDeviceList(List<Device> devices) {
		if(refreshView.isRefreshing()){
			refreshView.finishRefresh();
		}
		this.devices = devices;
		lvDeviceList.setAdapter(new UnlockDeviceListAdapter(this, devices));
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		currentPosition = position;
		if(devices != null && devices.size()>0){
//			MyDialog.showAlertDialog(UnlockActivity.this, R.string.are_you_sure_to_unlock,R.string.unlock,R.string.ok,R.string.cancel,this);
			MyDialog.showMyDialog(this, R.string.are_you_sure_to_unlock,R.string.unlock, this,false,devices.get(position));
		}
	}
	@Override
	public void choosePositiveButton() {
//		if(devices != null && devices.size()>0){
//			MyDialog.dismissMyDialog();
//			showUnlockingDialog();
//			PJSipService.currentMsgType = MSG_TYPE.MSG_CALL_OR_DIRECTOR_UNLOCK;
//			SipMsgManager.sendInstantMessage(devices.get(currentPosition).getSipId(), SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.OPEN_DOOR_TYPE, "", SipMsgType.GATE_CART_ENABLE)),PJSipService.account);
//			MyLog.print(Tag , "直接发送开锁命令", MyLog.PRINT_RED);
//		}
	}
	@Override
	public void chooseNegativeButton() {
		MyDialog.dismissMyDialog();
	}
	@Override
	public void choosePositiveButton(String userPassword, String newPassword,
			String confirmPassword,String sipId,String doorList) {
	}
	@Override
	public void showUnlockingDialog() {
		MyDialog.showDialog(this, R.string.unlocking);
	}

	@Override
	public void showToastServerBusy() {
		MyToast.showToast(this,R.string.server_is_busy);
	}

	@Override
	public void showToastNoNet() {
		MyToast.showToast(this,R.string.no_internet);
	}

	@Override
	public void finishRefresh() {
		if(refreshView.isRefreshing()){
			refreshView.finishRefresh();
		}
	}

	@Override
	public void choosePositiveButton(String doorNum) {
		if(devices != null && devices.size()>0){
			MyDialog.dismissMyDialog();
			showUnlockingDialog();
			PJSipService.currentMsgType = MSG_TYPE.MSG_CALL_OR_DIRECTOR_UNLOCK;
			SipMsgManager.sendInstantMessage(devices.get(currentPosition).getSipid(), SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.OPEN_DOOR_TYPE, "doorCode="+doorNum, SipMsgType.GATE_CART_ENABLE)),PJSipService.account);
			MyLog.print(Tag , "直接发送开锁命令", MyLog.PRINT_RED);
		}
	}

	@Override
	public void layoutCallBack(View dialogView) {
	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		if(unLockPresenter != null){
			unLockPresenter.loadDeviceList();
		}
	}
}
