package com.quhwa.cloudintercom.db;

import android.bluetooth.BluetoothClass;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据库管理类
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class DBManager {
	private static String Tag = "DBManager";
	/*--------------------------------------设备列表操作---------------------------------------------*/
	/**
	 * 向设备列表插入一条数据
	 * @param context
	 * @param device
	 * @return true：插入成功    false：插入失败
	 */
	public static void insertDeviceList(Context context, BoundResult.Device device){
		MyLog.print(Tag, "device保存数:"+device.toString(), MyLog.PRINT_RED);
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		ContentValues cv = new ContentValues();
		//cv.put(Table.DEVICELIST_COLUMN_MAC, device.getMac());
		cv.put(Table.DEVICELIST_COLUMN_ID,device.getDeviceId());
		cv.put(Table.DEVICELIST_COLUMN_USERNAME, device.getUsername());
		cv.put(Table.DEVICELIST_COLUMN_SIPID, device.getSipid());
		cv.put(Table.DEVICELIST_COLUMN_ROOMNUM, device.getRoomNo());
		//cv.put(Table.DEVICELIST_COLUMN_EXTENSIONNUM, device.getExtensionNum());
		cv.put(Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS,device.getShieldStatus());
		cv.put(Table.DEVICELIST_COLUMN_DEVICE_NAME,device.getDeviceName());
		cv.put(Table.DEVICELIST_COLUMN_DEVICE_ALIAS,device.getDeviceAlias());
		cv.put(Table.DEVICELIST_COLUMN_DEVICECODE,device.getDeviceCode());
		cv.put(Table.DEVICELIST_COLUMN_DOOR_DEVICE_STR,device.getUnitDoorNo());
		if(db != null){
			long row = db.insert(Table.DEVICELIST_TABLE_NAME, null, cv);
			db.close();
			if(row >= 0){
				MyLog.print(Tag, "数据插入成功", MyLog.PRINT_RED);
			}else{
				MyLog.print(Tag, "数据插入失败", MyLog.PRINT_RED);
			}
		}
	}
	/**
	 * 根据房号更新sipId和Mac地址
	 * @param context
	 * @param device
	 * @return true：更新成功    false：更新失败
	 */
	public static boolean updateMacAndSipIdByRoomNum(Context context,Device device){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		if(db != null){
			ContentValues cv = new ContentValues();
			cv.put(Table.DEVICELIST_COLUMN_MAC, device.getDeviceMac());
			cv.put(Table.DEVICELIST_COLUMN_SIPID, device.getSipid());
			int row = db.update(Table.DEVICELIST_TABLE_NAME, cv, Table.DEVICELIST_COLUMN_ROOMNUM+"=?",new String[]{device.getRoomNo()});
			db.close();
			if(row > 0){
				MyLog.print(Tag, "数据更新成功", MyLog.PRINT_RED);
				return true;
			}else{
				MyLog.print(Tag, "数据更新失败", MyLog.PRINT_RED);
				return false;
			}
		}
		return false;
	}
	/**
	 * 根据房号更新shieldStatus
	 * @param context
	 * @param device
	 * @return true：更新成功    false：更新失败
	 */
	public static boolean updateShieldStatusByRoomNum(Context context,Device device){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		if(db != null){
			ContentValues cv = new ContentValues();
			cv.put(Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS, device.getShieldStatus());
			int row = db.update(Table.DEVICELIST_TABLE_NAME, cv, Table.DEVICELIST_COLUMN_ROOMNUM+"=?",new String[]{device.getRoomNo()});
			db.close();
			if(row > 0){
				MyLog.print(Tag, "数据更新成功", MyLog.PRINT_RED);
				return true;
			}else{
				MyLog.print(Tag, "数据更新失败", MyLog.PRINT_RED);
				return false;
			}
		}
		return false;
	}

	/**
	 * 查询 key = value，数据库中是否存在此条数据
	 * @param context
	 * @param key
	 * @param value
	 * @return true：存在  false 不存在
	 */
	public static boolean getQueryByWhere(Context context,String key,String value){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		List<Device> devices = new ArrayList<Device>();
		if(db != null){
			Cursor cursor = db.query(Table.DEVICELIST_TABLE_NAME, null, key+"=?", new String[]{value}, null, null, null);
			while (cursor.moveToNext()) {
				Device device = new Device();
				device.setDeviceMac(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_MAC)));
				device.setUsername(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_USERNAME)));
				device.setSipid(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_SIPID)));
				device.setRoomNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ROOMNUM)));
				//device.setUnitDoorNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_EXTENSIONNUM)));
				device.setDeviceName(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_NAME)));
				device.setDeviceAlias(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_ALIAS)));
				device.setShieldStatus(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS)));
				device.setUnitDoorNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DOOR_DEVICE_STR)));
				devices.add(device);
			}
			//cursor.close();
			if(devices != null && devices.size()>0){
				MyLog.print(Tag, "字段为"+key+"="+value+"在数据库中已有存在数据", MyLog.PRINT_RED);
				return true;
			}else{
				MyLog.print(Tag, "字段为"+key+"="+value+"在数据库中没有数据", MyLog.PRINT_RED);
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * 根据房号和用户名查询设备列表
	 * @param context
	 * @param key
	 * @param value
	 * @param username_key
	 * @param username_value
	 * @return
	 */
	public static boolean getQueryByWhereRoomNumAndUsername(Context context,String key,String value,String username_key,String username_value){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		List<Device> devices = new ArrayList<Device>();
		if(db != null){
			Cursor cursor = db.query(Table.DEVICELIST_TABLE_NAME, null, key+"=?"+" and "+username_key+"=?", new String[]{value,username_value}, null, null, null);
			while (cursor.moveToNext()) {
				Device device = new Device();
				device.setDeviceId(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ID)));
				device.setDeviceMac(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_MAC)));
				device.setUsername(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_USERNAME)));
				device.setSipid(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_SIPID)));
				device.setRoomNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ROOMNUM)));
				//device.setExtensionNum(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_EXTENSIONNUM)));
				device.setDeviceName(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_NAME)));
				device.setDeviceAlias(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_ALIAS)));
				device.setShieldStatus(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS)));
				device.setUnitDoorNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DOOR_DEVICE_STR)));
				devices.add(device);
			}
			cursor.close();
			if(devices != null && devices.size()>0){
				MyLog.print(Tag, "字段为"+key+"="+value+"在数据库中已有存在数据", MyLog.PRINT_RED);
//				MyLog.print(Tag, "用户11"+value+"的数据"+devices.toString(), MyLog.PRINT_RED);
				return true;
			}else{
				MyLog.print(Tag, "字段为"+key+"="+value+"在数据库中没有数据", MyLog.PRINT_RED);
				return false;
			}

		}
		return false;
	}
	/**
	 * 根据房号和用户名查询设备列表
	 * @param context
	 * @param key
	 * @param value
	 * @param username_key
	 * @param username_value
	 * @return
	 */
	public static List<Device> getQueryByWhereRoomNumAndUsername1(Context context,String key,String value,String username_key,String username_value){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		List<Device> devices = new ArrayList<Device>();
		if(db != null){
			Cursor cursor = db.query(Table.DEVICELIST_TABLE_NAME, null, key+"=?"+" and "+username_key+"=?", new String[]{value,username_value}, null, null, null);
			while (cursor.moveToNext()) {
				Device device = new Device();
				device.setDeviceMac(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_MAC)));
				device.setUsername(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_USERNAME)));
				device.setSipid(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_SIPID)));
				device.setRoomNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ROOMNUM)));
				//device.setExtensionNum(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_EXTENSIONNUM)));
				device.setDeviceName(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_NAME)));
				device.setDeviceAlias(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_ALIAS)));
				device.setShieldStatus(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS)));
				device.setDeviceCode(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICECODE)));
				device.setUnitDoorNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DOOR_DEVICE_STR)));
				devices.add(device);
			}
			return devices;
		}
		return devices;
	}
	/**
	 * 根据用户名查询设备列表
	 * @param context
	 * @param key_username
	 * @param value_username
	 * @return
	 */
	public static ArrayList<BoundResult.Device> queryByUsername(Context context,String key_username,String value_username){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		ArrayList<BoundResult.Device> devices = new ArrayList<BoundResult.Device>();
		if(db != null){
			Cursor cursor = db.query(Table.DEVICELIST_TABLE_NAME, null, key_username+"=?", new String[]{value_username}, null, null, null);
			while (cursor.moveToNext()) {
				BoundResult.Device device = new BoundResult.Device();
				device.setDeviceId(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ID)));
				device.setDeviceMac(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_MAC)));
				device.setUsername(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_USERNAME)));
				device.setSipid(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_SIPID)));
				device.setRoomNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ROOMNUM)));
				//device.setExtensionNum(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_EXTENSIONNUM)));
				device.setDeviceName(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_NAME)));
				device.setDeviceAlias(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_ALIAS)));
				device.setShieldStatus(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS)));
				device.setDeviceCode(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICECODE)));
				device.setUnitDoorNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DOOR_DEVICE_STR)));
				devices.add(device);
			}
		}
