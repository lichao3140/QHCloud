package com.quhwa.cloudintercom.db;
/**
 * 偏好设置本地表名及key名称
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class Table {
	//1、偏好设置表
	/**
	 * 状态表(用户账号、sip账号登陆状态)
	 */
	public static final String TAB_STATUS = "tab_login_status";
	/**
	 * 登陆状态表的key值
	 */
	public static final String TAB_LOGIN_STATUS_KEY = "username_loginStatus";
	/**
	 * sip登陆状态表的key值
	 */
	public static final String TAB_SIP_LOGIN_STATUS_KEY = "sip_loginStatus";
	/**
	 * 用户表（用于保存用户信息）
	 */
	public static final String TAB_USER = "tab_user";
	/**
	 * 用户名
	 */
	public static final String TAB_USER_NAME_KEY = "username";
	/**
	 * 密码
	 */
	public static final String TAB_USER_PASSWORD_KEY = "password";
	/**
	 * sipId
	 */
	public static final String TAB_USER_PASSWORD_SIP_ID = "sipid";
	/**
	 * sipPassword
	 */
	public static final String TAB_USER_PASSWORD_SIP_PASSWORD = "sippassword";
	/**
	 * sessionKey
	 */
	public static final String TAB_USER_SESSION_KEY = "sessionKey";
	/**
	 * 用户的userId
	 */
	public static final String TAB_USER_USERID_KEY = "userId";

	/**
	 * 来电sipId
	 */
	public static final String TAB_USER_PASSWORD_INCOMING_SIP_ID_key = "incoming_sipid";
	
	/**
	 * 访客密码
	 */
	public static final String TAB_USER_VISITOR_PASSWORD_KEY = "visitor_password";
	/**
	 * 极光推送token
	 */
	public static final String TAB_USER_JPUSH_TOKEN_KEY = "token";
	
	/**
	 * 呼叫类型表
	 */
	public static final String TAB_CALL_TYPE = "tab_call_type";

	/**
	 * 消息表
	 */
	public static final String TAB_MSG = "tab_msg";
	/**
	 * 消息数量key值
	 */
	public static final String TAB_MSG_COUNT_KEY = "msg_count";

	
	//2、Sqlite数据库
	/**
	 * 设备表名
	 */
	public static final String DEVICELIST_TABLE_NAME = "deviceList";
	//设备列表各个字段
	/**设备列表id字段*/
	public static final String DEVICELIST_COLUMN_ID = "id";
	/**设备列表username字段*/
	public static final String DEVICELIST_COLUMN_USERNAME = "username";
	/**设备列表mac字段*/
	public static final String DEVICELIST_COLUMN_MAC = "mac";
	/**设备列表sipId字段*/
	public static final String DEVICELIST_COLUMN_SIPID = "sipId";
	/**设备列表roomNum字段*/
	public static final String DEVICELIST_COLUMN_ROOMNUM = "roomNum";
	/**设备列表extensionNum字段*/
	public static final String DEVICELIST_COLUMN_EXTENSIONNUM = "extensionNum";
	/**设备列表屏蔽来电状态字段*/
	public static final String DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS = "shieldStatus";
	/**设备名称字段*/
	public static final String DEVICELIST_COLUMN_DEVICE_NAME = "deviceName";
	/**设备别名字段*/
	public static final String DEVICELIST_COLUMN_DEVICE_ALIAS = "deviceAlias";
	/**门口机字符串*/
	public static final String DEVICELIST_COLUMN_DOOR_DEVICE_STR = "doorDeviceStr";
	/**设备编号*/
	public static final String DEVICELIST_COLUMN_DEVICECODE = "deviceCode";

	/**
	 * 创建设备列表的sql
	 */
	public static final String CREATE_DEVICE_TABLE_SQL = 
	"CREATE TABLE IF NOT EXISTS "+DEVICELIST_TABLE_NAME+"("
	+ DEVICELIST_COLUMN_ID+" INTEGER,"
	+ DEVICELIST_COLUMN_USERNAME+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_MAC+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_SIPID+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_ROOMNUM+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS+" Integer,"
	+ DEVICELIST_COLUMN_DEVICE_NAME+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_EXTENSIONNUM+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_DEVICECODE+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_DEVICE_ALIAS+" VARCHAR(50),"
	+ DEVICELIST_COLUMN_DOOR_DEVICE_STR+" VARCHAR(50))";
	
	/**
	 * 增加shieldStatus一列sql
	 */
	public static final String ADD_SHIELD_STATUS_COLUMN = 
	"ALTER TABLE "+DEVICELIST_TABLE_NAME+" ADD "+DEVICELIST_COLUMN_INCOMING_SHIELD_STATUS+" INTEGER DEFAULT 1";
	
	/**
	 * 增加deviceName一列sql
	 */
	public static final String ADD_DEVICE_NAME_COLUMN = 
			"ALTER TABLE "+DEVICELIST_TABLE_NAME+" ADD "+DEVICELIST_COLUMN_DEVICE_NAME+" VARCHAR(50)";
	
	/**
	 * 增加doorDeviceStr一列sql
	 */
	public static final String ADD_DOOR_DEVICE_STR_COLUMN = 
			"ALTER TABLE "+DEVICELIST_TABLE_NAME+" ADD "+DEVICELIST_COLUMN_DOOR_DEVICE_STR+" VARCHAR(50)";
	/**
	 * 增加deviceCode一列sql
	 */
	public static final String ADD_DEVICECODE_COLUMN =
			"ALTER TABLE "+DEVICELIST_TABLE_NAME+" ADD "+DEVICELIST_COLUMN_DEVICECODE+" VARCHAR(50)";
	/**
	 * 增加deviceCode一列sql
	 */
	public static final String ADD_DEVICE_ALIAS_COLUMN =
			"ALTER TABLE "+DEVICELIST_TABLE_NAME+" ADD "+DEVICELIST_COLUMN_DEVICE_ALIAS+" VARCHAR(50)";

}
