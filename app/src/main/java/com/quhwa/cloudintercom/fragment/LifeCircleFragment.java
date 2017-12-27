package com.quhwa.cloudintercom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.ProduceDetailActivity;
import com.quhwa.cloudintercom.adapter.LifeCircleAdapter;
import com.quhwa.cloudintercom.bean.Produce;
import com.quhwa.cloudintercom.presenter.LifeCircelPresenter;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.view.ICircleFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 生活圈页面
 *
 * @author lxz
 * @date 2017年3月17日
 */
public class LifeCircleFragment extends Fragment implements ICircleFragment,AdapterView.OnItemClickListener,OnRefreshListener{
    @BindView(R.id.gv_life_circle)
    GridView gvLifeCircle;
    @BindView(R.id.smart_refresh_life_circle)
    SmartRefreshLayout smartRefreshLifeCircle;
    Unbinder unbinder;

    private String Tag = "LifeCircleFragment";
    private View view;
    private LifeCircelPresenter lifeCircelPresenter;
    private List<Produce> produces;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_life_circle, null);
        MyLog.print(Tag, "onCreateView", MyLog.PRINT_GREEN);
        ButterKnife.bind(this, view);
        loadProdectList();
        return view;
    }

    private void loadProdectList() {
        lifeCircelPresenter = new LifeCircelPresenter(this);
        lifeCircelPresenter.loadProduceList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyLog.print(Tag, "onActivityCreated", MyLog.PRINT_GREEN);
        setListener();
    }

    private void setListener() {
        gvLifeCircle.setOnItemClickListener(this);
        smartRefreshLifeCircle.setOnRefreshListener(this);
        smartRefreshLifeCircle.setPrimaryColorsId(R.color.app_base_color,R.color.white);
    }


    private LifeCircleAdapter lifeCircleAdapter = null;

    @Override
    public void loadProduceList(List<Produce> produces) {
        this.produces = produces;
        if (lifeCircleAdapter == null) {
            lifeCircleAdapter = new LifeCircleAdapter(getActivity(), produces);
        }
        System.out.println(lifeCircelPresenter.toString());
        gvLifeCircle.setAdapter(lifeCircleAdapter);

    }

    @Override
    public void refreshCompelete() {
        smartRefreshLifeCircle.finishRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (produces != null && produces.size() > 0) {
            Intent intent = new Intent(getActivity(), ProduceDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("produce", produces.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        lifeCircelPresenter.loadProduceList();
    }
}
