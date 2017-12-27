package com.quhwa.cloudintercom.bean;
/**
 * 服务器返回结果类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class Result {
	private int code;
	private String message;
	private UserInfo data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserInfo getUserInfo() {
		return data;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.data = userInfo;
	}
	@Override
	public String toString() {
		return "Result [code=" + code + ", message=" + message + ", data="
				+ data + "]";
	}
	
}
