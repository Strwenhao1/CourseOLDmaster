package com.coursemis.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.view.activity.TSecondActivity;

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
 * Created by zhxchao on 2018/3/13.
 */

public class FileFragment extends BaseFragment
        implements View.OnClickListener {

    private View mHomework;
    private View mResource;

    @Override
    public void refresh(Course course) {
        Log.e("测试","刷新"+course.getCName()) ;
        mCourse = course ;
        mHomework.setOnClickListener(this);
        mResource.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_file, null);
        initView();
        initData();
        return mView;
    }

    private void initView() {
        mHomework = mView.findViewById(R.id.homework);
        mResource = mView.findViewById(R.id.resource);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("teacher", mTeacher);
        intent.putExtra("course", mCourse);
        intent.setClass(getActivity(), TSecondActivity.class);
        switch (v.getId()) {
            case R.id.homework:
                Log.e("测试", "作业");
                intent.putExtra(TSecondActivity.TYPE, TSecondActivity.HOMEWORK);
                getActivity().startActivity(intent);
                break;
            case R.id.resource:
                Log.e("测试", "资源共享");
                intent.putExtra(TSecondActivity.TYPE, TSecondActivity.RESOURCE);
                getActivity().startActivity(intent);
                break;
        }
    }
}
