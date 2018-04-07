package com.coursemis.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
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
 *作业管理的界面
 * Created by zhxchao on 2018/3/18.
 */

public class FileHomeworkFragment extends BaseFragment {
    @Override
    public void refresh(Course course) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_file_homework, null);
        initView() ;
        initData() ;
        return mView;
    }

    private void initData() {
        Log.e("测试","初始化数据"+(mCourse == null)) ;
        AsyncHttpClient client = new AsyncHttpClient() ;
        RequestParams params = new RequestParams();
        params.put("tid", mTeacher.getTId() + "");
        params.put("courseinfo", mCourse.getCId()+" "+mCourse.getCName());
        //Log.e("点击", tid+"...."+uriString);
        client.post(HttpUtil.server_teacher_homework_select, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONArray object = arg1.optJSONArray("result");
                        P.p(object.toString() + 1111);

                        if (object.length() == 0) {
                            Toast.makeText(getActivity(), "这门课程您还没有布置过作业", Toast.LENGTH_SHORT).show();
                            /*ArrayList<String> list = new ArrayList<String>();
                            Intent intent = new Intent(getActivity(), HomeworkManageCourseSelectInfoActivity.class);
                            intent.putStringArrayListExtra("sourcemanagelist", list);
                            intent.putExtra("title", mCourseInfo.get(position));
                            mContext.startActivity(intent);*/

                        } else {
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                P.p(object_temp.toString() + 2222);
                                list.add(i, (object_temp.optInt("smid") + " " + (object_temp.optString("smname") + " " + object_temp.optString("smpath"))));
                            }

                            /*Intent intent = new Intent(mContext, HomeworkManageCourseSelectInfoActivity.class);
                            intent.putStringArrayListExtra("sourcemanagelist", list);
                            intent.putExtra("title", mCourseInfo.get(position));
                            mContext.startActivity(intent);*/
                        }

                        super.onSuccess(arg0, arg1);
                    }
                });
    }

    private void initView() {
        
    }
}
