package com.quhwa.cloudintercom.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.DeviceListAdapter;
import com.quhwa.cloudintercom.adapter.DoorDevicesListAdapter;
import com.quhwa.cloudintercom.adapter.ModifyUnlockPswAdapter;
import com.quhwa.cloudintercom.adapter.ShieldIncomingAdapter;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.presenter.SettingPresenter;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.PopupWindowManager;
import com.quhwa.cloudintercom.view.ISettingView;
import com.quhwa.cloudintercom.widget.AlertDialogChooseStatusListener;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

import java.util.ArrayList;
import java.util.List;

import static com.quhwa.cloudintercom.widget.MyDialog.dimissDefineDialog;

public class SettingActivity extends BaseActivity implements OnClickListener,ISettingView,AlertDialogChooseStatusListener{

	private TextView tvBack,tvModifyUnlockPwd,tvShiledIncomingStatus,tvClear;
	public SettingPresenter settingPresenter;
	private LinearLayout ll;
	public static ShieldIncomingAdapter adapter;
	private ArrayList<BoundResult.Device> devices;
	private String unitDoorNo;
	private List<String> doorList;
	private String doorNoSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setView();
		initListener();
	}
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvBack.setOnClickListener(this);
		tvModifyUnlockPwd.setOnClickListener(this);
		tvShiledIncomingStatus.setOnClickListener(this);
		tvClear.setOnClickListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.setting_title);
		TextView tvTitle = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvTitle.setText(R.string.setting);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		tvModifyUnlockPwd = (TextView) findViewById(R.id.tv_motify_unlock_psw);
		tvShiledIncomingStatus = (TextView) findViewById(R.id.tv_shield_incoming);
		tvClear = (TextView) findViewById(R.id.tv_clear_device);
		ll = (LinearLayout) findViewById(R.id.ll);
		View view = findViewById(R.id.view_setting);
		if(!MySharedPreferenceManager.queryBoolean(this, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)){
			ll.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}
		settingPresenter = new SettingPresenter(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			SettingActivity.this.finish();
			break;
		case R.id.tv_motify_unlock_psw:
			MyDialog.showDialog(this, R.string.loading);
			settingPresenter.queryDeviceList(0);
			break;
		case R.id.tv_shield_incoming:
			MyDialog.showDialog(this, R.string.loading);
			settingPresenter.queryDeviceList(1);
			break;
		case R.id.tv_clear_device:
			showClearDailog();
			break;
		}
	}
	
	private void showClearDailog() {
		MyDialog.showAlertDialog(this, R.string.clear_data_tip, R.string.clear_device, R.string.ok,
				R.string.cancel,new AlertDialogChooseStatusListener() {
			@Override
			public void choosePositiveButton() {
				clear();
			}
			@Override
			public void chooseNegativeButton() {
			}
			
			@Override
			public void choosePositiveButton(String newName) {
			}

					@Override
					public void layoutCallBack(View dialogView) {
					}

					@Override
			public void choosePositiveButton(String userPassword, String newPassword,
					String confirmPassword, String sipId,String doorList) {
			}
		});
		
	}
	private void clear() {
		settingPresenter.deleteAll();
	}
	@Override
	public void showModifyPswDialog(ArrayList<BoundResult.Device> deviceInfoList) {
		this.devices = deviceInfoList;
		MyDialog.showDefineDialog(this, R.layout.dialog_modify_unlock_password,this);
	}
	@Override
	public void choosePositiveButton() {
	}
	@Override
	public void chooseNegativeButton() {
		dimissDefineDialog();
	}
	@Override
	public void choosePositiveButton(String userPassword, String newPassword,
			String confirmPassword,String sipId,String doorList) {
		settingPresenter.modify(userPassword,newPassword,confirmPassword,sipId,doorList);
	}
	@Override
	public void showToastInputIsNotNull() {
		MyToast.showToast(this, R.string.input_no_null);
	}
	@Override
	public void showToastPasswordNotSame() {
		MyToast.showToast(this, R.string.password_inconsistency);
	}
	@Override
	public void showToastUserPasswordError() {
		MyToast.showToast(this, R.string.user_password_error);
	}
	@Override
	public void showOnModifyingDialog() {
		MyDialog.showDialog(this, R.string.modifying);
	}
	@Override
	public void dismissModifyPswDialog() {
		dimissDefineDialog();
	}
	@Override
	public void showToastPleaseInputSixNumPassword() {
		MyToast.showToast(this, R.string.please_input_six_num_password);
	}
	@Override
	public void choosePositiveButton(String newName) {
	}
	private String sipId = null;
	private ModifyUnlockPswAdapter modifyUnlockPswAdapter;
	@Override
	public void layoutCallBack(View dialogView) {
		final TextView tvOk = (TextView) dialogView.findViewById(R.id.tv_ok);
		final TextView tvModifyOk = (TextView) dialogView.findViewById(R.id.tv_modify_ok);
		final TextView tvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
		final EditText etUserPassword = (EditText) dialogView.findViewById(R.id.et_user_password);
		final EditText etNewPassword = (EditText) dialogView.findViewById(R.id.et_new_password);
		final EditText etConfirmPassword = (EditText) dialogView.findViewById(R.id.et_confirm_password);
		final ListView lvModify = (ListView) dialogView.findViewById(R.id.lv_modify);
		final ListView lvUnitDoorList = (ListView) dialogView.findViewById(R.id.lv_unit_door_list);
		final LinearLayout llModify = (LinearLayout) dialogView.findViewById(R.id.ll_modify);
		final LinearLayout llButton = (LinearLayout) dialogView.findViewById(R.id.ll_button);
		//final List<BoundResult.Device> devices = DBManager.queryByUsername(SettingActivity.this, Table.DEVICELIST_COLUMN_USERNAME, MySharedPreferenceManager.getUsername());

		if(devices != null && devices.size() > 0){
			devices.get(0).setCheck(true);
			sipId = devices.get(0).getSipid();
			unitDoorNo = devices.get(0).getUnitDoorNo();
			modifyUnlockPswAdapter = new ModifyUnlockPswAdapter(SettingActivity.this, devices);
			lvModify.setAdapter(modifyUnlockPswAdapter);
		}else{
			MyToast.showToast(SettingActivity.this, R.string.please_bind_device_first);;
			dimissDefineDialog();
			return;
		}

		tvOk.setOnClickListener(new View.OnClickListener() {//隐藏列表，显示修改选项
			@Override
			public void onClick(View v) {
				lvModify.setVisibility(View.GONE);
				lvUnitDoorList.setVisibility(View.VISIBLE);
				tvOk.setVisibility(View.GONE);
//				tvCancel.setVisibility(View.GONE);
				llButton.setVisibility(View.GONE);
				//显示门口机列表
				doorList = getUnitDoorNoList();
				lvUnitDoorList.setAdapter(new DoorDevicesListAdapter(SettingActivity.this,doorList));

			}
		});

		lvUnitDoorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				doorNoSend = doorList.get(position);
				if(sipId != null){
					MyLog.print("Tag", "sipId:"+sipId, MyLog.PRINT_RED);
					lvUnitDoorList.setVisibility(View.GONE);
					//MyAnimation.alphaAnimation(llModify);
					llModify.setVisibility(View.VISIBLE);
					//tvCancel.setVisibility(View.VISIBLE);
					llButton.setVisibility(View.VISIBLE);
					tvModifyOk.setVisibility(View.VISIBLE);
				}
			}
		});

		tvModifyOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(modifyUnlockPswAdapter != null && sipId != null){
					String userPassword = etUserPassword.getText().toString().trim();
					String newPassword = etNewPassword.getText().toString().trim();
					String confirmPassword = etConfirmPassword.getText().toString().trim();
					choosePositiveButton(userPassword,newPassword,confirmPassword,sipId,doorNoSend);
				}
			}
		});
		lvModify.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				sipId = devices.get(position).getSipid();
				unitDoorNo = devices.get(position).getUnitDoorNo();
				for(int i = 0;i<devices.size();i++){
					devices.get(i).setCheck(false);
				}
				devices.get(position).setCheck(true);
				MyLog.print("Tag", "点击第"+position+"个item的sipId:"+sipId, MyLog.PRINT_RED);
				modifyUnlockPswAdapter.notifyDataSetChanged();
			}
		});

		tvCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseNegativeButton();
			}
		});
	}

	private List<String> getUnitDoorNoList() {
		List<String> doorList = new ArrayList<String>();
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
		return doorList;
	}

	@Override
	public void showToastClearSuccess() {
		MyToast.showToast(this, R.string.clear_success);
	}
	@Override
	public void showToastClearFail() {
		MyToast.showToast(this, R.string.clear_fail);
	}
	public PopupWindowManager manager;
	@Override
	public void showPopumenu(final ArrayList<BoundResult.Device> devices) {
		manager = new PopupWindowManager(SettingActivity.this);
		manager.showPopupWindow(SettingActivity.this, R.layout.dialog_shield_incoming, R.layout.activity_setting,
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, new PopupWindowManager.LayoutCallBackListener() {
					@Override
					public void layoutCallBack(View popupWindowLayout) {
						ListView lv = (ListView) popupWindowLayout.findViewById(R.id.lv_shield_incoming);
						adapter = new ShieldIncomingAdapter(SettingActivity.this, devices,manager);
						lv.setAdapter(adapter);
					}
				},false);
	}

	@Override
	public void showToastNoDevice() {
		MyToast.showToast(this,R.string.please_bind_device_first);
	}

	@Override
	public void showToastSetFail() {
		MyToast.showToast(MyApplication.instance, R.string.set_fail);
	}

	@Override
	public void showToastSetSuccess() {
		MyToast.showToast(MyApplication.instance, R.string.set_success);
	}

}
