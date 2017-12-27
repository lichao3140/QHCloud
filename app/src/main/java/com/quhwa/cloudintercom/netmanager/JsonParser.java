package com.quhwa.cloudintercom.netmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.quhwa.cloudintercom.bean.BoundResult;

/**
 * json解析类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class JsonParser {
	private Gson gson;

	public JsonParser() {
		gson = new Gson();
	}

	/**
	 * 解析json对象
	 * @param jsonString
	 * @param cls
	 * @param <T>
     * @return
     */
	public <T> T getObject(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return t;
	}

	/**
	 * 解析json数组
	 * @param jsonString
	 * @param cls
	 * @param <T>
     * @return List集合
     */
	public <T> List<T> getObjectList(String jsonString, Class<T> cls) {
		List<T> list;
		try {
			list = (List) gson.fromJson(jsonString, new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return list;
	}
	/**
	 * 解析json数组
	 * @param jsonString
	 * @param cls
     * @return List集合
     */
	public  List<BoundResult.Device> getObjectList1(String jsonString, Class<BoundResult.Device> cls) {
		List<BoundResult.Device> list;
		try {
			list = (List) gson.fromJson(jsonString, new TypeToken<List<BoundResult.Device>>() {
			}.getType());
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return list;
	}

	/**
	 * 解析json数组
	 * @param jsonString
	 * @return Map集合
     */
	public List<Map<String, Object>> listKeyMaps(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = (List) gson.fromJson(jsonString, new TypeToken() {
			}.getType());
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return list;
	}
}
