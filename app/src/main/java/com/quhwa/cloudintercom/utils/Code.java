package com.quhwa.cloudintercom.utils;
/**
 * 服务器返回Code类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class Code {
	// 接口返回值
	// 成功
	public static final int RETURN_FAILD = 0;
	public static final int RETURN_SUCCESS = 1;
	public static final int RETURN_PASSWD_ERROR = 5;
	public static final int RETURN_NOT_EXIST = 6;
	public static final int RETURN_ALREADY_EXIST = 7;
	public static final int RETURN_USER_OR_PASSWD_ERROR = 8;
	public static final int RETURN_RESULT = 9;
	public static final int RETURN_INPUT_IS_NULL = 10;
	public static final int RETURN_USERNAME_OR_PASSWD_IS_NULL = 11;
	public static final int RETURN_USERNAME_NOT_EXIST = 12;
	public static final int RETURN_MOBILE_ALREADY_EXIST = 13;
	
	//跳转请求码
	public static final int LOGINACTIVITY_TO_REGISTERACTIVITY_REQUESTCODE = 20;
	public static final int MYFRAGMENT_TO_LOGINACTIVITY_REQUESTCODE = 21;
}
