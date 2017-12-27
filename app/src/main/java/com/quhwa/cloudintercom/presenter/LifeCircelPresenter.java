package com.quhwa.cloudintercom.presenter;

import java.util.List;

import com.quhwa.cloudintercom.bean.Produce;
import com.quhwa.cloudintercom.fragment.LifeCircleFragment;
import com.quhwa.cloudintercom.model.produce.IProduceModel;
import com.quhwa.cloudintercom.model.produce.IProduceModel.ProduceDataOnLoadListener;
import com.quhwa.cloudintercom.model.produce.ProduceModelImpl;

public class LifeCircelPresenter {
	private LifeCircleFragment lifeCircleView;
	
	
	public LifeCircelPresenter(LifeCircleFragment lifeCircleView) {
		super();
		this.lifeCircleView = lifeCircleView;
	}


	IProduceModel produceModel = new ProduceModelImpl();
	public void loadProduceList(){
		if(lifeCircleView != null && produceModel != null){
			produceModel.loadProduceData(new ProduceDataOnLoadListener() {
				@Override
				public void onComplete(List<Produce> produces) {
					lifeCircleView.loadProduceList(produces);
					lifeCircleView.refreshCompelete();
				}
			});
		}
	}
	
}
