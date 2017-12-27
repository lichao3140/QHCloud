package com.quhwa.cloudintercom.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.MainPagerAdapter;
import com.quhwa.cloudintercom.presenter.AreaNoticePresenter;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IAreaNoticeView;
import com.quhwa.cloudintercom.widget.ViewPagerScroller;

import java.util.List;

public class AreaNoticeActivity extends BaseActivity implements OnClickListener,IAreaNoticeView,OnCheckedChangeListener,OnPageChangeListener{

	private TextView tvBack;
//	private ListView lvAreaNotice;
	private AreaNoticePresenter areaNoticePresenter;
	private RadioGroup rgMsg;
	private RadioButton rbAreaMsg,rbPersonalMsg;
	private ViewPager vpMsg;
	private MainPagerAdapter mainPagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_notice);
		setView();
		loadPager();
		initListener();
	}
	private void loadPager() {
		areaNoticePresenter = new AreaNoticePresenter(this);
		areaNoticePresenter.loadPage();
	}

//	private void setAdapter1() {
//		areaNoticePresenter = new AreaNoticePresenter(this);
//		areaNoticePresenter.loadAreaNoticeList();
//	}
	/**
	 * 初始化监听器
	 */
	@SuppressWarnings("deprecation")
	private void initListener() {
		tvBack.setOnClickListener(this);
		rgMsg.setOnCheckedChangeListener(this);
		vpMsg.setOnPageChangeListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.area_notice_title);
		TextView tvLogin = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.area_announcement);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
//		lvAreaNotice = (ListView) findViewById(R.id.lv_area_notice);
		rgMsg = (RadioGroup) findViewById(R.id.rg_msg);
		rbAreaMsg = (RadioButton) findViewById(R.id.rb_area_msg);
		rbPersonalMsg = (RadioButton) findViewById(R.id.rb_personal_msg);
//		rbTotalMsg = (RadioButton) findViewById(R.id.rb_total_msg);
		vpMsg = (ViewPager) findViewById(R.id.vp_msg);
		View view = findViewById(R.id.view_notice);
		if(MySharedPreferenceManager.queryBoolean(this, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {
			rgMsg.setVisibility(View.VISIBLE);
			vpMsg.setVisibility(View.VISIBLE);
			view.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AreaNoticeActivity.this.finish();
			break;

		default:
			break;
		}
	}
//	@Override
//	public void loadAreaNoticeInfoList(List<AreaNoticeInfo> areaNoticeInfos) {
//		//TODO
//		lvAreaNotice.setAdapter(new AreaNoticeInfoAdapter(AreaNoticeActivity.this, areaNoticeInfos));
//	}
	@Override
	public void loadPage(List<Fragment> fragments) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		mainPagerAdapter = new MainPagerAdapter(fragmentManager,fragments);
		vpMsg.setOffscreenPageLimit(mainPagerAdapter.getCount()-1);//缓存fragment
		vpMsg.setAdapter(mainPagerAdapter);
		new ViewPagerScroller(AreaNoticeActivity.this).initViewPagerScroll(vpMsg);//改变viewPager滑动速度
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_area_msg:
			vpMsg.setCurrentItem(0);
			break;
		case R.id.rb_personal_msg:
			vpMsg.setCurrentItem(1);
			break;
//		case R.id.rb_total_msg:
//			vpMsg.setCurrentItem(2);
//			break;
		}
		
	}
	
	@Override
	public void onPageSelected(int position) {
		switch (position) {
		case 0:
			rbAreaMsg.setChecked(true);
			break;
		case 1:
			rbPersonalMsg.setChecked(true);
			break;
//		case 2:
//			rbTotalMsg.setChecked(true);
//			break;
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

}
