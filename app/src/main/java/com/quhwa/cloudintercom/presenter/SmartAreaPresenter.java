package com.quhwa.cloudintercom.presenter;

import java.util.List;

import android.os.Handler;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.Advertisement;
import com.quhwa.cloudintercom.model.autoplaypicture.AutoPlayPictureModelImpl;
import com.quhwa.cloudintercom.model.autoplaypicture.IAutoPlayPictureModel;
import com.quhwa.cloudintercom.model.autoplaypicture.IAutoPlayPictureModel.PictureDataOnLoadListener;
import com.quhwa.cloudintercom.view.ISmartAreaView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;


public class SmartAreaPresenter {
	private ISmartAreaView iSmartAreaView;

	public SmartAreaPresenter(ISmartAreaView iSmartAreaView) {
		super();
		this.iSmartAreaView = iSmartAreaView;
	}
	
	private IAutoPlayPictureModel autoPlayPictureModelImpl = new AutoPlayPictureModelImpl();
	/**
	 * 轮播图自动播放
	 * @param handler
	 */
	public void play(Handler handler){
		if(iSmartAreaView != null && autoPlayPictureModelImpl != null){
			handler.sendEmptyMessageDelayed(0, 6000);
		}
	}
	/**
	 * 加载轮播图
	 */
	public void load(){
		if(iSmartAreaView != null && autoPlayPictureModelImpl != null){
			autoPlayPictureModelImpl.loadPictureData(new PictureDataOnLoadListener() {
				@Override
				public void onCompelete(List<Advertisement> advertisements) {
					iSmartAreaView.loadPicture(advertisements);
				}
			});
		}
	}

}
