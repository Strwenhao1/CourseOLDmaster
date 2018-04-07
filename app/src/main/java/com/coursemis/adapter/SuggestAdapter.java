package com.coursemis.adapter;

import java.util.List;

import com.coursemis.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SuggestAdapter extends BaseAdapter{
	
	private List<String> suggests;
	private LayoutInflater inflater;
	
	public SuggestAdapter(Context context,List<String> suggests){
		inflater=LayoutInflater.from(context);
		this.suggests=suggests;
	}

	@Override
	public int getCount() {
		return suggests.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.teacher_course_item,parent,false);
			holder.tv_suggest=(TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		String suggest=suggests.get(position);
		holder.tv_suggest.setText(suggest);
		return convertView;
	}

	/**
	 * 
	 * @author Sivan
	 * @2017-5-9下午5:06:41
	 * 更多建议展示界面的封装
	 *
	 */
	class ViewHolder{
		TextView tv_suggest;
	}
}
