package com.quhwa.cloudintercom.utils;

public class ReceiverAction {
	/**开锁成功发送广播的Action*/
	public final static String ACTION_UNLOCK_SUCCESS = "action.unlock.success";
	/**开锁失败发送广播的Action*/
	public final static String ACTION_UNLOCK_FAIL = "action.unlock.fail";
	/**发给室内机sipId成功发送广播的Action*/
	public final static String ACTION_BIND_SUCCESS = "action.bind.success";
	/**发给室内机sipId失败发送广播的Action*/
	public final static String ACTION_BIND_FAIL = "action.bind.fail";
	/**开锁密码成功发送广播的Action*/
	public final static String ACTION_UNLOCK_PASSWORD_SUCCESS = "action.send.unlock.password.success";
	/**开锁密码失败发送广播的Action*/
	public final static String ACTION_UNLOCK_PASSWORD_FAIL = "action.send.unlock.password.fail";
	/**访客密码成功发送广播的Action*/
	public final static String ACTION_VISITOR_PASSWORD_SUCCESS = "action.send.visitor.password.success";
	/**访客密码失败发送广播的Action*/
	public final static String ACTION_VISITOR_PASSWORD_FAIL = "action.send.visitor.password.fail";
	/**屏蔽来电通知成功发送广播的Action*/
	public final static String ACTION_INCOMING_SHIELD_SUCCESS = "action.send.incoming.shield.success";
	/**屏蔽来电通知发送广播的Action*/
	public final static String ACTION_INCOMING_SHIELD_FAIL = "action.send.incoming.shield.fail";
	/**收到解绑设备通知发送广播的Action*/
	public final static String ACTION_UNBIND_DEVICE_MSG = "action.receive.unbind.device.msg";
	/**解绑设备通知成功发送广播的Action*/
	public final static String ACTION_UNBIND_DEVICE_FROM_PHONE_SUCCESS = "action.send.bind.device.success";
	/**解绑设备通知失败发送广播的Action*/
	public final static String ACTION_UNBIND_DEVICE_FROM_PHONE_FAIL = "action.send.bind.device.fail";
}
