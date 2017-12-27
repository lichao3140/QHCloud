package com.quhwa.cloudintercom.model.userinfo;

import android.os.Handler;
import android.os.Message;

import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.ReturnResult;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.netmanager.OkhttpManager;
import com.quhwa.cloudintercom.netmanager.RequestParamsValues;
import com.quhwa.cloudintercom.utils.Constants;
import com.quhwa.cloudintercom.utils.MSG;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lxz on 2017/10/17 0017.
 */

public class UserInfoModelImpl implements IUserInfoModel {
    private OnDeleteTokenlistener onDeleteTokenlistener;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG.DELETE_TOKEN_SUCCESS:
                    ReturnResult result = (ReturnResult) msg.obj;
                    onDeleteTokenlistener.onCompelete(result);
                    break;
                case MSG.SERVER_EXCEPTION:
                    onDeleteTokenlistener.onCompleteFail();
                    break;
                case MSG.NO_NET_MSG:
                    onDeleteTokenlistener.onNoNet();
                    break;
            }
        }
    };
    @Override
    public void deleteToken(OnDeleteTokenlistener onDeleteTokenlistener,String token) {
        this.onDeleteTokenlistener = onDeleteTokenlistener;
        RequestParamsValues requestParams = new RequestParamsValues();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        HashMap<String, String> addRequestParams = requestParams
                .addRequestParams(map);
        OkhttpManager okhttpManager = new OkhttpManager();
        List<Class> clsList = new ArrayList<Class>();
        clsList.add(ReturnResult.class);
        okhttpManager.getData(Constants.DELETE_TOKEN, addRequestParams,
                clsList, handler, MSG.DELETE_TOKEN_SUCCESS,
                MSG.SERVER_EXCEPTION,MSG.NO_NET_MSG,0);
    }
}
