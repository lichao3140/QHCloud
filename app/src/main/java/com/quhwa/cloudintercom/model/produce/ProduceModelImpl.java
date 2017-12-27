package com.quhwa.cloudintercom.model.produce;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.Produce;
import com.quhwa.cloudintercom.R;

public class ProduceModelImpl implements IProduceModel{

	@SuppressWarnings("deprecation")
	@Override
	public void loadProduceData(
			ProduceDataOnLoadListener produceDataOnLoadListener) {
		List<Produce> produces = new ArrayList<Produce>();
		Resources resources = MyApplication.instance.getResources();
		produces.add(new Produce("百草味 台湾特色糕点 凤梨酥", 29, "", null,R.drawable.food_1, "","德林蛋糕店","0755-8642689"));
		produces.add(new Produce("口水娃 零食大礼包 肉类豆干 ", 50, "", null,R.drawable.food_2, "","乔司披萨","0755-75032657"));
		produces.add(new Produce("廖记棒棒鸡460g无骨凤爪", 72, "", null,R.drawable.food_3, "","老王菜馆","0755-85632654"));
		produces.add(new Produce("良品铺子香辣味金钱菇 麻辣特产", 89, "", null,R.drawable.food_4, "","良品铺子","0755-85622632"));
		produces.add(new Produce("百草味 卤味大礼包 肉脯肉干豆干", 13, "", null,R.drawable.food_5, "","百草味","0755-89632523"));
		produces.add(new Produce("卤香干 气调盒装", 89, "", null,R.drawable.food_6, "","周黑鸭","0755-81696256"));
		produceDataOnLoadListener.onComplete(produces);;
	}

}
