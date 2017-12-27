package com.quhwa.cloudintercom.bean;

public class Advertisement {
	private String introduce;
	private int icon;
	private String imagePath;
	
	public Advertisement(String introduce, int icon, String imagePath) {
		super();
		this.introduce = introduce;
		this.icon = icon;
		this.imagePath = imagePath;
	}
	
	public Advertisement(String introduce, int icon) {
		super();
		this.introduce = introduce;
		this.icon = icon;
	}
	
	public Advertisement(String introduce) {
		super();
		this.introduce = introduce;
	}

	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	@Override
	public String toString() {
		return "Advertisement [introduce=" + introduce + ", iconDot=" + icon
				+ ", imagePath=" + imagePath + "]";
	}
	
}
