package com.quhwa.cloudintercom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.BindDeviceActivity;
import com.quhwa.cloudintercom.activity.SettingActivity;
import com.quhwa.cloudintercom.activity.VisitorPwdActivity;
import com.quhwa.cloudintercom.adapter.ShieldIncomingAdapter;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.presenter.SettingPresenter;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.NotifyManager;
import com.quhwa.cloudintercom.utils.ReceiverAction;
import com.quhwa.cloudintercom.utils.SoundManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.utils.TimerUtils;
import com.quhwa.cloudintercom.view.ISettingView;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

public class HandleSipMsgReceiver extends BroadcastReceiver {

	private String Tag = "HandleSipMsgReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		MyLog.print(Tag , "收到sipmsg广播", MyLog.PRINT_RED);
		String action = intent.getAction();
		//直接开锁sip返回成功消息
		if(action.equals(ReceiverAction.ACTION_UNLOCK_SUCCESS)){
			MyLog.print(Tag, "收到发送密码成功返回消息", MyLog.PRINT_RED);
			MyToast.showToast(context, R.string.unlock_success);
			MyDialog.dismissDialog();
			SoundManager.play(context, "open_door_success_audio_name");
		}
		//直接开锁sip返回失败消息
		if(action.equals(ReceiverAction.ACTION_UNLOCK_FAIL)){
			MyLog.print(Tag, "收到发送密码失败返回消息", MyLog.PRINT_RED);
			MyToast.showToast(context, R.string.unlock_fail);
			MyDialog.dismissDialog();
			SoundManager.play(context, "open_door_fail_audio_name");
		}
		//绑定设备sip返回成功消息
		if(action.equals(ReceiverAction.ACTION_BIND_SUCCESS)){
			MyLog.print(Tag, "收到绑定设备成功返回消息", MyLog.PRINT_RED);
			TimerUtils.stopToSend();
			BindDeviceActivity.deviceDataPresenter.sipMessageReturnSuccess();
//			MyDialog.dismissDialog();
//			SoundManager.play(context, "bind_device_success_audio_name");
		}
		//绑定设备sip返回失败消息
		if(action.equals(ReceiverAction.ACTION_BIND_FAIL)){
			MyLog.print(Tag, "收到绑定设备失败返回消息", MyLog.PRINT_RED);
			TimerUtils.stopToSend();
			BindDeviceActivity.deviceDataPresenter.sipMessageReturnFail();
//			MyDialog.dismissDialog();
//			SoundManager.play(context, "bind_divice_fail_audio_name");
		}
		//修改开锁密码sip返回成功消息
		if(action.equals(ReceiverAction.ACTION_UNLOCK_PASSWORD_SUCCESS)){
			MyLog.print(Tag, "收到发送开锁密码成功返回消息", MyLog.PRINT_RED);
			MyToast.showToast(context, R.string.modify_success);
			MyDialog.dismissDialog();
		}
		//修改开锁密码sip返回失败消息
		if(action.equals(ReceiverAction.ACTION_UNLOCK_PASSWORD_FAIL)){
			MyLog.print(Tag, "收到发送开锁密码失败返回消息", MyLog.PRINT_RED);
			MyToast.showToast(context, R.string.modify_fail);
			MyDialog.dismissDialog();
		}
		//发送访客密码sip返回成功消息
		if(action.equals(ReceiverAction.ACTION_VISITOR_PASSWORD_SUCCESS)){
			MyLog.print(Tag, "收到发送访客密码成功返回消息", MyLog.PRINT_RED);
			VisitorPwdActivity.visitorPwdPresenter.sipMessageReturnSuccess();
		}
		//发送访客密码sip返回失败消息
		if(action.equals(ReceiverAction.ACTION_VISITOR_PASSWORD_FAIL)){
			MyLog.print(Tag, "收到发送访客密码失败返回消息", MyLog.PRINT_RED);
			VisitorPwdActivity.visitorPwdPresenter.sipMessageReturnFail();
		}
		//发送屏蔽来电通知sip返回成功消息
		if(action.equals(ReceiverAction.ACTION_INCOMING_SHIELD_SUCCESS)){
			MyLog.print(Tag, "收到发送屏蔽来电通知成功返回消息", MyLog.PRINT_RED);
//			new MyDialog().sipMessageReturnSuccess();
			SettingActivity.adapter.sipMessageReturnSuccess();
		}
		//发送屏蔽来电通知sip返回失败消息
		if(action.equals(ReceiverAction.ACTION_INCOMING_SHIELD_FAIL)){
			MyLog.print(Tag, "收到发送屏蔽来电通知失败返回消息", MyLog.PRINT_RED);
//			new MyDialog().sipMessageReturnFail();
//			SettingActivity.settingPresenter.sipMessageReturnFail();
			SettingActivity.adapter.sipMessageReturnFail();
		}
		//收到室内机解绑手机设备消息
		if(action.equals(ReceiverAction.ACTION_UNBIND_DEVICE_MSG)){//收到解绑设备消息
			String roomNum = intent.getStringExtra("msg");
			String username = MySharedPreferenceManager.getUsername();
			DBManager.deleteDevicByRoomNumAndUsername(MyApplication.instance, Table.DEVICELIST_COLUMN_ROOMNUM, roomNum,Table.DEVICELIST_COLUMN_USERNAME,username);
			NotifyManager.showNotify("解绑", "该用户与室内机已解绑");
			try {
				if(BindDeviceActivity.instance != null){
					BindDeviceActivity.instance.loadDeviceListAfterUnbind();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//解绑设备sip返回成功消息
		if(action.equals(ReceiverAction.ACTION_UNBIND_DEVICE_FROM_PHONE_SUCCESS)){
			MyLog.print(Tag, "收到发送解绑通知成功返回消息", MyLog.PRINT_RED);
			MyToast.showToast(context, R.string.unbind_success);
//			BindDeviceActivity.deviceDataPresenter.sipMessageReturnSuccess();
//			SoundManager.play(context, "ubind_device_success_audio_name");
			BindDeviceActivity.deviceDataPresenter.sipMessageReturnSuccess();
		}
		//解绑设备sip返回失败消息
		if(action.equals(ReceiverAction.ACTION_UNBIND_DEVICE_FROM_PHONE_FAIL)){
			MyLog.print(Tag, "收到发送解绑通知失败返回消息", MyLog.PRINT_RED);
			MyToast.showToast(context, R.string.unbind_fail);
//			MyDialog.dismissDialog();
//			SoundManager.play(context, "ubind_divice_fail_audio_name");
			BindDeviceActivity.deviceDataPresenter.sipMessageReturnFail();
		}
		
		PJSipService.currentMsgType = 0;//重置为原始状态
		
	}

}
