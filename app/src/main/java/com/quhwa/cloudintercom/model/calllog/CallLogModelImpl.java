package com.quhwa.cloudintercom.model.calllog;

import java.util.ArrayList;
import java.util.List;

import com.quhwa.cloudintercom.bean.CallLog;

/**
 * 通话记录model层，获取通话记录数据
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class CallLogModelImpl implements ICallLogModel {

	@Override
	public void loadCallLogData(CallLogDataOnLoadListener listener) {
		// TODO Auto-generated method stub
		List<CallLog> logs = new ArrayList<CallLog>();
		for(int i = 0;i<10;i++){//假数据
			logs.add(new CallLog(i,"roomNum"+i,"12:0"+i,"12:0"+i));
		}
		listener.onCompelete(logs);
	}
	
}
