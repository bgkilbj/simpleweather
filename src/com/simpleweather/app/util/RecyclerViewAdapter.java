package com.simpleweather.app.util;

import java.util.ArrayList;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpleweather.app.R;
import com.simpleweather.app.model.RecyclerViewItem;

public class RecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private ArrayList<RecyclerViewItem> mItems = new ArrayList<RecyclerViewItem>();

	// private OnItemClickListener mListener;
	//
	// public void setOnItemClickListener(OnItemClickListener li) {
	// mListener = li;
	// }
	 public RecyclerViewAdapter(ArrayList<RecyclerViewItem> mItems) {
		    super();
		    this.mItems = mItems;
		  }

//	public void addDatas(ArrayList<RecyclerViewItem> datas) {
//		mItems.addAll(datas);
//		notifyDataSetChanged();
//	}

	@Override
	public int getItemViewType(int position) {
		return mItems.get(position).getType();
	}
	

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,	final int viewType) {
		View v = null;
		RecyclerView.ViewHolder holer = null;
	    switch (viewType) {
	    case RecyclerViewItem.H_TYPE:
	      v = LayoutInflater.from(parent.getContext()).inflate(
	          R.layout.recyclerview_header,parent, false);
	      holer = new HeaderHolder(v);
	      break;
	    case RecyclerViewItem.M_TYPE:
	      v = LayoutInflater.from(parent.getContext()).inflate(
	          R.layout.recyclerview_middler,parent, false);
	      holer = new MiddlerHolder(v);
	      break;
	    case RecyclerViewItem.I_TYPE:
	      v = LayoutInflater.from(parent.getContext()).inflate(
	          R.layout.recyclerview_item,parent, false);
	      holer = new ItemHolder(v);
	      break;
	    }

	    return holer;
	}


	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
		switch (getItemViewType(position)) {
	    case RecyclerViewItem.H_TYPE:
			RecyclerViewItem hItem = mItems.get(position);
			((HeaderHolder)viewHolder).img.setImageResource(hItem.getImg());
			((HeaderHolder)viewHolder).title.setText(hItem.getTitle());
			((HeaderHolder)viewHolder).description.setText(hItem.getDescription());
			((HeaderHolder)viewHolder).temp1.setText(hItem.getTemp1());
			((HeaderHolder)viewHolder).temp2.setText(hItem.getTemp2());
			break;
	    case RecyclerViewItem.M_TYPE:
			RecyclerViewItem mItem = mItems.get(position);//position是否需要更改待检测
			((MiddlerHolder)viewHolder).img.setImageResource(mItem.getImg());
			((MiddlerHolder)viewHolder).title.setText(mItem.getTitle());
			((MiddlerHolder)viewHolder).description.setText(mItem.getDescription());
			((MiddlerHolder)viewHolder).temp1.setText(mItem.getTemp1());
			((MiddlerHolder)viewHolder).temp2.setText(mItem.getTemp2());
			break;
	    case RecyclerViewItem.I_TYPE:
			RecyclerViewItem iItem = mItems.get(position);	
			((ItemHolder) viewHolder).img.setImageResource(iItem.getImg());
			((ItemHolder) viewHolder).title.setText(iItem.getTitle());
			((ItemHolder) viewHolder).description.setText(iItem.getDescription());
			break;

		}

//		if (mListener != null) {
//			viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					mListener.onItemClick(pos, data);
//				}
//			});
//		}
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
		if (manager instanceof GridLayoutManager) {
			final GridLayoutManager gridManager = ((GridLayoutManager) manager);
			gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
			{
						@Override
						public int getSpanSize(int position) {
							int i = 0;
							switch (getItemViewType(position)) {
							case RecyclerViewItem.H_TYPE:
								i = gridManager.getSpanCount();
								break;
							case RecyclerViewItem.M_TYPE:
								i = 1;
								break;
							case RecyclerViewItem.I_TYPE:
								i = gridManager.getSpanCount() / 2;
								break;
							}
							return i;
						}
					});
		}
	}

	@Override
	//StaggeredGridLayoutManager将第一格设为header
	public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
		super.onViewAttachedToWindow(holder);
		ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
		if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
				&& holder.getLayoutPosition() == 0) {
			StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
			p.setFullSpan(true);
		}
	}

//	public int getRealPosition(RecyclerView.ViewHolder holder) {
//		int position = holder.getLayoutPosition();
//		return position;
//	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	class HeaderHolder extends RecyclerView.ViewHolder {
		ImageView img;
		TextView title;
		TextView description;
		TextView temp1;
		TextView temp2;

		public HeaderHolder(View itemView) {
			super(itemView);
			img = (ImageView) itemView.findViewById(R.id.header_image);

			title = (TextView) itemView.findViewById(R.id.header_title);
			description = (TextView) itemView
					.findViewById(R.id.header_description);
			temp1 = (TextView) itemView.findViewById(R.id.header_temp1);
			temp2 = (TextView) itemView.findViewById(R.id.header_temp2);

		}
		}

	class MiddlerHolder extends RecyclerView.ViewHolder {
		ImageView img;
		TextView title;
		TextView description;
		TextView temp1;
		TextView temp2;

		public MiddlerHolder(View itemView) {
			super(itemView);
			img = (ImageView) itemView.findViewById(R.id.middler_image);
			title = (TextView) itemView.findViewById(R.id.middler_title);
			description = (TextView) itemView
					.findViewById(R.id.middler_description);
			temp1 = (TextView) itemView.findViewById(R.id.middler_temp1);
			temp2 = (TextView) itemView.findViewById(R.id.middler_temp2);

		}
		}
	
	class ItemHolder extends RecyclerView.ViewHolder {
			// 每一项的控件
			ImageView img;
			TextView title;
			TextView description;

			public ItemHolder(View itemView) {
				super(itemView);

				img = (ImageView) itemView.findViewById(R.id.item_image);
				title = (TextView) itemView.findViewById(R.id.item_title);
				description = (TextView) itemView
						.findViewById(R.id.item_description);
			}
		}
	// public interface OnItemClickListener<RecyclerViewItem> {
	// void onItemClick(int position, RecyclerViewItem data);
	// }

}
