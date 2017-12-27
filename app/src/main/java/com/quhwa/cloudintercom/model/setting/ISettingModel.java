package com.quhwa.cloudintercom.model.setting;

import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.ReturnResult;

/**
 * Created by lxz on 2017/10/16 0016.
 */

public interface ISettingModel {
    /**
     *
     * @param disturbStatusListener
     */
    void setDisturbStatus(DisturbStatusListener disturbStatusListener, BoundResult.Device device,String deviceStatus);

    /**
     *
     */
    interface DisturbStatusListener{
        void loadDisturbStatusOnComplete(ReturnResult result);
        void onCompleteFail();//获取不到数据
        void onNoNet();
    }
}
