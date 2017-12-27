package com.quhwa.cloudintercom.utils;

public class Encrypt {
	public final static String CHARSET = "UTF-8";
	static{	
		System.loadLibrary("encrypt");
	}
	public native static byte[] enBase(byte[] buf);
    public native static byte[] deBase(byte[] buf);
}
