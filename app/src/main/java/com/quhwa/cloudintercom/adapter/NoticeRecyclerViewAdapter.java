package com.quhwa.cloudintercom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.NoticeActivity;
import com.quhwa.cloudintercom.bean.MsgResult;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.TimerUtils;
import com.quhwa.cloudintercom.widget.MyToast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxz on 2017/9/14 0014.
 */

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<MsgResult.MsgDataBean.MsgInfoBean> mMsgInfoBeans;
    private boolean isVisiable = false;
    private List<String> ids = new ArrayList<>();
    /**
     *是否全选
     */
    private boolean isChooseAll =false;
    private boolean isItemClickOnEdit = false;
    public NoticeRecyclerViewAdapter(final Context context, final List<MsgResult.MsgDataBean.MsgInfoBean> msgInfoBeans, final TextView tvChooseAll) {
        this.mContext = context;
        this.mMsgInfoBeans = msgInfoBeans;
        for (int i = 0; i < msgInfoBeans.size(); i++) {
            msgInfoBeans.get(i).setIsChildSelected(false);
        }
        setOnItemClickListener(new NoticeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(isItemClickOnEdit()){
                    MsgResult.MsgDataBean.MsgInfoBean msgInfoBean = msgInfoBeans.get(position);
                    msgInfoBean.setIsChildSelected(!msgInfoBean.isChildSelected());
                    notifyItemChanged(position);
                }else{
                    Intent intent = new Intent(context, NoticeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("msgInfoBean",mMsgInfoBeans.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        tvChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChooseAll = !isChooseAll;
                if(isChooseAll()){
                    tvChooseAll.setText(R.string.choose_nothing);
                }else {
                    tvChooseAll.setText(R.string.choose_all);
                }
                chooseAllOrNone(isChooseAll);
            }
        });

    }

    /**
     * 设置msgInfoBeans集合中的所有MsgInfoBean对象中CheckBox状态
     * @param chooseAll true 为选中状态 false 为未选中状态
     */
    public void chooseAllOrNone(boolean chooseAll) {
        if (mMsgInfoBeans != null && mMsgInfoBeans.size() > 0) {
            for (int i = 0; i < mMsgInfoBeans.size(); i++) {
                mMsgInfoBeans.get(i).setIsChildSelected(chooseAll);
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeRecyclerViewAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_area_notice_info, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //根据位置得到对应的数据
        MsgResult.MsgDataBean.MsgInfoBean msgInfoBean = mMsgInfoBeans.get(position);
        NoticeRecyclerViewAdapter.ViewHolder myHolder = (NoticeRecyclerViewAdapter.ViewHolder) holder;
        myHolder.tvAreaNoticeTitle.setText(msgInfoBean.getTitle());
        String roomNo = msgInfoBean.getRoomNo().toString();
        myHolder.tvRoomNo.setText(roomNo.substring(roomNo.length()-4,roomNo.length())+"室");
        myHolder.tvAreaNoticeTime.setText(TimerUtils.timeLongToDate(msgInfoBean.getCreateTime(),"yyyy/MM/dd hh:mm"));
        MyLog.print("roomNo",msgInfoBean.getRoomNo().toString(),MyLog.PRINT_RED);
        if(getCheckBoxVisiable()){
            myHolder.cbChoose.setVisibility(View.VISIBLE);
        }else {
            myHolder.cbChoose.setVisibility(View.GONE);
        }
        MyAnimation.alphaAnimation(myHolder.cbChoose);
        myHolder.cbChoose.setChecked(msgInfoBean.isChildSelected());
    }

    @Override
    public int getItemCount() {
        return mMsgInfoBeans.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_choose)
        AppCompatCheckBox cbChoose;
        @BindView(R.id.tv_area_notice_title)
        TextView tvAreaNoticeTitle;
        @BindView(R.id.tv_area_notice_time)
        TextView tvAreaNoticeTime;
        @BindView(R.id.tv_roomNo)
        TextView tvRoomNo;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setBackgroundResource(R.drawable.common2_button_bg);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });

        }
    }

    /**
     * 当数据删除成功时，删除列表对应的item
     * @return
     */
    public void deleteItem() {
        if (mMsgInfoBeans != null && mMsgInfoBeans.size() > 0) {
            for (Iterator iterator = mMsgInfoBeans.iterator(); iterator.hasNext(); ) {

                MsgResult.MsgDataBean.MsgInfoBean msgInfoBean = (MsgResult.MsgDataBean.MsgInfoBean) iterator.next();

                if (msgInfoBean.isChildSelected()) {
                    //这行代码放在前面
                    int position = mMsgInfoBeans.indexOf(msgInfoBean);
//                    //1.删除本地缓存的
//                    cartProvider.deleteData(cart);
                    //2.删除当前内存的
                    iterator.remove();
                    //3.刷新数据
                    notifyItemRemoved(position);
                }
            }
            //刷新整个页面
            notifyItemRangeChanged(0,mMsgInfoBeans.size());
        }
    }

    /**
     * 获取要删除的消息id集合
     * @return id list集合
     */
    public List<String> getIdsList(){
        if (mMsgInfoBeans != null && mMsgInfoBeans.size() > 0) {
            ids.clear();
            for (Iterator iterator = mMsgInfoBeans.iterator(); iterator.hasNext(); ) {
                MsgResult.MsgDataBean.MsgInfoBean msgInfoBean = (MsgResult.MsgDataBean.MsgInfoBean) iterator.next();
                if (msgInfoBean.isChildSelected()) {
                    ids.add(msgInfoBean.getId()+"");
                }
            }
        }
        return ids;
    }

    /**
     * 点击RecyclerView某条的监听
     */
    public interface OnItemClickListener {

        /**
         * 当RecyclerView某个被点击的时候回调
         *
         * @param view 点击item的视图
         * @param position 点击得到的数据
         */
        public void onItemClick(View view, int position);

    }

    private NoticeRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    /**
     * 设置RecyclerView某个的监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(NoticeRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置CheckBox是否可见
     * @param isVisiable true:可见 false:不可见
     */
    public void setCheckBoxVisiable(boolean isVisiable){
        this.isVisiable = isVisiable;
    }

    public boolean getCheckBoxVisiable(){
        return isVisiable;
    }

    /**
     * 设置是否全选
     * @param isChooseAll true 为全选，false 为全不选
     */
    public void setChooseAll(boolean isChooseAll) {
        this.isChooseAll = isChooseAll;
    }

    public boolean isChooseAll() {
        return isChooseAll;
    }

    /**
     * 设置处于编辑状态的点击事件
     * @param itemClickOnEdit true:表示处于编辑状态(可以选择checkBox) false:表示处于非编辑状态(可以进行页面跳转)
     */
    public void setItemClickOnEdit(boolean itemClickOnEdit) {
        isItemClickOnEdit = itemClickOnEdit;
    }

    public boolean isItemClickOnEdit() {
        return isItemClickOnEdit;
    }

    /**
     * 刷新
     * @param lastMsgInfoBeans
     */
    public void refresh(List<MsgResult.MsgDataBean.MsgInfoBean> lastMsgInfoBeans) {
//        msgInfoBeans.addAll(lastMsgInfoBeans);
        lastMsgInfoBeans.addAll(mMsgInfoBeans);
        List<MsgResult.MsgDataBean.MsgInfoBean> list = removeDuplicate(lastMsgInfoBeans);
        mMsgInfoBeans.clear();
        mMsgInfoBeans.addAll(list);
        notifyDataSetChanged();
    }
    /**
     *加载更多
     */
    public void loadMore(List<MsgResult.MsgDataBean.MsgInfoBean> newMsgInfoBeans,int total,int pageSize,int currentPage){
        Set<MsgResult.MsgDataBean.MsgInfoBean> set = new HashSet<MsgResult.MsgDataBean.MsgInfoBean>();
        int pageCount = total%pageSize == 0 ? (total/pageSize):(total/pageSize+1);
        if(currentPage <= pageCount){
            mMsgInfoBeans.addAll(newMsgInfoBeans);
            //去重复
            List<MsgResult.MsgDataBean.MsgInfoBean> list = removeDuplicate(mMsgInfoBeans);
            mMsgInfoBeans.clear();
            mMsgInfoBeans.addAll(list);
            notifyDataSetChanged();
        }else{
            MyToast.showToast(mContext,R.string.no_data);
        }
    }
    /**
     * list集合去重
     * @param list
     * @return
     */
    private List<MsgResult.MsgDataBean.MsgInfoBean> removeDuplicate(List<MsgResult.MsgDataBean.MsgInfoBean> list) {
        Set<MsgResult.MsgDataBean.MsgInfoBean> set = new HashSet<MsgResult.MsgDataBean.MsgInfoBean>();
        List<MsgResult.MsgDataBean.MsgInfoBean> newList = new ArrayList<MsgResult.MsgDataBean.MsgInfoBean>();
        for (Iterator<MsgResult.MsgDataBean.MsgInfoBean> iter = list.iterator(); iter.hasNext();) {
            MsgResult.MsgDataBean.MsgInfoBean element = (MsgResult.MsgDataBean.MsgInfoBean) iter.next();
            //利用set集合不会添加重复元素的特性
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }

}
