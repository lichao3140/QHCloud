package com.quhwa.cloudintercom.model.autoplaypicture;

import java.util.List;

import com.quhwa.cloudintercom.bean.Advertisement;

public interface IAutoPlayPictureModel {
	void loadPictureData(PictureDataOnLoadListener pictureDataOnLoadListener);
	interface PictureDataOnLoadListener{
		void onCompelete(List<Advertisement> advertisements);
	}
}
