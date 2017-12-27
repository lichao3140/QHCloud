package com.quhwa.cloudintercom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.bean.Produce;
import com.quhwa.cloudintercom.bean.SmartProduce;

import java.util.List;

/**
 * 智慧家庭产品适配器
 *
 * @author lxz
 * @date 2017年4月13日
 */
public class SmartFamilyAdapter extends BaseAdapter{
	private List<SmartProduce> produces;
	private LayoutInflater inflater;
	private String Tag = "MonitorDeviceListAdapter";
	private Context context;
	public SmartFamilyAdapter(Context context, List<SmartProduce> produces) {
		super();
		this.produces = produces;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return produces.size();
	}

	@Override
	public Object getItem(int position) {
		return produces.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final SmartProduce produce = produces.get(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_smart_family_producelist, null);
			holder = new ViewHolder();
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_pro_title);
			holder.ivItem = (ImageView) convertView.findViewById(R.id.iv_pro);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvItem.setText(produce.getSpName());
//		holder.ivItem.setBackground(produce.getProPicture());
		Glide.with(context)
				.load(produce.getSpRes())//图片地址
				.crossFade()
				.into(holder.ivItem);
		return convertView;
	}
	class ViewHolder{
		TextView tvItem;
		ImageView ivItem;
	}
}
