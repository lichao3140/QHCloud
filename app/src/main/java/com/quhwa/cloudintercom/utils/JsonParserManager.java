package com.quhwa.cloudintercom.utils;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.quhwa.cloudintercom.bean.ReturnInfo;
import com.quhwa.cloudintercom.bean.TextMsg;

public class JsonParserManager {
	/**
	 * 解析收到文本消息
	 * @param jsonString json格式文本
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getDataFromJson(String jsonString,String value){
		String result = null;
		if(jsonString !=null && jsonString.startsWith("{\"") && jsonString.endsWith("\"}")){
			try{
				JSONObject jb = new JSONObject(jsonString);
				result = jb.getString(value);
				if(result !=null && !"".equals(result.trim())){
					return result.trim();
				}
			}catch(Exception e){
				//e.printStackTrace();
				return null;
			}
		}
		return result;
	}
	public static TextMsg parserJson(String json) throws UnsupportedEncodingException{
		byte[] deBase = Encrypt.deBase(json.getBytes(Encrypt.CHARSET));
		json = new String(deBase, Encrypt.CHARSET);
		TextMsg textMsg = null;
		if(json !=null && json.startsWith("{\"") && json.endsWith("\"}")){
			Gson gson = new Gson();
			textMsg = gson.fromJson(json, TextMsg.class);
			return textMsg;
		}
		return textMsg;
	}
	
	
}






