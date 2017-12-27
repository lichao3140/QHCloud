package com.quhwa.cloudintercom.model.userinfo;

import com.quhwa.cloudintercom.bean.ReturnResult;

/**
 * Created by lxz on 2017/10/17 0017.
 */

public interface IUserInfoModel {
    void deleteToken(OnDeleteTokenlistener onDeleteTokenlistener,String token);
    interface OnDeleteTokenlistener{
        void onCompelete(ReturnResult result);
        void onCompleteFail();
        void onNoNet();
    }
}
