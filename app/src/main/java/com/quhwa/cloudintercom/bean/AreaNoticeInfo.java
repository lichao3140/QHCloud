package com.quhwa.cloudintercom.bean;

public class AreaNoticeInfo {
	private String title;
	private String content;
	private String time;
	
	public AreaNoticeInfo(String title, String content, String time) {
		super();
		this.title = title;
		this.content = content;
		this.time = time;
	}
	
	public AreaNoticeInfo(String title, String time) {
		super();
		this.title = title;
		this.time = time;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "AreaNoticeInfo [title=" + title + ", content=" + content
				+ ", time=" + time + "]";
	}
	
	
}
