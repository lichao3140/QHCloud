package com.quhwa.cloudintercom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.CallLog;

/**
 * 设备列表适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class CallLogListAdapter extends BaseAdapter{
	private List<CallLog> logs;
	private LayoutInflater inflater;
	
	public CallLogListAdapter(Context context, List<CallLog> logs) {
		super();
		this.logs = logs;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return logs.size();
	}

	@Override
	public Object getItem(int position) {
		return logs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_callog, null);
			holder=new ViewHolder();
			holder.tvRoomNum = (TextView) convertView.findViewById(R.id.tv_roomNum);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.ivCallIcon = (ImageView) convertView.findViewById(R.id.iv_icon_call);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		CallLog log=logs.get(position);
		holder.tvRoomNum.setText(log.getRoomNum());
		holder.tvTime.setText(log.getIncomingTime());
		holder.ivCallIcon.setBackgroundResource(R.drawable.ic_incoming);
		return convertView;
	}
	class ViewHolder{
		TextView tvRoomNum,tvTime;
		ImageView ivCallIcon;
	}

	
}
