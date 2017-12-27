package com.quhwa.cloudintercom.netmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.NetworkManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.os.Handler;
import android.os.Message;
/**
 * 网络管理类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class OkhttpManager {
	private String json;

	/**
	 * 联网并解析json数据
	 * @param url
	 * @param addRequestParams
	 * @param javaBeans
	 * @param handler
	 * @param successMsg
	 * @param failMsg
     * @param noNetMsg
     * @param type 0表示解析json对象  1表示解析json数组
     */
	public void getData(final String url, final HashMap<String, String> addRequestParams,
						final List<Class> javaBeans, final Handler handler,
						final int successMsg, final int failMsg,final int noNetMsg,final int type) {
		if(NetworkManager.isConnected(MyApplication.instance)){
			new Thread(){
				@Override
				public void run() {
					OkHttpClient okHttpClient = new OkHttpClient();
					FormBody.Builder builder = new FormBody.Builder();
					if ((addRequestParams != null) && (addRequestParams.size() > 0)) {
						for (String key : addRequestParams.keySet()) {
							String value = (String) addRequestParams.get(key);
							builder.add(key, value);
						}
					}
					Request request = new Request.Builder().url(url).post(builder.build())
							.build();
					okHttpClient.newCall(request).enqueue(new Callback() {
						private Object object;
						private List list = new ArrayList();

						@SuppressWarnings("unchecked")
						public void onResponse(Call arg0, Response response)
								throws IOException {
							try {
								json = response.body().string();
								if (json == null || json.equals("[]")) {
									return;
								}
								if(json.contains("\"\"")){
									json = json.replace("\"\"","null");
								}
								System.out.println("json:" + json);
								JsonParser jsonParser = new JsonParser();
								if ((javaBeans != null) && (javaBeans.size() > 0)) {
									if(type == 0){
										int i = 0;
										for (int len = javaBeans.size(); i < len; i++) {
											object = jsonParser.getObject(json,javaBeans.get(i));
											MyLog.print("Tag",object.toString(),MyLog.PRINT_RED);
										}
									}else if(type == 1){
										list = jsonParser.getObjectList1(json,javaBeans.get(0));
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException();
							} finally {
								Message msg = handler.obtainMessage();
								if(type == 0){
									msg.obj = object;
								}else if(type == 1){
									msg.obj = list;
								}
								msg.what = successMsg;
								handler.sendMessage(msg);
							}
						}

						public void onFailure(Call arg0, IOException arg1) {
							handler.sendEmptyMessage(failMsg);
						}
					});
				}
			}.start();

		}else{
			handler.sendEmptyMessage(noNetMsg);
		}
	}
}
