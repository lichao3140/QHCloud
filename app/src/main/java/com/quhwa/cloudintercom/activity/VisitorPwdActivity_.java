package com.quhwa.cloudintercom.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.utils.PopupWindowManager;

public class VisitorPwdActivity_ extends BaseActivity implements PopupWindowManager.LayoutCallBackListener{
    private VisitorPwdActivity_ instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_pwd_);
        instance = this;
    }


    @Override
    public void layoutCallBack(View popupWindowLayout) {

    }

}
