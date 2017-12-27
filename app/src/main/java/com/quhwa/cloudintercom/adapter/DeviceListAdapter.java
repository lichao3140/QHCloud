package com.quhwa.cloudintercom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.BoundResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备列表适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class DeviceListAdapter extends BaseAdapter{
	private List<BoundResult.Device> devices;

	public List<BoundResult.Device> getDevices() {
		return devices;
	}
	public void setDevices(List<BoundResult.Device> devices){
		this.devices = devices;
	}
	private LayoutInflater inflater;
//	private boolean isShowDeleteItem = false;
//	private boolean isShowAll = false;
	private String Tag = "DeviceListAdapter";
	private Context context;

	public DeviceListAdapter(Context context, ArrayList<BoundResult.Device> devices) {
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
		final BoundResult.Device device = (BoundResult.Device) getItem(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_devicelist, null);
			holder = new ViewHolder();
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_devicelist_item_text);
//			holder.cdDelete = (AppCompatCheckBox) convertView.findViewById(R.id.cb_delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String roomNum = device.getRoomNo();
		String alias = device.getDeviceAlias();
		String deviceName = device.getDeviceName();
		if(!alias.equals("")|| alias != null){
				holder.tvItem.setText(alias);
		}else{
			if(!deviceName.equals("")|| deviceName != null){
				holder.tvItem.setText(deviceName);
			}else {
				if(!roomNum.equals("")|| roomNum != null){
					holder.tvItem.setText(roomNum.substring(roomNum.length()-4,roomNum.length()));
				}else{
					holder.tvItem.setText("室内机");
				}
			}
		}

		return convertView;
	}
	class ViewHolder{
		TextView tvItem;
	}

}
