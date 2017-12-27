package com.quhwa.cloudintercom.utils;

import com.quhwa.cloudintercom.app.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	/**
	 * 读取config.properties配置文件key对应的value值
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		try {
			Properties properties = new Properties();
//			InputStream in = PropertiesUtils.class.getResourceAsStream("src/config.properties");
			InputStream in = MyApplication.instance.getAssets().open("config.properties");
			properties.load(in);
			return (String) properties.get(key);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
