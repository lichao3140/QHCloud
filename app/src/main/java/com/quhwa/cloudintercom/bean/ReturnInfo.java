package com.quhwa.cloudintercom.bean;

public class ReturnInfo {
	private String kind;
	private String type;
	private String msg;
	private String ack;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAck() {
		return ack;
	}
	public void setAck(String ack) {
		this.ack = ack;
	}
	@Override
	public String toString() {
		return "ReturnInfo [kind=" + kind + ", type=" + type + ", msg=" + msg
				+ ", ack=" + ack + "]";
	}
	
}
