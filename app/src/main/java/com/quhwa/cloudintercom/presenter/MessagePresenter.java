package com.quhwa.cloudintercom.presenter;

import android.util.Log;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.bean.MsgResult;
import com.quhwa.cloudintercom.db.DBManager;
import com.quhwa.cloudintercom.model.areanotice.AreaNoticeModelImpl;
import com.quhwa.cloudintercom.model.areanotice.IAreaNoticeModel;
import com.quhwa.cloudintercom.model.areanotice.IAreaNoticeModel.AreaNoticeInfoOnLoadListener;
import com.quhwa.cloudintercom.utils.Code;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.view.IMessageView;

import java.util.List;

public class MessagePresenter {
	private IMessageView personalMsgFragment;
	private IAreaNoticeModel areaNoticeModelImpl = new AreaNoticeModelImpl();

	public MessagePresenter(IMessageView personalMsgFragment) {
		super();
		this.personalMsgFragment = personalMsgFragment;
	}
	/**
	 * 加载消息列表
	 * @param loadType 0:直接加载  1:刷新的时候加载
	 * @param currentPage
	 * @param pageSize
	 * @param type
     */
	public void loadAreaNoticeList(final int loadType,int currentPage,int pageSize,int type){
		if(personalMsgFragment != null && areaNoticeModelImpl != null){
			if(MySharedPreferenceManager.queryBoolean(MyApplication.instance, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY)) {//判断是否登录
				String username = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER,Table.TAB_USER_NAME_KEY);
				List<Device> devices = DBManager.queryByUsername(MyApplication.instance, Table.DEVICELIST_COLUMN_USERNAME, username);
				if(devices != null && devices.size() > 0){//判断是否绑定设备
					areaNoticeModelImpl.loadAreaNoticeInfo(new AreaNoticeInfoOnLoadListener() {
						@Override
						public void onComplete(MsgResult msgResult) {
							if(msgResult.getCode() == Code.RETURN_SUCCESS){
								if(msgResult.getData().getTotal() > 0){
									switch (loadType) {
										case 0:
											personalMsgFragment.loadAreaNoticeInfoList(msgResult.getData());
											break;
										case 1:
											personalMsgFragment.loadAreaNoticeInfoList(msgResult.getData());
											personalMsgFragment.showToastRefreshSuccess();
											break;
									}
								}else {
									personalMsgFragment.showToastNoRefreshData();
								}
							}else {//code不为1的时候：失败
								//personalMsgFragment.showToastLoadFail();
								personalMsgFragment.showToastLoadDataFail();
							}
						}

						@Override
						public void onCompleteFail() {
							if(personalMsgFragment != null ){
								personalMsgFragment.showToastLoadFail();
							}
						}

						@Override
						public void onNoNet() {
							personalMsgFragment.showToastNoNet();
						}
					},currentPage,pageSize,type);

				}else {//设备没有绑定
					personalMsgFragment.setEnableUpAndDownSuper(false);
					personalMsgFragment.cancelRefresh();
				}
			}else {//用户没有登录
				personalMsgFragment.setEnableUpAndDownSuper(false);
				personalMsgFragment.cancelRefresh();
			}
		}
	}

	/**
	 * 加载更多数据
	 * @param currentPage
	 * @param pageSize
	 * @param type
     */
	public void loadMore(int currentPage,int pageSize,int type){
		if(personalMsgFragment != null && areaNoticeModelImpl != null) {

				areaNoticeModelImpl.loadAreaNoticeInfo(new AreaNoticeInfoOnLoadListener() {
					@Override
					public void onComplete(MsgResult msgResult) {
						Log.e("Tag", msgResult.toString());
						if(msgResult.getCode() == Code.RETURN_SUCCESS){
							if(msgResult.getData().getTotal() > 0){
								personalMsgFragment.loadMore(msgResult.getData());
							}else{
								personalMsgFragment.showToastNoData();
							}
						}else {//code不为1的时候：失败
							personalMsgFragment.showToastLoadFail();
						}
					}

					@Override
					public void onCompleteFail() {
						personalMsgFragment.showToastLoadFail();
					}

					@Override
					public void onNoNet() {
						personalMsgFragment.showToastNoNet();
					}
				}, currentPage, pageSize, type);
		}
	}

	/**
	 * 删除消息
	 * @param ids 要删除消息的集合
     */
	public void deleteMsg(List<String> ids){
		if(personalMsgFragment != null && areaNoticeModelImpl != null){
			if(ids != null && ids.size()>0){
				personalMsgFragment.showDeleteDialog(true);
				areaNoticeModelImpl.loadDeleteMsg(new IAreaNoticeModel.DeletMsgOnLoadListener() {
					@Override
					public void onComplete(MsgResult msgResult) {
						if(msgResult.getCode() == Code.RETURN_SUCCESS){
							personalMsgFragment.showToastDeleteMsgSuccess(msgResult);
						}else {//code不为1的时候：失败
							personalMsgFragment.showToastLoadFail();
						}
					}

					@Override
					public void onCompleteFail() {
						personalMsgFragment.showToastDeleteMsgFail();
					}

					@Override
					public void onNoNet() {
						personalMsgFragment.showToastNoNet();
					}
				},ids);
			}else{
				personalMsgFragment.showToastChooseDeleteItem();
			}
		}
	}
	
	
}
