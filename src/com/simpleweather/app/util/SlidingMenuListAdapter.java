package com.simpleweather.app.util;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simpleweather.app.R;
import com.simpleweather.app.activity.MainActivity;
import com.simpleweather.app.model.County;

public class SlidingMenuListAdapter extends ArrayAdapter<County> {

	private List<County> mcountyArray;// �����б�
	private int resourceId;
	private Context mContext;

	public SlidingMenuListAdapter(Context context, int textViewResourceId,
			List<County> objects) {
		super(context, textViewResourceId, objects);
		this.mContext=context;
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
	 * ��ȡItem��ID
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
			viewHolder.mTextView = (TextView) view.findViewById(R.id.sliding_menu_item_textview);
			viewHolder.mImageButton = (ImageButton) view.findViewById(R.id.sliding_menu_item_imgbutton);

			view.setTag(viewHolder); // ��ViewHolder�洢��View��
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag(); // ���»�ȡViewHolder
		}
		 viewHolder.mTextView.setText(mcounty.getCountyName());
		 viewHolder.mImageButton.setTag(new Integer(position));//
		 viewHolder.mImageButton.setOnClickListener(new View.OnClickListener()
		 {
		 @Override
		 public void onClick(View v)
		 {
			 Integer tmp = (Integer) v.getTag();
			 ((MainActivity)mContext).mMainFragment.removeFragment(tmp);  //ɾ��tmpλ�õ�tab
		 }});
		 
		return view;
	}

	class ViewHolder {
		 TextView mTextView;
		 ImageButton mImageButton;
	}

	public void refreshData(List<County> array) {
		this.mcountyArray = array;
		notifyDataSetChanged();
	}
}
