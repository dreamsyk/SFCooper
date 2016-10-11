package com.nh.sfcooper.adapter;

import java.util.List;

import com.nh.sfcooper.R;
import com.nh.sfcooper.model.OperateItemData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OpreaterHistoryAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<OperateItemData> data;
	
	public OpreaterHistoryAdapter(Context context,List<OperateItemData> data){

		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;

	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.oprate_history_item, parent, false);
			
			holder = new ViewHolder();
			
			//holder.img = (ImageView) convertView.findViewById(R.id.history_item_icon);
			holder.title = (TextView) convertView.findViewById(R.id.history_item_title);
			holder.time = (TextView) convertView.findViewById(R.id.history_item_time);
			holder.content = (TextView) convertView.findViewById(R.id.history_item_content_detail);
			
			convertView.setTag(holder);
			
		} else {
			
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
			
		}
		
		
		 OperateItemData item = data.get(position);
		
		 holder.title.setText("查单记录 # " + (position + 1));
		 holder.time.setText(item.getTime());
		 holder.content.setText("运单号: " + item.getContent());
		
		return convertView;
	}
	
	private class ViewHolder {
		//public ImageView img;
		public TextView title;
		public TextView time;
		public TextView content;
	}
	
	private void resetViewHolder(ViewHolder holder) {
		
		if (holder != null) {
			// holder.img.setBackground(null);
			holder.title.setText("");
			holder.time.setText("");
			holder.content.setText("");
		}
	}
}
