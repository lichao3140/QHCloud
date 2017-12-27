package com.quhwa.cloudintercom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.SettingActivity;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.bean.ReturnResult;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.model.setting.ISettingModel;
import com.quhwa.cloudintercom.model.setting.SettingModelImpl;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.Code;
import com.quhwa.cloudintercom.utils.ISipMessageReturnListener;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.PopupWindowManager;
import com.quhwa.cloudintercom.utils.SipMsgManager;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

import org.pjsip.pjsua2.app.MSG_TYPE;

import java.util.List;
/**
 * 修改开锁密码设备列表适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class ShieldIncomingAdapter extends BaseAdapter implements ISipMessageReturnListener {
	private List<Device> devices;
	private LayoutInflater inflater;
	private String Tag = "ShieldIncomingAdapter";
	private Context context;
	private int position = 0;
	/**屏蔽来电状态  0:表示取消屏蔽状态  2:表示屏蔽状态*/
	private String currentStatus = null;
	private PopupWindowManager manager;
//	/**屏蔽开关是否开启  true:表示开启状态  false:表示关闭状态*/
//	boolean isClose = true;
	public ShieldIncomingAdapter(Context context, List<Device> devices, PopupWindowManager manager) {
		super();
		this.devices = devices;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.manager = manager;
	}

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_shield_incoming, null);
			holder = new ViewHolder();
			holder.tvRoom = (TextView) convertView.findViewById(R.id.tv_room);
			holder.ivShieldStatus = (ImageView) convertView.findViewById(R.id.iv_shield_status);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final Device device = devices.get(position);
		String roomStr = context.getResources().getString(R.string.room);
		holder.tvRoom.setText(device.getRoomNo().substring(11, 15)+roomStr);
		final String mySipId = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID);
		String deviceStatus = device.getStatus();
		if(deviceStatus.equals("2")){
			holder.ivShieldStatus.setBackgroundResource(R.drawable.ic_shield_open);
		}else if(deviceStatus.equals("0")){
			holder.ivShieldStatus.setBackgroundResource(R.drawable.ic_shield_close);
		}
		holder.ivShieldStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPosition(position);
				PJSipService.currentMsgType = MSG_TYPE.MSG_SEND_INCOMING_SHIELD;//把当前消息设为发送屏蔽来电通知消息
				operationIncomingShieldStatus(device, mySipId);
				MyLog.print(Tag, "向"+device.getSipid()+"发送", MyLog.PRINT_RED);
				MyDialog.showDialog(context, R.string.setting_for_you);
			}
		});
		
		return convertView;
	}


	class ViewHolder{
		TextView tvRoom;
		ImageView ivShieldStatus;
	}
	/**
	 * 设置当前操作item位置
	 * @param position
	 */
	public void setPosition(int position){
		this.position = position;
	}
	/**
	 * 获取当前操作item位置
	 * @return
	 */
	public int getPosition(){
		return position;
	}
	/**
	 * 设置屏蔽来电状态
	 * @param
	 */
	public void setCurrentStatus(String status){
		this.currentStatus = status;
	}
	/**
	 * 获取屏蔽来电状态
	 * @return
	 */
	public String getCurrentStatus(){
		return currentStatus;
	}
	/**
	 * 操作屏蔽来电通知的状态
	 * @param device 对应的室内机对象(封装室内机信息的对象)
	 * @param mySipId 当前用户的sipId
	 */
	public void operationIncomingShieldStatus(Device device,String mySipId){
		if(PJSipService.account != null){
			if(device.getStatus().equals("0")){//非免打扰
				setCurrentStatus("2");
				SipMsgManager.sendInstantMessage(device.getSipid(), SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.SEND_INCOMING_SHIELD_TYPE, mySipId, "2")), PJSipService.account);
				MyLog.print(Tag, "屏蔽", MyLog.PRINT_RED);
			}else{//为免打扰
				setCurrentStatus("0");
				SipMsgManager.sendInstantMessage(device.getSipid(), SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.SEND_INCOMING_SHIELD_TYPE, mySipId, "0")), PJSipService.account);
				MyLog.print(Tag, "取消屏蔽", MyLog.PRINT_RED);
			}
		}else{
			MyToast.showToast(context,R.string.set_fail);
		}
	}
	private ISettingModel iSettingModel = new SettingModelImpl();
	private void setDisturbStatus(BoundResult.Device device,String status) {
		if(iSettingModel != null){
			iSettingModel.setDisturbStatus(new ISettingModel.DisturbStatusListener() {
				@Override
				public void loadDisturbStatusOnComplete(ReturnResult result) {
					if(result.getCode() == Code.RETURN_SUCCESS){
						MyDialog.dismissDialog();
						manager.dismissPopupWindow();
						MyToast.showToast(context,R.string.set_success);
					}else{
						manager.dismissPopupWindow();
						MyToast.showToast(context,R.string.set_fail);
					}
				}

				@Override
				public void onCompleteFail() {
					MyToast.showToast(context,R.string.server_is_busy);
				}

				@Override
				public void onNoNet() {
					MyToast.showToast(context,R.string.no_internet);
				}
			},device,status);
		}
	}

	@Override
	public void sipMessageReturnSuccess() {
		Device device = (Device) getItem(position);
		setDisturbStatus(device,getCurrentStatus());
	}

	@Override
	public void sipMessageReturnFail() {
		MyDialog.dismissDialog();
		MyToast.showToast(context,R.string.set_fail);
	}
}
