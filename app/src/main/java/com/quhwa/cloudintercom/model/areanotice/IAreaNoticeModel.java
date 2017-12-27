package com.quhwa.cloudintercom.model.areanotice;

import java.util.List;

import android.support.v4.app.Fragment;

import com.quhwa.cloudintercom.bean.AreaNoticeInfo;
import com.quhwa.cloudintercom.bean.MsgResult;

public interface IAreaNoticeModel {
	/**
	 * 加载消息数据
	 * @param areaNoticeInfoOnLoadListener
	 */
	void loadAreaNoticeInfo(AreaNoticeInfoOnLoadListener areaNoticeInfoOnLoadListener,int currentPage,int pageSize,int type);




	/**
	 * 
	 * 消息数据加载监听器
	 * @author lxz
	 * @date 2017年4月11日
	 */
	interface AreaNoticeInfoOnLoadListener{
		void onComplete(MsgResult msgResult);
		void onCompleteFail();//获取不到数据
		void onNoNet();
	}
	/**
	 * 加载页面
	 * @param pageDataOnLoadListener
	 */
	void loadPageData(PageDataOnLoadListener pageDataOnLoadListener);
	/**
	 * 
	 * 页面数据加载监听器
	 * @author lxz
	 * @date 2017年4月11日
	 */
	interface PageDataOnLoadListener{
		void onComplete(List<Fragment> fragments);
	}

	/**
	 * 删除消息数据
	 * @param deletMsgOnLoadListener
	 * @param ids 要删除消息的id
     */
	void loadDeleteMsg(DeletMsgOnLoadListener deletMsgOnLoadListener, List<String> ids);


	/**
	 *
	 * 设备数据加载监听器
	 * @author lxz
	 * @date 2017年4月11日
	 */
	interface DeletMsgOnLoadListener{
		void onComplete(MsgResult msgResult);
		void onCompleteFail();//获取不到数据
		void onNoNet();
	}

}
