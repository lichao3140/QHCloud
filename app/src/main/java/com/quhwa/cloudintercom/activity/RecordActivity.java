package com.quhwa.cloudintercom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;

public class RecordActivity extends BaseActivity implements OnClickListener{

	private TextView tvBack,tvOpenRecord,tvCallRecord;
	private LinearLayout ll;
	private View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		setView();
		initListener();
	}
	
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvBack.setOnClickListener(this);
		tvOpenRecord.setOnClickListener(this);
		tvCallRecord.setOnClickListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.record_title);
		TextView tvLogin = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.record);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		tvOpenRecord = (TextView) findViewById(R.id.tv_open_door_log);
		tvCallRecord = (TextView) findViewById(R.id.tv_call_log);
		ll = (LinearLayout) findViewById(R.id.ll_setting);
		view = findViewById(R.id.view_record);
		if(!MySharedPreferenceManager.queryBoolean(this, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {
			ll.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			RecordActivity.this.finish();
			break;
		case R.id.tv_call_log:
			startActivity(new Intent(RecordActivity.this,CallLogActivity.class));
			break;
		case R.id.tv_open_door_log:
			startActivity(new Intent(RecordActivity.this,OpenDoorRecordActivity.class));
			break;
		default:
			break;
		}
		
	}
}
