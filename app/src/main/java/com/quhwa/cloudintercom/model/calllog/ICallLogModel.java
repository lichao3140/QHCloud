package com.quhwa.cloudintercom.model.calllog;

import java.util.List;

import com.quhwa.cloudintercom.bean.CallLog;

public interface ICallLogModel {
	void loadCallLogData(CallLogDataOnLoadListener listener);
	interface CallLogDataOnLoadListener{
		void onCompelete(List<CallLog> logs);
	}
}
