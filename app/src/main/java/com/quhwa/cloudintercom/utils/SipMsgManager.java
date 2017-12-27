package com.quhwa.cloudintercom.utils;

import java.io.UnsupportedEncodingException;

import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MyAccount;
import org.pjsip.pjsua2.app.MyBuddy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

import android.util.Log;
import android.widget.Toast;

public class SipMsgManager {
	private static String Tag = "SipMsgManager";
	/**
	 * 发送消息
	 * @param number 对方sip账号
	 * @param msgBody 消息文本
	 */
	public static void sendInstantMessage(String number, String msgBody,MyAccount account) {
		if(NetworkManager.isConnected(MyApplication.instance)){
			String sipServer = Constants.SERVER_IP;
			String buddy_uri = "<sip:" + number + "@" + sipServer + ">";
			try {
				byte[] enBase = Encrypt.enBase(msgBody.getBytes(Encrypt.CHARSET));
				msgBody = new String(enBase, Encrypt.CHARSET);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			BuddyConfig bCfg = new BuddyConfig();
			bCfg.setUri(buddy_uri);
			bCfg.setSubscribe(false);
			
			MyBuddy myBuddy = new MyBuddy(bCfg);
			SendInstantMessageParam prm = new SendInstantMessageParam();
			prm.setContent(msgBody);
			
			try {
				myBuddy.create(account, bCfg);
				myBuddy.sendInstantMessage(prm);
				myBuddy.delete();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}else{
			Toast.makeText(MyApplication.instance, "网络无连接", Toast.LENGTH_SHORT).show();
			MyDialog.dismissDialog();
			return;
		}
	}
	/**
	 * 获取消息命令
	 * @param msg 封装消息文本的对象
	 * @return json字符串
	 */
	public static String getMsgCommand(TextMsg msg){
		String json = new GsonBuilder().disableHtmlEscaping().create().toJson(msg);
		MyLog.print(Tag , "消息json:"+json, MyLog.PRINT_RED);
		return json;
	}
}
