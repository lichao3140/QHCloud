package com.quhwa.cloudintercom.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.DoorDevicesListAdapter;
import com.quhwa.cloudintercom.adapter.ModifyUnlockPswAdapter;
import com.quhwa.cloudintercom.adapter.ShieldIncomingAdapter;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.utils.ISipMessageReturnListener;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
/**
 * 我的对话框类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class MyDialog implements ISipMessageReturnListener{
	static ProgressDialog mProgressDialog;
	static AlertDialog alertDialog;
	private static AlertDialog defineDialog;
	private static String Tag = "MyDialog";
	/**
	 * 显示对话框
	 * @param context 上下文对象
	 * @param message 对话框文本
	 */
	private static LoadingDialog loadingDialog;
	private static AlertDialog myDialog;
	public static void showDialog(Context context,int message){
//		mProgressDialog = new ProgressDialog(context);
//		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		mProgressDialog.setMessage(context.getResources().getString(message));
//		mProgressDialog.setIndeterminate(false);
//		mProgressDialog.setProgress(30);
//		mProgressDialog.setCancelable(true);
//		mProgressDialog.setCanceledOnTouchOutside(false);
//		mProgressDialog.show();
//		//改变mProgressDialog参数
//		Point size = new Point();
//		mProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
//		int width = size.x;
//		int height = size.y;
//		LayoutParams params = mProgressDialog.getWindow().getAttributes();
//		params.alpha = 0.8f; //进度条背景透明度
//		params.dimAmount = 0.1f;//整屏亮暗设置(0f-1f):0f为不变，1f变黑
//		params.width = 3*width/4;
//		params.height = height/6;
//		mProgressDialog.getWindow().setAttributes(params);
		loadingDialog = new LoadingDialog(context, context.getResources().getString(message));
		loadingDialog.show();
	}
	/**
	 * 消除对话框
	 */
	public static void dismissDialog(){
//		if(mProgressDialog != null){
//			mProgressDialog.dismiss();
//		}
		if(loadingDialog != null){
			loadingDialog.close();
		}
	}
	
	/**
	 * 显示AlerDialog
	 * @param context 上下文对象
	 * @param msg 提示内容
	 * @param title 标题
	 * @param positiveButtonStr 确定按钮文本
	 * @param negativeButtonStr 取消按钮文本
	 * @param listener 选择确定按钮和取消按钮的监听器
	 */
	public static void showAlertDialog(Context context,int msg,int title,int positiveButtonStr,int negativeButtonStr,final AlertDialogChooseStatusListener listener){
		alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
        .setPositiveButton(positiveButtonStr, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.choosePositiveButton();
			}
		}).setNegativeButton(negativeButtonStr, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.chooseNegativeButton();
			}
		}).show();  
	}
	/**
	 * alertDialog对话消失
	 */
	public static void dismissAlertDialog(){
		if(alertDialog != null){
			alertDialog.dismiss();
		}
	}
	
	public static void showMyDialog(Context context,final int msg,final int title,final AlertDialogChooseStatusListener listener,boolean isShow,BoundResult.Device device){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		myDialog = builder.create();
		View dialogView = View.inflate(context, R.layout.dialog_choose, null);
		final TextView tvTitle = (TextView) dialogView.findViewById(R.id.tv_choose_title);
		final TextView tvmsg = (TextView) dialogView.findViewById(R.id.tv_choose_msg);
		final TextView tvChooseOk = (TextView) dialogView.findViewById(R.id.tv_choose_ok);
		final TextView tvChooseCancel = (TextView) dialogView.findViewById(R.id.tv_choose_cancel);
		final LinearLayout ll = (LinearLayout) dialogView.findViewById(R.id.ll_dialog_choose);
		myDialog.setView(dialogView, 0, 0, 0, 0);
		myDialog.show();
		if(isShow){
			tvTitle.setText(title);
			tvmsg.setText(msg);
			tvChooseOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.choosePositiveButton();
				}
			});
			tvChooseCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.chooseNegativeButton();
				}
			});
		}else{
			ll.setVisibility(View.GONE);
			tvTitle.setText(R.string.choose_door_which_you_want_to_open);
			final ListView lvDoorDevicesList = (ListView) dialogView.findViewById(R.id.lv_door_devices_list);
			lvDoorDevicesList.setVisibility(View.VISIBLE);
				final List<String> doorList = new ArrayList<String>();
						String doorDeviceStr = device.getUnitDoorNo();
							if(!doorDeviceStr.contains("_")){
								doorList.add(doorDeviceStr);
							}else{
								while(doorDeviceStr.contains("_")){
									doorList.add(doorDeviceStr.substring(0, doorDeviceStr.indexOf("_")));
									doorDeviceStr = doorDeviceStr.substring(doorDeviceStr.indexOf("_")+1, doorDeviceStr.length());
								}
								doorList.add(doorDeviceStr);
							}
							MyLog.print(Tag, "门口机数据:"+doorList.toString(), MyLog.PRINT_RED);
//						break;
//					}
//				}

				if(doorList != null && doorList.size()>0){
					lvDoorDevicesList.setAdapter(new DoorDevicesListAdapter(context, doorList));
					lvDoorDevicesList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, final int position, long id) {
							listener.choosePositiveButton(doorList.get(position));
						}
					});
				}
				
