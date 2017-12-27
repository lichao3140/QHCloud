package com.quhwa.cloudintercom.activity;

import java.util.ArrayList;
import java.util.List;

import org.pjsip.pjsua2.app.MSG_TYPE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.DeviceListAdapter;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.presenter.DeviceDataPresenter;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.CommonUtil;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.SipMsgManager;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IBindDeviceView;
import com.quhwa.cloudintercom.widget.AlertDialogChooseStatusListener;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
/**
 * 绑定设备Activity
 *
 * @author lxz
 * @date 2017年4月15日
 */
public class BindDeviceActivity extends BaseActivity implements OnClickListener,IBindDeviceView,OnItemLongClickListener,OnItemClickListener,AlertDialogChooseStatusListener,OnRefreshListener {

	private TextView tvBack;
	private ImageButton ibBindDevice;
	private ListView lvDeviceList;
	private RelativeLayout rlDelete;
	private AppCompatButton btnCancel,btnOk;
	private SmartRefreshLayout refreshView;
	public static final int START_TO_CAPTUREACTIVITY_REQUEST_CODE = 1;
	private static final int START_TO_LOGINACTIVITY_REQUEST_CODE = 0;
	public static DeviceDataPresenter deviceDataPresenter= null;
	private DeviceListAdapter adapter;
	private String Tag = "BindDeviceActivity";
	public static BindDeviceActivity instance;
	private ArrayList<BoundResult.Device> devices = new ArrayList<>();
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG.MSG_SEND_SIPID_AND_TOKEN_TO_INDOOR:
				String sipId = (String)msg.obj;
				PJSipService.currentMsgType = MSG_TYPE.MSG_BIND;//把当前消息设为绑定消息（即向室内机发送sipId消息）
				String mySipId = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID);
				String token = CommonUtil.getJpushToken();
				if(sipId != null && token != null && token.length() > 0){
					String message = "sipId="+mySipId+","+"token="+token+","+"mobileType="+SipMsgType.MOBILETYPE;
					MyLog.print(Tag, "message:"+message, MyLog.PRINT_RED);
					SipMsgManager.sendInstantMessage(sipId, SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.SEND_SIPID_AND_TOKEN_TYPE, message, "")), PJSipService.account);
					MyLog.print(Tag, "把sipId和token发给室内机", MyLog.PRINT_RED);
				}else{
					MyLog.print(Tag, "获取极光推送registerId失败token为null或sipId为null", MyLog.PRINT_RED);
					MyDialog.dismissDialog();
					showToastDeviceIsBindFail();
				}
				break;

			default:
				break;
			}
		};
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_device);
		instance = this;
		setView();
		initListener();

	}
	/**
	 * 设置适配器
	 */
	private void setAdapter() {
		deviceDataPresenter = new DeviceDataPresenter(this);
		//deviceDataPresenter.loadDeviceList();
		deviceDataPresenter.queryDeviceList();
	}
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvBack.setOnClickListener(this);
		ibBindDevice.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
//		lvDeviceList.setOnItemLongClickListener(this);
		lvDeviceList.setOnItemClickListener(this);
		refreshView.setOnRefreshListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.bind_device_title);
		TextView tvLogin = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.bind_device);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		ibBindDevice = (ImageButton) findViewById(R.id.ib_bind_device);
		lvDeviceList = (ListView) findViewById(R.id.lv_devices_list);
		rlDelete = (RelativeLayout) findViewById(R.id.ll_delete);
		btnCancel = (AppCompatButton) findViewById(R.id.btn_cancel);
		btnOk = (AppCompatButton) findViewById(R.id.btn_ok);
		refreshView = (SmartRefreshLayout) findViewById(R.id.smart_refresh_device_list);
		refreshView.setPrimaryColorsId(R.color.app_base_color,R.color.white);
		refreshView.autoRefresh();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back://返回
			BindDeviceActivity.this.finish();
			break;
		case R.id.ib_bind_device://二维码扫描
			deviceDataPresenter.scanQRCode();
			break;
		case R.id.btn_ok://确定删除
//			delete();
			break;
		case R.id.btn_cancel://取消删除