//		MyLog.print(Tag, "用户"+value_username+"的数据"+devices.toString(), MyLog.PRINT_RED);
		return devices;
	}
	
	/**
	 * 查询设备列表所有
	 * @param context
	 * @return 设备列表集合
	 */
	public static List<Device> queryAll(Context context){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		List<Device> devices = new ArrayList<Device>();
		if(db != null){
			Cursor cursor = db.query(Table.DEVICELIST_TABLE_NAME, new String[]{
					Table.DEVICELIST_COLUMN_ID,
					Table.DEVICELIST_COLUMN_MAC,
					Table.DEVICELIST_COLUMN_SIPID,
					Table.DEVICELIST_COLUMN_ROOMNUM,
					Table.DEVICELIST_COLUMN_EXTENSIONNUM,
					Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS,
					Table.DEVICELIST_COLUMN_DEVICE_NAME,
					Table.DEVICELIST_COLUMN_DEVICE_ALIAS,
					Table.DEVICELIST_COLUMN_DOOR_DEVICE_STR,
					Table.DEVICELIST_COLUMN_DEVICECODE
			}, null, null, null, null, null);
			while (cursor.moveToNext()) {
				Device device = new Device();
				device.setDeviceId(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ID)));
				device.setDeviceMac(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_MAC)));
				device.setSipid(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_SIPID)));
				device.setRoomNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_ROOMNUM)));
				//device.setExtensionNum(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_EXTENSIONNUM)));
				device.setDeviceName(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_NAME)));
				device.setDeviceAlias(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICE_ALIAS)));
				device.setShieldStatus(cursor.getInt(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS)));
				device.setDeviceCode(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DEVICECODE)));
				device.setUnitDoorNo(cursor.getString(cursor.getColumnIndex(Table.DEVICELIST_COLUMN_DOOR_DEVICE_STR)));
				devices.add(device);
			}
		}
		return devices;
	}
	
	/**
	 * 根据用户名和另一个字段删除数据
	 * @param context 上下文对象
	 * @param key 要删除的字段(房号)
	 * @param value 要删除的字段值(房号)
	 * @param username_key 用户名字段
	 * @param username_value 用户名值
	 * @return
	 */
	public static ArrayList<BoundResult.Device> deleteDevicByRoomNumAndUsername(Context context,String key,String value,String username_key,String username_value){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		ArrayList<BoundResult.Device> devices = null;
		if(db != null){
			int row = db.delete(Table.DEVICELIST_TABLE_NAME, key+"=?"+" and "+username_key+"=?", new String[]{value,username_value});
			if(row == 0){
				MyLog.print(Tag, "删除失败", MyLog.PRINT_RED);
			}else{
				MyLog.print(Tag, "删除成功", MyLog.PRINT_RED);
			}
			devices = queryByUsername(context, username_key, username_value);
		}
		return devices;
	}
	
	public static void updateDeviceNameByUserAndRoomNum(Context context,String newDeviceName,BoundResult.Device device){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		if(db != null){
			ContentValues cv = new ContentValues();
			cv.put(Table.DEVICELIST_COLUMN_DEVICE_ALIAS, newDeviceName);
//			int row = db.update(Table.DEVICELIST_TABLE_NAME, cv, Table.DEVICELIST_COLUMN_USERNAME+"=?"+" and "+Table.DEVICELIST_COLUMN_ROOMNUM+"=?",new String[]{device.getUsername(),device.getRoomNo()});
			int row = db.update(Table.DEVICELIST_TABLE_NAME, cv, Table.DEVICELIST_COLUMN_USERNAME+"=?"+" and "+Table.DEVICELIST_COLUMN_ID+"=?",new String[]{MySharedPreferenceManager.getUsername(),device.getDeviceId()+""});
//			ArrayList<BoundResult.Device> aa = queryByUsername(MyApplication.instance,Table.DEVICELIST_COLUMN_USERNAME,device.getUsername());
			db.close();
			MyLog.print(Tag, "userName:"+device.getUsername(), MyLog.PRINT_RED);
			if(row > 0){
				MyLog.print(Tag, "数据更新成功", MyLog.PRINT_RED);
			}else{
				MyLog.print(Tag, "数据更新失败", MyLog.PRINT_RED);
			}
		}
	}
	
	/**
	 * 根据用户清除数据
	 * @param context
	 * @param username
	 * @return
	 */
	public static boolean deleteDevicesByUser(Context context,String username){
		SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
		if(db != null){
			int row = db.delete(Table.DEVICELIST_TABLE_NAME, Table.DEVICELIST_COLUMN_USERNAME + "=?", new String[]{username});
			if(row > 0){
				MyLog.print(Tag, "清除数据成功", MyLog.PRINT_RED);
				return true;
			}else{
				MyLog.print(Tag, "清除数据失败", MyLog.PRINT_RED);
				return false;
			}
		}
		return false;
	}
	
	/*------------------------------------------------------------------------------------------------------------------------*/
	
	
	
}
