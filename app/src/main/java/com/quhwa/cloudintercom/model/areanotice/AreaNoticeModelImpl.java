package com.quhwa.cloudintercom.model.areanotice;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.bean.MsgResult;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.fragment.AreaMessageFragment;
import com.quhwa.cloudintercom.fragment.PersonalMessageFragment;
import com.quhwa.cloudintercom.netmanager.OkhttpManager;
import com.quhwa.cloudintercom.netmanager.RequestParamsValues;
import com.quhwa.cloudintercom.utils.Constants;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AreaNoticeModelImpl implements IAreaNoticeModel{

	private AreaNoticeInfoOnLoadListener areaNoticeInfoOnLoadListener;
	private DeletMsgOnLoadListener deletMsgOnLoadListener;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG.PERSONAL_MSG_SUCCESS:
					MsgResult msgResult = (MsgResult) msg.obj;
					areaNoticeInfoOnLoadListener.onComplete(msgResult);
					break;
				case MSG.SERVER_EXCEPTION:
					areaNoticeInfoOnLoadListener.onCompleteFail();
					break;
				case MSG.NO_NET_MSG:
					areaNoticeInfoOnLoadListener.onNoNet();
					break;
				case MSG.DELETE_MSG_SUCCESS:
					MsgResult msgDeleteResult = (MsgResult) msg.obj;
					deletMsgOnLoadListener.onComplete(msgDeleteResult);
					break;

				default:
					break;
			}
		};
	};

	@Override
	public void loadAreaNoticeInfo(AreaNoticeInfoOnLoadListener areaNoticeInfoOnLoadListener,int currentPage,int pageSize,int type) {
		this.areaNoticeInfoOnLoadListener = areaNoticeInfoOnLoadListener;
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		String username = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_NAME_KEY);
		List<Device> devices = DBManager.queryByUsername(MyApplication.instance, Table.DEVICELIST_COLUMN_USERNAME, username);
		String roomNo = listToString(devices);
		map.put("roomNo", roomNo);
		map.put("page", currentPage+"");
		map.put("size", pageSize+"");
		map.put("type", type+"");
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
//		clsList.add(MsgResult.MsgDataBean.MsgInfoBean.class);
//		clsList.add(MsgResult.MsgDataBean.class);
		clsList.add(MsgResult.class);
		okhttpManager.getData(Constants.MSG_URL, addRequestParams,
				clsList, handler, MSG.PERSONAL_MSG_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
	}

	@Override
	public void loadPageData(PageDataOnLoadListener pageDataOnLoadListener) {
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new AreaMessageFragment());
		fragments.add(new PersonalMessageFragment());
		pageDataOnLoadListener.onComplete(fragments);
	}

	@Override
	public void loadDeleteMsg(DeletMsgOnLoadListener deletMsgOnLoadListener, List<String> ids) {
		this.deletMsgOnLoadListener = deletMsgOnLoadListener;
		RequestParamsValues requestParams = new RequestParamsValues();
		HashMap<String, String> map = new HashMap<String, String>();
		String isdStr = idsListToString(ids);
		map.put("ids",isdStr);
		MyLog.print("idsStr:",isdStr,MyLog.PRINT_RED);
		HashMap<String, String> addRequestParams = requestParams
				.addRequestParams(map);
		OkhttpManager okhttpManager = new OkhttpManager();
		List<Class> clsList = new ArrayList<Class>();
		clsList.add(MsgResult.class);
		okhttpManager.getData(Constants.DELETE_MSG_URL, addRequestParams,
				clsList, handler, MSG.DELETE_MSG_SUCCESS,
				MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
	}
	private String idsListToString(List<String> ids){
		if(ids != null && ids.size()>0){
			StringBuffer buffer = new StringBuffer();
			for (int i = 0;i<ids.size();i++){
				buffer.append(ids.get(i)+",");
			}
			return buffer.substring(0,buffer.length()-1);
		}
		return null;
	}
	private String listToString(List<Device> list){
		if(list != null && list.size()>0){
			StringBuffer buffer = new StringBuffer();
			for (int i = 0;i<list.size();i++){
				String roomNumTotal = list.get(i).getRoomNo();
				buffer.append(roomNumTotal.substring(0,roomNumTotal.length()-2)+",");
			}
			return buffer.substring(0,buffer.length()-1);
		}
		return null;
	}



}
