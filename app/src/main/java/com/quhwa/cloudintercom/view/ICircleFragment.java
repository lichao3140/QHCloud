package com.quhwa.cloudintercom.view;

import java.util.List;

import com.quhwa.cloudintercom.bean.Produce;

public interface ICircleFragment {
	/**加载通话记录列表*/
	void loadProduceList(List<Produce> produces);
	
	void refreshCompelete();
}
