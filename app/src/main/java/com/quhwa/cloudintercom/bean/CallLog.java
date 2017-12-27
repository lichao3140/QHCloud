package com.quhwa.cloudintercom.bean;

public class CallLog {
	private int callIcon;
	private String roomNum;
	private String incomingTime;
	private String outGoningTime;
	
	
	
	public CallLog(int callIcon, String roomNum, String incomingTime,
			String outGoningTime) {
		super();
		this.callIcon = callIcon;
		this.roomNum = roomNum;
		this.incomingTime = incomingTime;
		this.outGoningTime = outGoningTime;
	}
	public int getCallIcon() {
		return callIcon;
	}
	public void setCallIcon(int callIcon) {
		this.callIcon = callIcon;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public String getIncomingTime() {
		return incomingTime;
	}
	public void setIncomingTime(String incomingTime) {
		this.incomingTime = incomingTime;
	}
	public String getOutGoningTime() {
		return outGoningTime;
	}
	public void setOutGoningTime(String outGoningTime) {
		this.outGoningTime = outGoningTime;
	}
	@Override
	public String toString() {
		return "CallLog [callIcon=" + callIcon + ", roomNum=" + roomNum
				+ ", incomingTime=" + incomingTime + ", outGoningTime="
				+ outGoningTime + "]";
	}
	
}
