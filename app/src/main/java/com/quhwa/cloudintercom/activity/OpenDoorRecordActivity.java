package com.quhwa.cloudintercom.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;

public class OpenDoorRecordActivity extends BaseActivity implements OnClickListener{

	private View tvBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_door_record);
		setView();
		initListener();
	}
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvBack.setOnClickListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.open_door_record_title);
		TextView tvTitle = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvTitle.setText(R.string.door_record);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			OpenDoorRecordActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
