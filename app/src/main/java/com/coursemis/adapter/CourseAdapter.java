package com.coursemis.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.activity.CourseActivity;
import com.coursemis.view.activity.CourseInfoActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

/**
 * _oo0oo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * 0\  =  /0
 * ___/`---'\___
 * .' \\|     |// '.
 * / \\|||  :  |||// \
 * / _||||| -:- |||||- \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |_/ |
 * \  .-\__  '-'  ___/-. /
 * ___'. .'  /--.--\  `. .'___
 * ."" '<  `.___\_<|>_/___.' >' "".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `_.   \_ __\ /__ _/   .-` /  /
 * =====`-.____`.___ \_____/___.-`___.-'=====
 * `=---='
 * <p>
 * <p>
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Created by zhxchao on 2018/3/1.
 */

public class CourseAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Course> mCourseList;
    private int mTeacherId;
    private AsyncHttpClient client = new AsyncHttpClient();
    ;

    public CourseAdapter(Context context, List<Course> courseList, int teacherid) {
        mContext = context;
        mCourseList = courseList;
        mTeacherId = teacherid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.adapter_course, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.courseName.setText(mCourseList.get(position).getCName());
        myViewHolder.delete.setOnClickListener(new OnDeleteListener(position));
        myViewHolder.courseDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, CourseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("courseid", mCourseList.get(position).getCId());
                // 将课程ID作为额外参数传过去
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView courseName;
        public Button delete;
        public ImageView courseDetail;

        public MyViewHolder(View itemView) {
            super(itemView);
            courseDetail = (ImageView) itemView.findViewById(R.id.adapter_course_detail);
            delete = (Button) itemView.findViewById(R.id.adapter_course_del);
            courseName = (TextView) itemView.findViewById(R.id.adapter_course_name);
        }

    }

    private class OnDeleteListener implements View.OnClickListener {

        int position;

        public OnDeleteListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("确定删除该课程吗").setCancelable(false);
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            RequestParams params = new RequestParams();
                            params.put("courseid", mCourseList.get(position).getCId() + "");
                            params.put("teacherid", mTeacherId + "");
                            client.post(HttpUtil.server_course_del, params,
                                    new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int arg0, JSONObject arg1) {
                                            // TODO Auto-generated method stub
                                            String delCourse_msg = arg1.optString("result");
                                            //DialogUtil.showDialog(mContext, delCourse_msg, true);
                                            final AlertDialog.Builder a = new AlertDialog.Builder(mContext);
                                            a.setMessage(delCourse_msg);
                                            a.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            a.create().show();
                                            CourseActivity courseActivity = (CourseActivity) mContext;
                                            courseActivity.initList();
                                            Log.e("删除课程", delCourse_msg);
                                            //super.onSuccess(arg0, arg1);
                                        }
                                    });
                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }

                    });

            builder.create().show();

        }
    }

}