//			cancel();
			break;
		default:
			break;
		}
	}
	/**
	 * 删除设备
	 */
	private void delete() {
		if(devices!= null && devices.size()>0){
//			deviceDataPresenter.deleteDevie(adapter,devices.get(position));
			deviceDataPresenter.deleteData(devices.get(position));
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == START_TO_CAPTUREACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
			 //处理扫描结果
			DeviceDataPresenter deviceDataPresenter = new DeviceDataPresenter(BindDeviceActivity.this, data);
			deviceDataPresenter.fetchQRCodeResult(BindDeviceActivity.this,handler);
		}
		if(requestCode == START_TO_LOGINACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
			//登陆成功
			deviceDataPresenter.loadDeviceList();
		}
	}
	@Override
	public void loadDeviceList(ArrayList<BoundResult.Device> devices) {
//		if(devices != null && devices.size()>0){
		this.devices = devices;
		if(adapter != null){
			adapter.setDevices(devices);
			adapter.notifyDataSetChanged();
		}else {
			adapter = new DeviceListAdapter(this, devices);
			lvDeviceList.setAdapter(adapter);
		}
			refreshView.finishRefresh();
//		}
	}
	@Override
	public void showToastDecodeFail() {
		MyToast.showToast(this, R.string.analysis_fail);
	}
	@Override
	public void showToastDeviceIsBind() {
		MyToast.showToast(this, R.string.device_is_binded);
	}
	@Override
	public void showToastDeviceIsBindSuccess() {
		MyToast.showToast(this, R.string.bind_success);
	}
	@Override
	public void showToastQRCodeIsNotGetFromDevice() {
		MyToast.showToast(this, R.string.please_scan_from_device);
	}
	@Override
	public void showToastLoginFirst() {
		MyToast.showToast(this, R.string.please_login_first);
	}
	@Override
	public void startToCaptureActivity() {
		Intent intent = new Intent(this, CaptureActivity.class);
		startActivityForResult(intent, START_TO_CAPTUREACTIVITY_REQUEST_CODE);
	}
	@Override
	public void startToLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, START_TO_LOGINACTIVITY_REQUEST_CODE);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		deviceDataPresenter = null;
	}
	@Override
	public void showToastDeviceIsBindUpdateSuccess() {
		MyToast.showToast(this, R.string.device_is_binded_update_success);
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}
	@Override
	public void showDeleteMenu() {
		//删除菜单可见
		rlDelete.setVisibility(View.VISIBLE);
		MyAnimation.fromDownToUpAnim(this, rlDelete);
		//绑定按钮隐藏
		ibBindDevice.setVisibility(View.GONE);
		MyAnimation.fromUpToDownAnim(this, ibBindDevice);
		//全选按钮可见
//		tvChooseAll.setVisibility(View.VISIBLE);
//		MyAnimation.fromDownToUpAnim(this, tvChooseAll);
	}
	@Override
	public void dismissDeleteMenu() {
		//删除菜单隐藏
		rlDelete.setVisibility(View.GONE);
		MyAnimation.fromUpToDownAnim(this, rlDelete);
		//绑定按钮可见
		ibBindDevice.setVisibility(View.VISIBLE);
		MyAnimation.fromDownToUpAnim(this, ibBindDevice);
		//全选按钮隐藏
//		tvChooseAll.setVisibility(View.GONE);
//		MyAnimation.fromUpToDownAnim(this, tvChooseAll);
	}
	@Override
	public void showToastChooseItemDelete() {
		MyToast.showToast(this, R.string.choose_you_want_delete);
	}
	@Override
	public void showBindingDialog() {
		MyDialog.showDialog(this, R.string.binding);
	}
	@Override
	public void showToastDeviceIsBindFail() {
		MyToast.showToast(this, R.string.bind_fail);
	}
	@Override
	public void showUnBindingDialog() {
		MyDialog.showDialog(this, R.string.unbinding);
	}
	@Override
	public void showUnBindingFail() {
		MyToast.showToast(this, R.string.unbind_fail);
	}
	private int position;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.position = position;
		MyDialog.showSetDeviceDialog(this,this);
	}
	/**
	 * 解绑后加载设备列表
	 */
	public void loadDeviceListAfterUnbind(){
		if(adapter != null && lvDeviceList != null){
			String username = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_NAME_KEY);
			List<BoundResult.Device> devices = DBManager.queryByUsername(MyApplication.instance, Table.DEVICELIST_COLUMN_USERNAME, username);
			adapter.setDevices(devices);
			adapter.notifyDataSetChanged();
		}
	}
	@Override
	public void choosePositiveButton() {
		delete();
	}
	@Override
	public void chooseNegativeButton() {
	}
	@Override
	public void choosePositiveButton(String userPassword, String newPassword,
			String confirmPassword, String sipId,String doorList) {
	}
	@Override
	public void choosePositiveButton(String newName) {
		deviceDataPresenter.renameServerData(newName, devices.get(position));
	}

	@Override
	public void layoutCallBack(View dialogView) {
	}

	@Override
	public void showToastRenameSuccess() {
		MyToast.showToast(this, R.string.modify_success);
	}
	@Override
	public void showToastRenameFail() {
		MyToast.showToast(this, R.string.modify_fail);
	}

	@Override
	public void showToastSetIndoorRoomNum() {
		MyToast.showToast(this,R.string.please_set_indoor_roomNum);
	}

	@Override
	public void showAlertDeviceAliasDailog() {
		MyDialog.showDialog(this,R.string.alerting);
	}

	@Override
	public void dismissAlertDeviceAliasDailog() {
		MyDialog.dismissDialog();
	}

	@Override
	public void showToastNoNet() {
		MyToast.showToast(this,R.string.no_internet);
	}

	@Override
	public void showToastServerBusy() {
		MyToast.showToast(this,R.string.server_is_busy);
	}

	@Override
	public void stopRefresh() {
		if(refreshView.isRefreshing()){
			refreshView.finishRefresh();
		}
	}

	@Override
	public void showToastNoDevice() {
		MyToast.showToast(this,R.string.no_device_bind);
	}

	@Override
	public void dimissSetDeviceDialog() {
		MyDialog.dimissSetDeviceDialog();
	}

	@Override
	public void finishRefresh() {
		if(refreshView.isRefreshing()){
			refreshView.finishRefresh();
		}
	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		setAdapter();
	}
}
