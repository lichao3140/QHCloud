package com.quhwa.cloudintercom.bean;

import java.io.Serializable;
/**
 * 用户信息实体类，用户信息表
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	
	private String username;
	
	private String password;
	
	private String confirmPassword;
	
	private String type = "2";
	
	private int status;
	
	private String mobile;
	
	private String email;
	
	private long createTime;
	
	private String updateTime;
	
	private String remark;
	
	private String sessionKey;
	
	private String sipid;
	
	private String sipPasswd;
	
	public UserInfo(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public UserInfo(String username, String password, String confirmPassword) {
		super();
		this.username = username;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
	
	
	
	public String getSipid() {
		return sipid;
	}

	public void setSipid(String sipid) {
		this.sipid = sipid;
	}

	public String getSipPasswd() {
		return sipPasswd;
	}

	public void setSipPasswd(String sipPasswd) {
		this.sipPasswd = sipPasswd;
	}

	public UserInfo() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", username=" + username + ", password="
				+ password + ", confirmPassword=" + confirmPassword + ", type="
				+ type + ", status=" + status + ", mobile=" + mobile
				+ ", email=" + email + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", remark=" + remark
				+ ", sessionKey=" + sessionKey + ", sipid=" + sipid
				+ ", sipPasswd=" + sipPasswd + "]";
	}

	
	
}
