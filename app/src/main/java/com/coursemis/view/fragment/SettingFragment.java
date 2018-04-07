package com.coursemis.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class SettingFragment extends BaseFragment
        implements View.OnClickListener {

    private Button mCourseSetting;
    //private Button mAddCourse;
    private Button mStudentManager;
    private Button mChangePassword;

    @Override
    public void refresh(Course course) {
        mCourse = course ;
        mCourseSetting.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mStudentManager.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, null);
        initView();
        initData();
        return mView;
    }

    private void initData() {

    }

    private void initView() {
        mCourseSetting = (Button) mView.findViewById(R.id.courseSetting);
        mChangePassword = (Button) mView.findViewById(R.id.changePassword);
        mStudentManager = (Button) mView.findViewById(R.id.studentManager);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent() ;
        intent.putExtra("teacher",mTeacher) ;
        intent.putExtra("course",mCourse) ;
        intent.setClass(getActivity(), TSecondActivity.class) ;
        switch (v.getId()){
            case R.id.courseSetting:
                intent.putExtra(TSecondActivity.TYPE, TSecondActivity.COURSESETTING) ;
                startActivity(intent);
                break;
            case R.id.changePassword :
                intent.putExtra(TSecondActivity.TYPE, TSecondActivity.CHANGEPASSWORD) ;
                startActivity(intent);
                break;
            case R.id.studentManager:
                intent.putExtra(TSecondActivity.TYPE, TSecondActivity.STUDENTMANAGER) ;
                startActivity(intent);
                break;
        }
    }

}
