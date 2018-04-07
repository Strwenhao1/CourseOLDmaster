package com.coursemis.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.View;

import com.coursemis.model.Course;
import com.coursemis.model.Teacher;

import java.io.Serializable;

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

public abstract class BaseFragment extends Fragment {
    protected View mView;
    protected Teacher mTeacher;
    protected Course mCourse;
    public abstract void refresh(Course course) ;
    @CallSuper
    @Override
    public void setArguments(Bundle args) {
        mTeacher = (Teacher) args.getSerializable("teacher");
        Serializable course = args.getSerializable("course");
        if (course!=null){
            mCourse = (Course) course;
        }
    }
}
