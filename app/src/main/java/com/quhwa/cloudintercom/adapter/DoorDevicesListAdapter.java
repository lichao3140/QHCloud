package com.quhwa.cloudintercom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
/**
 * 修改开锁密码设备列表适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class DoorDevicesListAdapter extends BaseAdapter{
	private List<String> doorDevicesList;
	private LayoutInflater inflater;
	private String Tag = "DoorDevicesListAdapter";
	private Context context;
	public DoorDevicesListAdapter(Context context, List<String> doorDevicesList) {
		super();
		this.doorDevicesList = doorDevicesList;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return doorDevicesList.size();
	}

	@Override
	public Object getItem(int position) {
		return doorDevicesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_door_list, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv_door_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final String door = doorDevicesList.get(position);
		String doorStr = context.getResources().getString(R.string.door_device);
		holder.tv.setText(door.substring(door.length()-2, door.length())+doorStr);
		return convertView;
	}
	class ViewHolder{
		TextView tv;
	}
}
