package com.quhwa.cloudintercom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.MsgResult;
import com.quhwa.cloudintercom.utils.TimerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.msg_title)
    TextView msgTitle;
    @BindView(R.id.msg_time)
    TextView msgTime;
    @BindView(R.id.msg_content)
    TextView msgContent;
    @BindView(R.id.activity_notice)
    AutoLinearLayout activityNotice;
    private MsgResult.MsgDataBean.MsgInfoBean msgInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        tvTitleText.setText(R.string.msg_detail);
        getData();
        showData();
    }

    private void showData() {
        msgTitle.setText(msgInfoBean.getTitle());
        msgTime.setText(TimerUtils.timeLongToDate(msgInfoBean.getCreateTime(), "yyyy-MM-dd hh:ss"));
        msgContent.setText(msgInfoBean.getContent());
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        msgInfoBean = (MsgResult.MsgDataBean.MsgInfoBean) bundle.get("msgInfoBean");
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
