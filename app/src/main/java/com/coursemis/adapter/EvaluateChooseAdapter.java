package com.coursemis.adapter;

import java.util.List;

import com.coursemis.view.activity.EvaluateGetActivity;
import com.coursemis.R;
import com.coursemis.model.Course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EvaluateChooseAdapter extends BaseAdapter {

	private LayoutInflater	inflater;
	private List<Course>	courseList;
	private int				id;
	private Context			context;

	public EvaluateChooseAdapter(Context context, List<Course> courseList, int id){
		this.inflater = LayoutInflater.from(context);
		this.courseList = courseList;
		this.id = id;
		this.context = context;
	}

	@Override
	public int getCount() {
		Log.i("Size===", courseList.size()+"================");
		return courseList.size();
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
		final ViewHolder viewHolder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.teacher_course_item,parent,false);
			viewHolder=new ViewHolder();
			viewHolder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		Course course=courseList.get(position);
		viewHolder.tv_name.setText(course.getCName());
		final int courseid=course.getCId();
		viewHolder.tv_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,EvaluateGetActivity.class);
				Bundle bundle = new Bundle(); 
				bundle.putInt("teacherid", id);
				bundle.putInt("courseid", courseid);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {

		TextView	tv_name;
	}
}
