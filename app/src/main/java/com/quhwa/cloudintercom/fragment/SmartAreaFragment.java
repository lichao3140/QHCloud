package com.quhwa.cloudintercom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.AreaNoticeActivity_;
import com.quhwa.cloudintercom.activity.BindDeviceActivity;
import com.quhwa.cloudintercom.activity.ContactPropertyActivity;
import com.quhwa.cloudintercom.activity.ConvenienceServiceActivity;
import com.quhwa.cloudintercom.activity.MonitorActivity;
import com.quhwa.cloudintercom.activity.RecordActivity;
import com.quhwa.cloudintercom.activity.SettingActivity;
import com.quhwa.cloudintercom.activity.UnlockActivity;
import com.quhwa.cloudintercom.activity.VisitorPwdActivity;
import com.quhwa.cloudintercom.activity.VisitorPwdActivity_;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.Advertisement;
import com.quhwa.cloudintercom.presenter.SmartAreaPresenter;
import com.quhwa.cloudintercom.utils.GlideImageLoader;
import com.quhwa.cloudintercom.view.ISmartAreaView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.CubeOutTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 智慧小区页面
 *
 * @author lxz
 * @date 2017年3月17日
 */
public class SmartAreaFragment extends Fragment implements OnClickListener,ISmartAreaView,OnBannerListener{
	private static final int REFRESH_COMPLETE = 1;
	private String Tag = "SmartAreaFragment";
	private View mView;
	private SmartRefreshLayout smartRefreshLayout;
	private Button mBtnDoorToDoor,mBtnMonitor,mBtnSetting,mBtnBindDevice,mBtnRecord,mBtnUnlock,mBtnContactManager,mBtnServerPeople,mBtnAreaNotice;
	private Banner banner;
	private SmartAreaPresenter smartAreaPresenter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_smart_area, null);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setView();
		initListener();
		loadAutoPicture();
	}

	private void loadAutoPicture() {
		smartAreaPresenter = new SmartAreaPresenter(this);
		smartAreaPresenter.load();
	}

	private void initListener() {
		mBtnDoorToDoor.setOnClickListener(this);
		mBtnMonitor.setOnClickListener(this);
		mBtnSetting.setOnClickListener(this);
		mBtnBindDevice.setOnClickListener(this);
		mBtnRecord.setOnClickListener(this);
		mBtnUnlock.setOnClickListener(this);
		mBtnContactManager.setOnClickListener(this);
		mBtnServerPeople.setOnClickListener(this);
		mBtnAreaNotice.setOnClickListener(this);
		banner.setOnBannerListener(this);
	}

	private void setView() {
		mBtnDoorToDoor = (Button) mView.findViewById(R.id.btn_door_to_door);
		mBtnMonitor = (Button) mView.findViewById(R.id.btn_monitor);
		mBtnSetting = (Button) mView.findViewById(R.id.btn_setting);
		mBtnBindDevice = (Button) mView.findViewById(R.id.btn_bind_device);
		mBtnRecord = (Button) mView.findViewById(R.id.btn_record);
		mBtnUnlock =(Button) mView.findViewById(R.id.btn_unlock);
		mBtnContactManager =(Button) mView.findViewById(R.id.btn_contact_manager);
		mBtnServerPeople =(Button) mView.findViewById(R.id.btn_server_for_people);
		mBtnAreaNotice =(Button) mView.findViewById(R.id.btn_area_announcement);
		banner = (Banner) mView.findViewById(R.id.banner);
		smartRefreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.smart_refresh_smart_area);
		smartRefreshLayout.setPrimaryColorsId(R.color.app_base_color,R.color.white);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_door_to_door://户户通
//			startToActivity(DialerActivity.class);
			startToActivity(VisitorPwdActivity.class);
			break;
		case R.id.btn_monitor://监控
			startToActivity(MonitorActivity.class);
			break;
		case R.id.btn_setting://设置
			startToActivity(SettingActivity.class);
			break;
		case R.id.btn_bind_device://绑定设备
			startToActivity(BindDeviceActivity.class);
			break;
		case R.id.btn_record://记录
			startToActivity(RecordActivity.class);
			break;
		case R.id.btn_unlock://开锁
			startToActivity(UnlockActivity.class);
			break;
		case R.id.btn_contact_manager://联系物业
			startToActivity(ContactPropertyActivity.class);
			break;
		case R.id.btn_area_announcement://小区公告
			startToActivity(AreaNoticeActivity_.class);
//			startToActivity(AreaNoticeActivity.class);
			break;
		case R.id.btn_server_for_people://便民服务
			startToActivity(ConvenienceServiceActivity.class);
//			startToActivity(DialerActivity.class);
			break;

		default:
			break;
		}
	}
	/**
	 * 跳转到指定Activity
	 * @param cla 指定跳转到哪个Activity
	 */
	public void startToActivity(Class cla){
		startActivity(new Intent(MyApplication.instance,cla));
	}


	@Override
	public void loadPicture(List<Advertisement> advertisements) {

		//使用Banner轮播图
		List<Integer> images = new ArrayList<Integer>();
		List<String> titles = new ArrayList<String>();
		for (int i = 0;i<advertisements.size();i++){
			images.add(advertisements.get(i).getIcon());
			titles.add(advertisements.get(i).getIntroduce());
		}
		banner.setImages(images)
				.setBannerTitles(titles)
				.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
				.setImageLoader(new GlideImageLoader())
				.setBannerAnimation(CubeOutTransformer.class)
				.setDelayTime(5000)
				.start();
	}

	@Override
	public void OnBannerClick(int position) {
		//TODO
		Toast.makeText(getActivity(),position+"",Toast.LENGTH_SHORT).show();
	}
}
