package com.quhwa.cloudintercom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.AreaNoticeActivity_;
import com.quhwa.cloudintercom.activity.BindDeviceActivity;
import com.quhwa.cloudintercom.activity.ContactPropertyActivity;
import com.quhwa.cloudintercom.activity.ConvenienceServiceActivity;
import com.quhwa.cloudintercom.activity.MonitorActivity;
import com.quhwa.cloudintercom.activity.RecordActivity;
import com.quhwa.cloudintercom.activity.SettingActivity;
import com.quhwa.cloudintercom.activity.UnlockActivity;
import com.quhwa.cloudintercom.activity.VisitorPwdActivity;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.Advertisement;
import com.quhwa.cloudintercom.fragment.SmartAreaFragment_;
import com.quhwa.cloudintercom.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.transformer.CubeOutTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lxz on 2017/9/17 0017.
 */

public class SmartAreaRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 横幅广告
     */
    public static final int BANNER = 0;
    /**
     * 主菜单
     */
    public static final int HOME = 1;

    /**
     * 当前类型
     */
    public int currentType = BANNER;


    private Context mContext;

    private final LayoutInflater mLayoutInflater;

    List<Advertisement> advertisements;
    private boolean isShow;

    private SmartAreaFragment_ instance;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public SmartAreaRecycleAdapter(Context mContext, List<Advertisement> advertisements, SmartAreaFragment_ instance) {
        this.instance = instance;
        this.advertisements = advertisements;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BANNER) {
            return new BannerViewHolder(mLayoutInflater.inflate(R.layout.banner_viewpage, null), mContext, advertisements);
        } else if (viewType == HOME) {
            return new HomeViewHolder(mLayoutInflater.inflate(R.layout.item_home, null), mContext);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                currentType = BANNER;
                break;
            case 1:
                currentType = HOME;
                break;
        }
        return currentType;
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//        if (getItemViewType(position) == HOME) {
//            HomeViewHolder hvh = (HomeViewHolder) holder;
//            hvh.setMsgShow(isShow);
//        }
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == BANNER) {
            BannerViewHolder bvh = (BannerViewHolder) holder;
            bvh.setData();
        } else if (getItemViewType(position) == HOME) {
            HomeViewHolder hvh = (HomeViewHolder) holder;
            hvh.setMsgShow(isShow);
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }


     class BannerViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        List<Advertisement> advertisements;
        private Banner banner;

        public BannerViewHolder(View itemView, Context context, List<Advertisement> advertisements) {
            super(itemView);
            this.mContext = context;
            this.advertisements = advertisements;
            banner = (Banner) itemView.findViewById(R.id.banner);
        }

        public void setData() {
            List<Integer> images = new ArrayList<Integer>();
            List<String> titles = new ArrayList<String>();
            for (int i = 0; i < advertisements.size(); i++) {
                images.add(advertisements.get(i).getIcon());
                titles.add(advertisements.get(i).getIntroduce());
            }

            banner.setImages(images)
                    .setBannerTitles(titles)
                    .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                    .setImageLoader(new GlideImageLoader())
                    .setBannerAnimation(CubeOutTransformer.class)
                    .setDelayTime(5000)
                    .start();
        }

    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        @BindView(R.id.btn_unlock)
        Button btnUnlock;
        @BindView(R.id.btn_bind_device)
        Button btnBindDevice;
        @BindView(R.id.btn_monitor)
        Button btnMonitor;
        @BindView(R.id.btn_door_to_door)
        Button btnDoorToDoor;
        @BindView(R.id.btn_record)
        Button btnRecord;
        @BindView(R.id.btn_contact_manager)
        Button btnContactManager;
        @BindView(R.id.btn_area_announcement)
        Button btnAreaAnnouncement;
        @BindView(R.id.tv_show_msg_new)
        TextView tvShowMsgNew;
        @BindView(R.id.btn_server_for_people)
        Button btnServerForPeople;
        @BindView(R.id.btn_setting)
        Button btnSetting;
        public HomeViewHolder(View view, Context context) {
            super(view);
            this.context = context;
            ButterKnife.bind(this,view);
        }

        public void setMsgShow(boolean isShow){
            if(isShow){
                tvShowMsgNew.setVisibility(View.VISIBLE);
            }else {
                tvShowMsgNew.setVisibility(View.GONE);
            }
        }


        @OnClick({R.id.btn_unlock, R.id.btn_bind_device, R.id.btn_monitor, R.id.btn_door_to_door, R.id.btn_record, R.id.btn_contact_manager, R.id.btn_area_announcement, R.id.btn_server_for_people, R.id.btn_setting})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.btn_unlock:
                    startToActivity(UnlockActivity.class);
                    break;
                case R.id.btn_bind_device:
                    startToActivity(BindDeviceActivity.class);
                    break;
                case R.id.btn_monitor:
                    startToActivity(MonitorActivity.class);
                    break;
                case R.id.btn_door_to_door:
                    startToActivity(VisitorPwdActivity.class);
                    break;
                case R.id.btn_record:
                    startToActivity(RecordActivity.class);
                    break;
                case R.id.btn_contact_manager:
                    startToActivity(ContactPropertyActivity.class);
                    break;
                case R.id.btn_area_announcement:
                    startToActivity(AreaNoticeActivity_.class);
                    break;
                case R.id.btn_server_for_people:
                    startToActivity(ConvenienceServiceActivity.class);
                    instance.setShow(true);
                    break;
                case R.id.btn_setting:
                    startToActivity(SettingActivity.class);
                    break;
            }
        }
        /**
         * 跳转到指定Activity
         * @param cla 指定跳转到哪个Activity
         */
        public void startToActivity(Class cla){
            context.startActivity(new Intent(MyApplication.instance,cla));
        }
    }
}
