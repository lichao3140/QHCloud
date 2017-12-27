package com.quhwa.cloudintercom.model.produce;

import java.util.List;

import com.quhwa.cloudintercom.bean.Produce;

public interface IProduceModel {
	/**
	 * 加载商家产品数据
	 * @param produceDataOnLoadListener
	 */
	void loadProduceData(ProduceDataOnLoadListener produceDataOnLoadListener);
	/**
	 * 
	 * 商品数据加载监听器
	 * @author lxz
	 * @date 2017年6月16日
	 */
	interface ProduceDataOnLoadListener{
		void onComplete(List<Produce> produces);
	}
}
