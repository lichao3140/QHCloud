package com.quhwa.cloudintercom.utils;

public class SipMsgType {
	/**开锁类型*/
	public final static String OPEN_DOOR_TYPE= "01";
	/**开锁状态:开*/
	public final static String GATE_CART_ENABLE= "1";
	/**来电和接通状态开锁状态:开*/
	public final static String INCOMING_GATE_CART_ENABLE= "2";
	/**发送sipId和token类型*/
	public final static String SEND_SIPID_AND_TOKEN_TYPE= "03";
	/**发送开锁密码类型*/
	public final static String SEND_UNLOCK_PWD_TYPE= "04";
	/**发送访客密码类型*/
	public final static String SEND_VISITOR_PWD_TYPE= "05";
	/**发送屏蔽来电通知类型*/
	public final static String SEND_INCOMING_SHIELD_TYPE= "06";
	/**收到解绑设备通知类型*/
	public final static String RECEIVE_UNBIND_DEVICE_TYPE= "07";
	/**手机类型:1为Android手机*/
	public final static String MOBILETYPE = "1";
}
