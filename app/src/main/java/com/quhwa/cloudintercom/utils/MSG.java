package com.quhwa.cloudintercom.utils;
/**
 * handler消息类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class MSG {
	/**注册成功消息*/
	public static final int REGISTER_SUCCESS = 0;
	/**登陆成功消息*/
	public static final int LOGIN_SUCCESS = 1;
	/**服务器异常消息*/
	public static final int SERVER_EXCEPTION = 2;
	/**通知开始注册到sip服务器的消息*/
	public final static int MSG_REGISTER_SIP_SERVER = 3;
	/**通知发送sipId和token到室内机的消息*/
	public final static int MSG_SEND_SIPID_AND_TOKEN_TO_INDOOR = 4;
	/**获取个人消息：成功返回json数据*/
	public final static int PERSONAL_MSG_SUCCESS = 5;
	/**没有网络*/
	public final static int NO_NET_MSG = 6;
	/**删除信息：成功返回json数据*/
	public final static int DELETE_MSG_SUCCESS = 7;
	/**绑定设备上传数据成功返回消息*/
	public static final int BOUND_UPLOAD_DATA_SUCCESS = 8;
	/**解绑设备上传数据成功返回消息*/
	public static final int UNBOUND_SUCCESS = 9;
	/**查询设备列表成功返回消息*/
	public static final int QUERY_DEVICE_LIST_SUCCESS = 10;
	/**修改设备别名成功返回消息*/
	public static final int ALTER_DEVICE_ALIAS_SUCCESS = 11;
	/**修改免打扰状态返回消息*/
	public static final int ALTER_DISTURB_STATUS_SUCCESS = 12;
	/**发送token返回消息*/
	public static final int SEND_TOKEN_SUCCESS = 13;
	/**删除token返回消息*/
	public static final int DELETE_TOKEN_SUCCESS = 14;

}
