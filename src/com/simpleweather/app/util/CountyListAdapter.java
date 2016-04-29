package com.simpleweather.app.util;

import java.util.List;

import com.simpleweather.app.R;
import com.simpleweather.app.model.County;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CountyListAdapter extends ArrayAdapter<County> {

	private List<County> mcountyArray;// 标题列表
	private int resourceId;

	public CountyListAdapter(Context context, int textViewResourceId,
			List<County> objects) {
		super(context, textViewResourceId, objects);
		this.mcountyArray=objects;
		resourceId = textViewResourceId;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mcountyArray.size();
	}
	

	@Override
	public County getItem(int position) {
		// TODO Auto-generated method stub
		return mcountyArray.get(position);
	}

	/**
	 * 获取Item的ID
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		County mcounty = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextView = (TextView) view.findViewById(R.id.county_name);
			view.setTag(viewHolder); // 将ViewHolder存储在View中
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
		}
		 viewHolder.mTextView.setText(mcounty.getCountyName()+" , "+mcounty.getCityName()+" , "+mcounty.getProvinceName());
		return view;
	}

	class ViewHolder {
		 TextView mTextView;
	}

	public void refreshData(List<County> array) {
		this.mcountyArray = array;
		notifyDataSetChanged();
	}
}
