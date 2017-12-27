package com.quhwa.cloudintercom.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.BoundResult.Device;
import com.quhwa.cloudintercom.utils.MyLog;
/**
 * 设备列表适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class UnlockDeviceListAdapter extends BaseAdapter{
	private List<Device> devices;
	private LayoutInflater inflater;
	private String Tag = "UnlockDeviceListAdapter";
	private Context context;
	public UnlockDeviceListAdapter(Context context, List<Device> devices) {
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
			convertView = inflater.inflate(R.layout.item_devicelist, null);
			holder = new ViewHolder();
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_devicelist_item_text);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.ivIcon.setBackgroundResource(R.drawable.door_device);
//		if(!device.getDeviceAlias().equals("")|| device.getDeviceAlias() != null){
//			holder.tvItem.setText(device.getDeviceAlias());
//		}else{
//			if(!device.getDeviceName().equals("")|| device.getDeviceName() != null){
//				holder.tvItem.setText(device.getDeviceName());
//			}else {
//				if(!device.getRoomNo().equals("")|| device.getRoomNo() != null){
//					holder.tvItem.setText(device.getRoomNo());
//				}else{
//					holder.tvItem.setText("室内机");
//				}
//			}
//		}
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
		ImageView ivIcon;
	}
}
