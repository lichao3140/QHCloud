package com.quhwa.cloudintercom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.ShowSmartFamilyProduceActivity;
import com.quhwa.cloudintercom.adapter.SmartFamilyAdapter;
import com.quhwa.cloudintercom.bean.SmartProduce;
import com.quhwa.cloudintercom.utils.MyLog;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 智慧家庭页面
 *
 * @author lxz
 * @date 2017年3月17日
 */
public class SmartFamilyFragment extends Fragment {
    @BindView(R.id.gv_smart_family)
    GridView gvSmartFamily;
    Unbinder unbinder;

    private String Tag = "SmartFamilyFragment";
    private List<SmartProduce> sps = new ArrayList<SmartProduce>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_family, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        MyLog.print(Tag, "onAttach", MyLog.PRINT_GREEN);
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyLog.print(Tag, "onActivityCreated", MyLog.PRINT_GREEN);
        initData();
        setAdapter();
        initListener();
    }

    private void initListener() {
        gvSmartFamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ShowSmartFamilyProduceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("smartProduce",sps.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setAdapter() {
        gvSmartFamily.setAdapter(new SmartFamilyAdapter(getActivity(),sps));
    }

    private void initData() {
        sps.add(new SmartProduce("QH_9811",R.drawable.s1));
        sps.add(new SmartProduce("QH_853Q",R.drawable.s2));
        sps.add(new SmartProduce("QH_0957",R.drawable.s3));
        sps.add(new SmartProduce("QH_857AK",R.drawable.s4));
        sps.add(new SmartProduce("QH_968B",R.drawable.s5));
        sps.add(new SmartProduce("QH_608Ls",R.drawable.s6));
    }

    @Override
    public void onStart() {
        super.onStart();
        MyLog.print(Tag, "onStart", MyLog.PRINT_GREEN);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyLog.print(Tag, "onPause", MyLog.PRINT_GREEN);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyLog.print(Tag, "onStop", MyLog.PRINT_GREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.print(Tag, "onDestroy", MyLog.PRINT_GREEN);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MyLog.print(Tag, "onDetach", MyLog.PRINT_GREEN);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