//			}
		}
		
	}
	
	public static void dismissMyDialog(){
		if(myDialog != null){
			myDialog.dismiss();
		}
	}
	
	private static ModifyUnlockPswAdapter adapter = null;
	static String sipId = null;
	public static ShieldIncomingAdapter shieldIncomingAdapter;
	public static AlertDialog shieldDialog;
	private static List<Device> devices;
	private static AlertDialog setDeviceDialog;
	/**
	 * 自定义对话框
	 * @param context 上下文对象
	 * @param layout 自定义布局
	 * @param listener 确定和取消按钮监听器
	 */
	public static void showDefineDialog(final Context context,int layout,final AlertDialogChooseStatusListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		defineDialog = builder.create();
		View dialogView = View.inflate(context, layout, null);
		defineDialog.setView(dialogView, 0, 0, 0, 0);
		defineDialog.show();
		listener.layoutCallBack(dialogView);
	}
	/**
	 * 取消自定义对话框
	 */
	public static void dimissDefineDialog(){
		if(defineDialog != null){
			defineDialog.dismiss();
		}
	}
	
//	public static void showShieldDialog(Context context){
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		shieldDialog = builder.create();
//		View dialogView = View.inflate(context, R.layout.dialog_shield_incoming, null);
//		shieldDialog.setView(dialogView, 0, 0, 0, 0);
//		shieldDialog.show();
//		ListView lvShield = (ListView) dialogView.findViewById(R.id.lv_shield_incoming);
////		final List<Device> devices = DBManager.queryByUsername(context, Table.DEVICELIST_COLUMN_USERNAME, MySharedPreferenceManager.getUsername());
//		String username = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_NAME_KEY);
//		devices = DBManager.queryByUsername(MyApplication.instance, Table.DEVICELIST_COLUMN_USERNAME, username);
//		if(devices != null && devices.size() > 0){
//			shieldIncomingAdapter = new ShieldIncomingAdapter(context, devices, manager);
//			lvShield.setAdapter(shieldIncomingAdapter);
//		}else{
//			MyToast.showToast(context, R.string.please_bind_device_first);
//			dismissShieldDialog();
//		}
//	}
//	public static void dismissShieldDialog(){
//		if(shieldDialog != null){
//			shieldDialog.dismiss();
//		}
//	}
	@Override
	public void sipMessageReturnSuccess() {
		dismissDialog();
		if(devices != null && devices.size() > 0){
			int position = shieldIncomingAdapter.getPosition();
			if(shieldIncomingAdapter.getCurrentStatus().equals("2")){
				devices.get(position).setDeviceStatus("2");
			}else {
				devices.get(position).setDeviceStatus("0");
			}
//			switch (shieldIncomingAdapter.getCurrentStatus()) {
//			case 0:
//				devices.get(position).setShieldStatus(0);
//				break;
//			case 1:
//				devices.get(position).setShieldStatus(1);
//				break;
//
//			default:
//				break;
//			}
			DBManager.updateShieldStatusByRoomNum(MyApplication.instance, devices.get(position));
			MyToast.showToast(MyApplication.instance, R.string.set_success);
			shieldIncomingAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public void sipMessageReturnFail() {
		dismissDialog();
		MyToast.showToast(MyApplication.instance, R.string.set_fail);
		shieldIncomingAdapter.notifyDataSetChanged();
	}
	/**
	 * 显示设置设备对话框
	 * @param context
	 * @param listener
	 */
	public static void showSetDeviceDialog(final Context context,final AlertDialogChooseStatusListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		setDeviceDialog = builder.create();
		View dialogView = View.inflate(context, R.layout.dialog_set_device, null);
		setDeviceDialog.setView(dialogView, 0, 0, 0, 0);
		setDeviceDialog.show();
		//初始化控件
		LinearLayout llUnbind = (LinearLayout) setDeviceDialog.findViewById(R.id.ll_unbind);
		LinearLayout llRename = (LinearLayout) setDeviceDialog.findViewById(R.id.ll_rename);
		final TextView tvUnbind = (TextView) setDeviceDialog.findViewById(R.id.tv_unbind);
		final TextView tvRename = (TextView) setDeviceDialog.findViewById(R.id.tv_rename);
		final LinearLayout llUnbindConfirm = (LinearLayout) setDeviceDialog.findViewById(R.id.ll_unbind_confirm);
		final LinearLayout llRenameConfirm = (LinearLayout) setDeviceDialog.findViewById(R.id.ll_rename_confirm);
		final Button btnUnbindOK = (Button) setDeviceDialog.findViewById(R.id.btn_unbind_ok);
		final Button btnUnbindCancel = (Button) setDeviceDialog.findViewById(R.id.btn_unbind_cancel);
		final Button btnRenameCancel = (Button) setDeviceDialog.findViewById(R.id.btn_rename_cancel);
		final Button btnRenamedOK = (Button) setDeviceDialog.findViewById(R.id.btn_rename_ok);
		final EditText etRename = (EditText) setDeviceDialog.findViewById(R.id.et_rename);
		
		
		llUnbind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAnimation.fromUpToDownAnim(context, tvUnbind);
				tvUnbind.setVisibility(View.GONE);
				MyAnimation.fromDownToUpAnim(context, llUnbindConfirm);
				llUnbindConfirm.setVisibility(View.VISIBLE);
			}
		});
		llRename.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAnimation.fromUpToDownAnim(context, tvRename);
				tvRename.setVisibility(View.GONE);
				MyAnimation.fromDownToUpAnim(context, llRenameConfirm);
				llRenameConfirm.setVisibility(View.VISIBLE);
			}
		});
		btnUnbindOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.choosePositiveButton();
			}
		});
		btnUnbindCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAnimation.fromDownToUpAnim(context, tvUnbind);
				tvUnbind.setVisibility(View.VISIBLE);
				MyAnimation.fromUpToDownAnim(context, llUnbindConfirm);
				llUnbindConfirm.setVisibility(View.GONE);
			}
		});
		btnRenamedOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String newName = etRename.getText().toString().trim();
				if(TextUtils.isEmpty(newName)){
					MyToast.showToast(context, R.string.input_no_null);
				}else{
					listener.choosePositiveButton(newName);
				}
			}
		});
		btnRenameCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAnimation.fromDownToUpAnim(context, tvRename);
				tvRename.setVisibility(View.VISIBLE);
				MyAnimation.fromUpToDownAnim(context, llRenameConfirm);
				llRenameConfirm.setVisibility(View.GONE);
			}
		});
	}
	public static void dimissSetDeviceDialog(){
		if(setDeviceDialog != null){
			setDeviceDialog.dismiss();
		}
	}
	
	public static void showLastTimePwdDailog(Context context,String pwd){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		View dialogView = View.inflate(context, R.layout.dialog_show_pwd, null);
		dialog.setView(dialogView, 0, 0, 0, 0);
		dialog.show();
		TextView tvShow = (TextView) dialogView.findViewById(R.id.tv_last_time_pwd);
		tvShow.setText(pwd);
		//改变mProgressDialog参数
		Point size = new Point();
		dialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
		int width = size.x;
		int height = size.y;
		LayoutParams params = dialog.getWindow().getAttributes();
		params.alpha = 1f; //进度条背景透明度
		params.dimAmount = 0f;//整屏亮暗设置(0f-1f):0f为不变，1f变黑
		params.width = 3*width/4;
		params.height = height/6;
		dialog.getWindow().setAttributes(params);
	}
	
}
