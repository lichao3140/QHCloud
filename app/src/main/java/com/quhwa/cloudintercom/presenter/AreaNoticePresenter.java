package com.quhwa.cloudintercom.presenter;

import java.util.List;

import android.support.v4.app.Fragment;

import com.quhwa.cloudintercom.bean.AreaNoticeInfo;
import com.quhwa.cloudintercom.model.areanotice.AreaNoticeModelImpl;
import com.quhwa.cloudintercom.model.areanotice.IAreaNoticeModel;
import com.quhwa.cloudintercom.model.areanotice.IAreaNoticeModel.AreaNoticeInfoOnLoadListener;
import com.quhwa.cloudintercom.model.areanotice.IAreaNoticeModel.PageDataOnLoadListener;
import com.quhwa.cloudintercom.view.IAreaNoticeView;

public class AreaNoticePresenter {
	private IAreaNoticeView iAreaNoticeView;

	public AreaNoticePresenter(IAreaNoticeView iAreaNoticeView) {
		super();
		this.iAreaNoticeView = iAreaNoticeView;
	}
	IAreaNoticeModel areaNoticeModelImpl = new AreaNoticeModelImpl();
	
//	public void loadAreaNoticeList(){
//		if(iAreaNoticeView != null && areaNoticeModelImpl != null){
//			areaNoticeModelImpl.loadAreaNoticeInfo(new AreaNoticeInfoOnLoadListener() {
//				@Override
//				public void onComplete(List<AreaNoticeInfo> areaNoticeInfos) {
//					iAreaNoticeView.loadAreaNoticeInfoList(areaNoticeInfos);
//				}
//			});
//		}
//	}
	
	public void loadPage(){
		if(iAreaNoticeView != null && areaNoticeModelImpl != null){
			areaNoticeModelImpl.loadPageData(new PageDataOnLoadListener() {
				@Override
				public void onComplete(List<Fragment> fragments) {
					iAreaNoticeView.loadPage(fragments);
				}
			});
		}
	}
}
