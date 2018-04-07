package com.coursemis.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.view.activity.HomeworkManageCourseSelectInfoActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class HomeworkSelectAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<String> mCourseInfo;

    public HomeworkSelectAdapter(Context context, ArrayList<String> courseInfo) {
        mContext = context;
        mCourseInfo = courseInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext)
                .inflate(android.R.layout.simple_list_item_checked, parent, false);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;
        myHolder.checkedTextView.setText(mCourseInfo.get(position));
        myHolder.checkedTextView.setChecked(false);
        //myHolder.checkedTextView.setOnClickListener(new MyOnClickListener());
        myHolder.checkedTextView.setOnClickListener(new MyOnClickListener(myHolder,position));
    }

    @Override
    public int getItemCount() {
        return mCourseInfo.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        public CheckedTextView checkedTextView;

        public MyHolder(View itemView) {
            super(itemView);
            checkedTextView = (CheckedTextView) itemView.findViewById(android.R.id.text1);
        }
    }

    private class MyOnClickListener implements View.OnClickListener{

        private MyHolder myHolder ;
        private int position ;

        public MyOnClickListener(MyHolder holder,int position) {
            myHolder = holder ;
            this.position = position ;
        }

        @Override
        public void onClick(View v) {
            int tid;
            AsyncHttpClient client = new AsyncHttpClient();
            // TODO Auto-generated method stub
            String uriString = "" + mCourseInfo.get(position);
            RequestParams params = new RequestParams();
            SharedPreferences sharedata = mContext.getSharedPreferences("courseMis", 0);
            tid = Integer.parseInt(sharedata.getString("userID", null));
            params.put("tid", tid + "");
            params.put("courseinfo", uriString);
            Log.e("点击", tid+"...."+uriString);
            client.post(HttpUtil.server_teacher_homework_select, params,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int arg0, JSONObject arg1) {
                            JSONArray object = arg1.optJSONArray("result");
                            P.p(object.toString() + 1111);

                            if (object.length() == 0) {
                                Toast.makeText(mContext, "这门课程您还没有布置过作业", Toast.LENGTH_SHORT).show();
                                ArrayList<String> list = new ArrayList<String>();
                                Intent intent = new Intent(mContext, HomeworkManageCourseSelectInfoActivity.class);
                                intent.putStringArrayListExtra("sourcemanagelist", list);
                                intent.putExtra("title", mCourseInfo.get(position));
                                mContext.startActivity(intent);

                            } else {
                                ArrayList<String> list = new ArrayList<String>();
                                for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                    JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                    P.p(object_temp.toString() + 2222);
                                    list.add(i, (object_temp.optInt("smid") + " " + (object_temp.optString("smname") + " " + object_temp.optString("smpath"))));
                                }

                                Intent intent = new Intent(mContext, HomeworkManageCourseSelectInfoActivity.class);
                                intent.putStringArrayListExtra("sourcemanagelist", list);
                                intent.putExtra("title", mCourseInfo.get(position));
                                mContext.startActivity(intent);
                            }

                            super.onSuccess(arg0, arg1);
                        }
                    });
            myHolder.checkedTextView.setChecked(true);
        }
    }
}
