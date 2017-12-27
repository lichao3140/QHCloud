package com.quhwa.cloudintercom.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.MainPagerAdapter;
import com.quhwa.cloudintercom.presenter.AreaNoticePresenter;
import com.quhwa.cloudintercom.view.IAreaNoticeView;
import com.quhwa.cloudintercom.widget.AutoRadioGroup;
import com.quhwa.cloudintercom.widget.ViewPagerScroller;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AreaNoticeActivity_ extends BaseActivity implements IAreaNoticeView,RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.rb_area_msg)
    RadioButton rbAreaMsg;
    @BindView(R.id.rb_personal_msg)
    RadioButton rbPersonalMsg;
    @BindView(R.id.rg_msg)
    AutoRadioGroup rgMsg;
    @BindView(R.id.vp_msg)
    ViewPager vpMsg;
    @BindView(R.id.rl_notice)
    AutoLinearLayout rlNotice;
    private AreaNoticePresenter areaNoticePresenter;
    private MainPagerAdapter mainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_notice_);
        ButterKnife.bind(this);
        tvTitleText.setText(R.string.area_announcement);
        loadPager();
        initListener();
    }

    private void loadPager() {
        areaNoticePresenter = new AreaNoticePresenter(this);
        areaNoticePresenter.loadPage();
    }

    /**
     * 初始化监听器
     */
    @SuppressWarnings("deprecation")
    private void initListener() {
        rgMsg.setOnCheckedChangeListener(this);
        vpMsg.setOnPageChangeListener(this);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rbAreaMsg.setChecked(true);
                break;
            case 1:
                rbPersonalMsg.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_area_msg:
                vpMsg.setCurrentItem(0);
                break;
            case R.id.rb_personal_msg:
                vpMsg.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void loadPage(List<Fragment> fragments) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mainPagerAdapter = new MainPagerAdapter(fragmentManager,fragments);
        vpMsg.setOffscreenPageLimit(mainPagerAdapter.getCount()-1);//缓存fragment
        vpMsg.setAdapter(mainPagerAdapter);
        new ViewPagerScroller(AreaNoticeActivity_.this).initViewPagerScroll(vpMsg);//改变viewPager滑动速度
    }
}
