package com.quhwa.cloudintercom.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Produce implements Serializable{
	private String proName;
	private double proPrice;
	private String proDetail;
	private Drawable proPicture;
	private int imageRes;
	private String proTitle;
	private String shopName;
	private String shopPhone;
	
	public Produce() {
		super();
	}
	public Produce(String proName, double proPrice, String proDetail,
			Drawable proPicture,int imageRes, String proTitle,String shopName,String shopPhone) {
		super();
		this.proName = proName;
		this.proPrice = proPrice;
		this.proDetail = proDetail;
		this.proPicture = proPicture;
		this.proTitle = proTitle;
		this.imageRes = imageRes;
		this.shopPhone = shopPhone;
		this.shopName = shopName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopPhone() {
		return shopPhone;
	}

	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

	public int getImageRes() {
		return imageRes;
	}

	public void setImageRes(int imageRes) {
		this.imageRes = imageRes;
	}

	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public double getProPrice() {
		return proPrice;
	}
	public void setProPrice(double proPrice) {
		this.proPrice = proPrice;
	}
	public String getProDetail() {
		return proDetail;
	}
	public void setProDetail(String proDetail) {
		this.proDetail = proDetail;
	}
	public Drawable getProPicture() {
		return proPicture;
	}
	public void setProPicture(Drawable proPicture) {
		this.proPicture = proPicture;
	}
	public String getProTitle() {
		return proTitle;
	}
	public void setProTitle(String proTitle) {
		this.proTitle = proTitle;
	}

	@Override
	public String toString() {
		return "Produce{" +
				"proName='" + proName + '\'' +
				", proPrice=" + proPrice +
				", proDetail='" + proDetail + '\'' +
				", proPicture=" + proPicture +
				", imageRes=" + imageRes +
				", proTitle='" + proTitle + '\'' +
				", shopName='" + shopName + '\'' +
				", shopPhone='" + shopPhone + '\'' +
				'}';
	}
}
