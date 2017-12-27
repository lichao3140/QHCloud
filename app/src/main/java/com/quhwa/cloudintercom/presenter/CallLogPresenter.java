package com.quhwa.cloudintercom.presenter;

import java.util.List;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.CallLog;
import com.quhwa.cloudintercom.model.calllog.CallLogModelImpl;
import com.quhwa.cloudintercom.model.calllog.ICallLogModel;
import com.quhwa.cloudintercom.model.calllog.ICallLogModel.CallLogDataOnLoadListener;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.ICallLogView;

public class CallLogPresenter {
	private ICallLogView iCallLogView;

	public CallLogPresenter(ICallLogView iCallLogView) {
		super();
		this.iCallLogView = iCallLogView;
	}
	
	private ICallLogModel callLogModelImpl = new CallLogModelImpl(); 
	
	/**
	 * 加载通话记录列表
	 */
	public void loadCallLogList() {
		if (MySharedPreferenceManager.queryBoolean(MyApplication.instance,
				Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {// 登陆状态
			if (iCallLogView != null && callLogModelImpl != null) {
				callLogModelImpl
						.loadCallLogData(new CallLogDataOnLoadListener() {
							@Override
							public void onCompelete(List<CallLog> logs) {
								iCallLogView.loadCallLogList(logs);
							}
						});
			}
		}
	}
}
