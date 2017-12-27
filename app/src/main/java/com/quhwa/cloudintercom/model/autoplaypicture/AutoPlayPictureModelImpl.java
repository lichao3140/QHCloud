package com.quhwa.cloudintercom.model.autoplaypicture;

import java.util.ArrayList;
import java.util.List;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.Advertisement;

public class AutoPlayPictureModelImpl implements IAutoPlayPictureModel{

	@Override
	public void loadPictureData(PictureDataOnLoadListener pictureDataOnLoadListener) {
		List<Advertisement>	advertisements = new ArrayList<Advertisement>();
		advertisements.add(new Advertisement("QU-80RC5",R.drawable.a));
		advertisements.add(new Advertisement("90L10",R.drawable.b));
		advertisements.add(new Advertisement("QU-80RC18",R.drawable.c));
		advertisements.add(new Advertisement("高端智能门口机主机系列",R.drawable.d));
//		advertisements.add(new Advertisement("",R.drawable.e));
		pictureDataOnLoadListener.onCompelete(advertisements);
	}

}
