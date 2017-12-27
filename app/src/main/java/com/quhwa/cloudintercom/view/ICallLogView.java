package com.quhwa.cloudintercom.view;

import java.util.List;

import com.quhwa.cloudintercom.bean.CallLog;

public interface ICallLogView {
	/**加载通话记录列表*/
	void loadCallLogList(List<CallLog> logs);
	/**显示加载对话框*/
	void showLoadDialog();
	/**消除加载对话框*/
	void disMissLoadDialog();
}
