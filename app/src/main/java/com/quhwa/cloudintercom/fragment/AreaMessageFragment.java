package com.quhwa.cloudintercom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.NoticeRecyclerViewAdapter;
import com.quhwa.cloudintercom.bean.MsgResult;
import com.quhwa.cloudintercom.presenter.MessagePresenter;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.view.IMessageView;
import com.quhwa.cloudintercom.widget.DividerListItemDecoration;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lxz on 2017/9/12 0012.
 */

public class AreaMessageFragment extends Fragment implements IMessageView,OnRefreshListener,OnLoadmoreListener {
    @BindView(R.id.recyclerView_per_msg)
    RecyclerView recyclerViewPerMsg;
    @BindView(R.id.smart_refresh_per_msg)
    SmartRefreshLayout smartRefreshPerMsg;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_delete_cancel)
    TextView tvDeleteCancel;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_choose_all)
    TextView tvChooseAll;
    @BindView(R.id.rl_edit)
    AutoRelativeLayout rlEdit;
    Unbinder unbinder;

    private View view;
    private MessagePresenter messagePresenter;
    private int currentPage = 1;
    private int pageSize = 15;
    private List<MsgResult.MsgDataBean.MsgInfoBean> msgInfoBeans;
    private NoticeRecyclerViewAdapter adapter;
    /**
     * 个人消息
     */
    public static final int AREA_MSG = 2;
    private String Tag = "PersonalMessageFragment_";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_areamessage_, null);
        ButterKnife.bind(this, view);
        smartRefreshPerMsg.setPrimaryColorsId(R.color.app_base_color,R.color.white);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        setListener();
    }
    /**
     * 自动刷新
     */
    private void autoRefresh() {
        smartRefreshPerMsg.autoRefresh();
    }

    private void setListener() {
        smartRefreshPerMsg.setOnRefreshListener(this);
        smartRefreshPerMsg.setOnLoadmoreListener(this);
    }

    private void loadData() {
        messagePresenter = new MessagePresenter(this);
        autoRefresh();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_edit, R.id.tv_delete_cancel, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                edit();
                break;
            case R.id.tv_delete_cancel:
                cancel();
                break;
            case R.id.tv_delete:
                delete();
                break;
        }
    }

    /**
     * 删除
     */
    private void delete() {
        List<String> ids = adapter.getIdsList();
        MyLog.print("idsList",ids.toString(),MyLog.PRINT_RED);
        messagePresenter.deleteMsg(ids);
    }

    /**
     * 取消
     */
    private void cancel() {
        adapter.setCheckBoxVisiable(false);
        adapter.chooseAllOrNone(false);
        hideMenu();
    }

    /**
     * 编辑消息
     */
    private void edit() {
        adapter.setCheckBoxVisiable(true);
        adapter.chooseAllOrNone(false);
        showMenu();
        tvChooseAll.setText(R.string.choose_all);
    }

    /**
     * 设置能否上拉和下拉
     * @param enable
     */
    private void setEnableUpAndDown(boolean enable){
        smartRefreshPerMsg.setEnableRefresh(enable);
        smartRefreshPerMsg.setEnableLoadmore(enable);
    }

    @Override
    public void loadAreaNoticeInfoList(MsgResult.MsgDataBean msgDataBean) {
        this.msgDataBean = msgDataBean;
        msgInfoBeans = msgDataBean.getInfo();
        if(msgInfoBeans != null && msgInfoBeans.size()>0) {
            if (adapter == null) {
                adapter = new NoticeRecyclerViewAdapter(getActivity(), msgInfoBeans,tvChooseAll);
                recyclerViewPerMsg.setAdapter(adapter);
            } else {
                adapter.refresh(msgInfoBeans);
            }
            recyclerViewPerMsg.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
            recyclerViewPerMsg.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            recyclerViewPerMsg.setItemAnimator(new DefaultItemAnimator());
            if(tvEdit.getVisibility() == View.GONE){
                tvEdit.setVisibility(View.VISIBLE);
            }
        }else{
            if(tvEdit.getVisibility() == View.VISIBLE){
                tvEdit.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void showToastRefreshSuccess() {
        MyToast.showToast(getActivity(), R.string.refresh_success);
        smartRefreshPerMsg.finishRefresh();
    }

    @Override
    public void showToastLoadFail() {
        MyToast.showToast(getActivity(),R.string.server_is_busy);
        smartRefreshPerMsg.finishRefresh();
        showDeleteDialog(false);
    }

    @Override
    public void showToastNoNet() {
        MyToast.showToast(getActivity(),R.string.no_internet);
        smartRefreshPerMsg.finishRefresh();
        smartRefreshPerMsg.finishLoadmore();
    }

    @Override
    public void showToastDeleteMsgSuccess(MsgResult msgResult) {
        adapter.setCheckBoxVisiable(false);
        adapter.deleteItem();
        hideMenu();
        MyToast.showToast(getActivity(),R.string.delete_success);
    }

    /**
     * 隐藏删除菜单
     */
    private void hideMenu(){
        //把列表item点击事件设置为非编辑状态
        adapter.setItemClickOnEdit(false);
        rlEdit.setVisibility(View.GONE);
        MyAnimation.fromUpToDownAnim(getActivity(),rlEdit);
        tvEdit.setVisibility(View.VISIBLE);
        MyAnimation.fromDownToUpAnim(getActivity(),tvEdit);
        setEnableUpAndDown(true);
        //隐藏菜单的时候把全选设为false
        adapter.setChooseAll(false);
        //设置上拉和下拉
        setEnableUpAndDown(true);
    }

    /**
     * 显示删除菜单
     */
    private void showMenu(){
        adapter.setItemClickOnEdit(true);
        tvEdit.setVisibility(View.GONE);
        MyAnimation.fromUpToDownAnim(getActivity(),tvEdit);
        rlEdit.setVisibility(View.VISIBLE);
        MyAnimation.fromDownToUpAnim(getActivity(),rlEdit);
        //设置不能上拉和下拉
        setEnableUpAndDown(false);
    }


    @Override
    public void showToastDeleteMsgFail() {
        hideMenu();
        MyToast.showToast(getActivity(),R.string.delete_fail);
        showDeleteDialog(false);
    }

    @Override
    public void showToastChooseDeleteItem() {
        MyToast.showToast(getActivity(),R.string.choose_you_want_delete);
    }

    @Override
    public void showToastNoData() {
        MyToast.showToast(getActivity(),R.string.no_area_data);
        smartRefreshPerMsg.finishLoadmore();
//        smartRefreshPerMsg.finishRefresh();
        MyDialog.dismissDialog();
    }

    @Override
    public void showToastNoRefreshData() {
        MyToast.showToast(getActivity(),R.string.no_data);
        tvEdit.setVisibility(View.GONE);
        smartRefreshPerMsg.finishRefresh();
    }

    private MsgResult.MsgDataBean msgDataBean;
    @Override
    public void loadMore(MsgResult.MsgDataBean msgDataBean) {
        msgInfoBeans = msgDataBean.getInfo();
        this.msgDataBean = msgDataBean;
        int total = msgDataBean.getTotal();
        adapter.loadMore(msgDataBean.getInfo(),total,pageSize,currentPage);
        smartRefreshPerMsg.finishLoadmore();
    }

    @Override
    public void setEnableUpAndDownSuper(boolean enable) {
        setEnableUpAndDown(enable);
    }

    @Override
    public void cancelRefresh() {
        smartRefreshPerMsg.finishRefresh();
    }

    @Override
    public void showDeleteDialog(boolean isShow) {
        if(isShow){
            MyDialog.showDialog(getActivity(),R.string.deleting);
        }else {
            MyDialog.dismissDialog();
        }
    }

    @Override
    public void showToastLoadDataFail() {
        MyToast.showToast(getActivity(),R.string.load_data_fail);
        smartRefreshPerMsg.finishRefresh();
        //showDeleteDialog(false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        currentPage = 1;
        messagePresenter.loadAreaNoticeList(1,currentPage,pageSize,AREA_MSG);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(msgInfoBeans.size() >= pageSize){
            currentPage ++;
        }
        MyLog.print(Tag,"currentPage:"+currentPage+"    msgInfoBeans.size()"+msgInfoBeans.size(),MyLog.PRINT_RED);
        messagePresenter.loadMore(currentPage,pageSize,AREA_MSG);
        smartRefreshPerMsg.finishLoadmore();
    }
}
