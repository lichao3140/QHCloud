package com.quhwa.cloudintercom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.BoundResult.Device;

import java.util.ArrayList;
import java.util.List;
/**
 * 设备列表适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class MonitorDeviceListAdapter extends BaseAdapter{
	private ArrayList<Device> devices;
	private LayoutInflater inflater;
	private String Tag = "MonitorDeviceListAdapter";
	private Context context;
	public MonitorDeviceListAdapter(Context context, ArrayList<Device> devices) {
		super();
		this.devices = devices;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Device device = devices.get(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_monitor_devicelist, null);
			holder = new ViewHolder();
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_devicelist_item_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
//		if(device.getRoomNo().length()>=17){
//			String roomStr = context.getResources().getString(R.string.room);
//			holder.tvItem.setText(device.getRoomNo().substring(11, 15)+roomStr);
//		}
		String roomStr = context.getResources().getString(R.string.room);
		String roomNum = device.getRoomNo();
//		if(!device.getDeviceAlias().equals("")|| device.getDeviceAlias() != null){
//			holder.tvItem.setText(device.getDeviceAlias());
//		}else{
//			if(!device.getDeviceName().equals("")|| device.getDeviceName() != null){
//				holder.tvItem.setText(device.getDeviceName());
//			}else {
				if(!device.getRoomNo().equals("")|| device.getRoomNo() != null){
					holder.tvItem.setText(roomNum.substring(roomNum.length()-4,roomNum.length())+roomStr);
				}else{
					holder.tvItem.setText("我的房间");
				}
//			}
//		}
		
		return convertView;
	}
	class ViewHolder{
		TextView tvItem;
	}
}
