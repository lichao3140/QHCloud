package com.quhwa.cloudintercom.view;

import com.quhwa.cloudintercom.bean.MsgResult;

public interface IMessageView {
	/**
	 * 加载消息数据
	 * @param msgInfoBean
	 */
	void loadAreaNoticeInfoList(MsgResult.MsgDataBean msgInfoBean);
	/**
	 * 数据刷新成功
	 */
	void showToastRefreshSuccess();
	/**
	 * 数据加载失败
	 */
	void showToastLoadFail();

	/**
	 * 无网络提示
	 */
	void showToastNoNet();

	/**
	 * 提示删除消息成功
	 */
	void showToastDeleteMsgSuccess(MsgResult msgResult);

	/**
	 * 提示删除消息失败
	 */
	void showToastDeleteMsgFail();
	/**
	 * 提示选择删除选项
	 */
	void showToastChooseDeleteItem();
	/**
	 * 提示暂无更多数据
	 */
	void showToastNoData();
	/**
	 * 提示暂无更新数据
	 */
	void showToastNoRefreshData();

	//void loadMore1(List<MsgResult.MsgDataBean.MsgInfoBean> msgInfoBeans);

	/**
	 * 加载更多数据
	 * @param msgDataBean
     */
	void loadMore(MsgResult.MsgDataBean msgDataBean);

	/**
	 * 设置能否上拉或下拉
	 * @param enable
     */
	void setEnableUpAndDownSuper(boolean enable);

	/**
	 * 取消刷新
	 */
	void cancelRefresh();

	/**
	 * 对话框显示与消失
	 * @param isShow
     */
	void showDeleteDialog(boolean isShow);

	/**
	 * code不为1的时候，数据加载失败
	 */
	void showToastLoadDataFail();
}
