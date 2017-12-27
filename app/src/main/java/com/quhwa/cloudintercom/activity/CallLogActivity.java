package com.quhwa.cloudintercom.activity;

import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.CallLogListAdapter;
import com.quhwa.cloudintercom.bean.CallLog;
import com.quhwa.cloudintercom.presenter.CallLogPresenter;
import com.quhwa.cloudintercom.view.ICallLogView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class CallLogActivity extends BaseActivity implements OnClickListener,OnLoadmoreListener,OnRefreshListener,ICallLogView{
	private TextView tvBack;
	private ListView lvCallLog;
	private SmartRefreshLayout smartRefreshLayout;
	private CallLogListAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_log);
		setView();
		initListener();
		setAdapter();
	}
	private void setAdapter() {
		new CallLogPresenter(this).loadCallLogList();
	}
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvBack.setOnClickListener(this);
		smartRefreshLayout.setOnRefreshListener(this);
		smartRefreshLayout.setOnLoadmoreListener(this);
		smartRefreshLayout.setPrimaryColorsId(R.color.app_base_color, android.R.color.white);
		smartRefreshLayout.autoRefresh();
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.call_log_title);
		TextView tvLogin = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.call_log);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		lvCallLog = (ListView) findViewById(R.id.lv_call_log);
		smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			CallLogActivity.this.finish();
			break;

		default:
			break;
		}
	}
	@Override
	public void loadCallLogList(List<CallLog> logs) {
		adapter = new CallLogListAdapter(this, logs);
		lvCallLog.setAdapter(adapter);
	}
	@Override
	public void showLoadDialog() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void disMissLoadDialog() {
		// TODO Auto-generated method stub

	}


	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		smartRefreshLayout.finishRefresh(3000);
	}

	@Override
	public void onLoadmore(RefreshLayout refreshlayout) {
		smartRefreshLayout.finishLoadmore(3000);
	}
}
