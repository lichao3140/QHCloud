package com.quhwa.cloudintercom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.bean.BoundResult.Device;

import java.util.List;
/**
 * 修改开锁密码设备列表适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class ModifyUnlockPswAdapter extends BaseAdapter{
	private List<BoundResult.Device> devices;
	private LayoutInflater inflater;
	private String Tag = "ModifyUnlockPswAdapter";
	private Context context;

	public ModifyUnlockPswAdapter(Context context, List<BoundResult.Device> devices) {
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
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_modify_unlock_psw, null);
			holder = new ViewHolder();
			holder.rb = (RadioButton) convertView.findViewById(R.id.rb_indoor);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final Device device = devices.get(position);
		String roomStr = context.getResources().getString(R.string.room);
		holder.rb.setText(device.getRoomNo().substring(11, 15)+roomStr);
		if(devices.get(position).getCheck()){
			holder.rb.setChecked(true);
		}else{
			holder.rb.setChecked(false);
		}
		return convertView;
	}
	class ViewHolder{
		RadioButton rb;
	}
	private String sipId = null;
	public void setSipId(String sipId){
		this.sipId = sipId;
	}
	public String getSipId(){
		return sipId;
	}
}
