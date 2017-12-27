package org.pjsip.pjsua2.app;
/**
 * pjsip消息常量类
 *
 * @author lxz
 * @date 2017年5月9日
 */
public class MSG_TYPE {
	/**来电消息*/
	public final static int INCOMING_CALL = 1;
	/**call状态消息*/
	public final static int CALL_STATE = 2;
	/**注册状态消息*/
	public final static int REG_STATE = 3;
	
	public final static int BUDDY_STATE = 4;
	/**call音视频消息*/
	public final static int CALL_MEDIA_STATE = 5;
	/**远程信息消息*/
	public final static int INSTANT_MESSAGE = 6;
	/**接听状态和来电状态开锁的返回消息*/
	public final static int MSG_CALL_OR_DIRECTOR_UNLOCK = 7;
	/**通知发送sipId到室内机的返回消息*/
	public final static int MSG_BIND = 8;
	/**注册失败消息*/
	public final static int REG_STATE_FAIL = 9;
	/**发送修改开锁密码的返回消息*/
	public final static int MSG_SEND_UNLOCK_PASSWORD = 10;
	/**发送访客密码的返回消息*/
	public final static int MSG_SEND_VISITOR_PASSWORD = 11;
	/**发送屏蔽来电通知返回消息*/
	public final static int MSG_SEND_INCOMING_SHIELD = 12;
	/**发送解绑设备通知返回消息*/
	public final static int MSG_SEND_UNBIND_DEVICE = 13;
	
}
