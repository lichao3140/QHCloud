package com.quhwa.cloudintercom.utils;
/**
 * 常量类，主要是接口
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class Constants {
	/**服务器IP地址*/
//	public static final String SERVER_IP = "192.168.10.10";	//此处为测试地址，请不要提交此地址
	//public static final String SERVER_IP = "120.25.126.228";	//此处为测试地址，请不要提交此地址
	//public static final String SERVER_IP = "192.156.1.200";	//此处为测试地址，请不要提交此地址
	//提交的时候此处不要修改，serverIP默认是亚马逊云服务器
	//public static final String SERVER_IP = "52.42.215.227";		//亚马逊云服务器
	public static final String SERVER_IP = "118.178.131.79";		//阿里云服务器
	/**服务器数据端口*/
	public static final String SERVER_DATA_PORT = "8080";
	//IP和端口号
	public static final String IP_PORT = "http://"+SERVER_IP+":"+SERVER_DATA_PORT;
	//注册接口
	public static final String REGISTER_URL = IP_PORT+ "/smarthomeservice/rest/user/sipRegister";
	//登陆接口
	public static final String LOGIN_URL = IP_PORT+ "/smarthomeservice/rest/user/sipVerify";
	//获取个人和小区消息接口
	public static final String MSG_URL = IP_PORT+ "/smarthomeservice/rest/notice/list";
	//删除个人和小区消息接口
	public static final String DELETE_MSG_URL = IP_PORT+ "/smarthomeservice/rest/notice/del";
	//绑定用户设备接口
	public static final String BOUND_URL = IP_PORT+ "/smarthomeservice/rest/user/device/boundDevice";
	//解绑设备接口
	public static final String UNBOUND_URL = IP_PORT+ "/smarthomeservice/rest/user/device/unbound";
	//查询设备列表接口
	public static final String QUERY_DEVICE_LIST_URL = IP_PORT+ "/smarthomeservice/rest/user/device/queryDeviceList";
	//修改设备别名接口
	public static final String ALTER_DEVICE_ALIAS_URL = IP_PORT+ "/smarthomeservice/rest/device/update";
	//设置免打扰接口
	public static final String DISTURBED_URL = IP_PORT+ "/smarthomeservice/rest/user/device/update";
	//发送极光推送注册ID接口
	public static final String SEND_JPUSH_TOKEN = IP_PORT+"/smarthomeservice/rest/msg/token/add";
	//注销删除token接口
	public static final String DELETE_TOKEN = IP_PORT + "/smarthomeservice/rest/msg/token/del";
}
