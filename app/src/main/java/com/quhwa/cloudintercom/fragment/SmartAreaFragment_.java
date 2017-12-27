package com.quhwa.cloudintercom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.SmartAreaRecycleAdapter;
import com.quhwa.cloudintercom.bean.Advertisement;
import com.quhwa.cloudintercom.presenter.SmartAreaPresenter;
import com.quhwa.cloudintercom.view.ISmartAreaView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lxz on 2017/9/17 0017.
 */

public class SmartAreaFragment_ extends Fragment implements OnRefreshListener,ISmartAreaView {
    @BindView(R.id.recyclerView_smart_area)
    RecyclerView recyclerViewSmartArea;
    @BindView(R.id.smart_refresh_smart_area)
    SmartRefreshLayout smartRefreshSmartArea;
    Unbinder unbinder;

    private View view;
    private SmartAreaPresenter smartAreaPresenter;
    private SmartAreaRecycleAdapter adapter;
    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
    private SmartAreaFragment_ instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_smart_area_, null);
        ButterKnife.bind(this, view);
        smartRefreshSmartArea.setPrimaryColorsId(R.color.app_base_color, R.color.white);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setListener();
        instance = this;
        loadData();
    }

    private void setListener() {
        smartRefreshSmartArea.setOnRefreshListener(this);
    }

    private void loadData() {
        smartAreaPresenter = new SmartAreaPresenter(this);
        smartAreaPresenter.load();
    }
    private void setAdapter(List<Advertisement> advertisements) {
        adapter = new SmartAreaRecycleAdapter(getActivity(),advertisements,instance);
        recyclerViewSmartArea.setAdapter(adapter);
        recyclerViewSmartArea.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void loadPicture(List<Advertisement> advertisements) {
        setAdapter(advertisements);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(adapter != null){
//            adapter.setShow(isShow);
//            adapter.notifyItemChanged(1);
//        }
    }
}
