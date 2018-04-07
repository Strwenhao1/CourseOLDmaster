package com.coursemis.view.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Teacher;

import java.util.ArrayList;
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
 * Created by zhxchao on 2018/3/13.
 */

public class ContentFragment extends BaseFragment implements View.OnClickListener {


    private RadioButton mCourseButton;
    private RadioButton mSettingButton;
    private RadioButton mFileButton;
    private BaseFragment mFragment ;
    private ViewPager mViewPager;
    private List<BaseFragment> mFragments ;
    private RadioGroup mRadioGroup;
    //private SwipeRefreshLayout mRefreshView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_content, null);
        initView();
        initFragments() ;
        initData();
        return mView;
    }

    private void initFragments() {
        mFragments = new ArrayList<>() ;
        Bundle bundle = new Bundle() ;
        bundle.putSerializable("teacher",mTeacher);
        CourseFragment courseFragment = new CourseFragment() ;
        FileFragment fileFragment = new FileFragment() ;
        SettingFragment settingFragment = new SettingFragment() ;
        courseFragment.setArguments(bundle);
        fileFragment.setArguments(bundle);
        settingFragment.setArguments(bundle);
        mFragments.add(courseFragment);
        mFragments.add(fileFragment);
        mFragments.add(settingFragment);
    }

    private void initData() {
        mCourseButton.setOnClickListener(this);
        mFileButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
        onClick(mCourseButton);
        PagerAdapter adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        } ;
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View childAt = mRadioGroup.getChildAt(position);
                onClick(childAt);
                //调用refresh 方法
                if (mCourse!=null){
                    BaseFragment baseFragment = mFragments.get(position);
                    baseFragment.refresh(mCourse);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //mRefreshView.setRefreshing(true);
    }

    private void initView() {
        mCourseButton = (RadioButton) mView.findViewById(R.id.course);
        mFileButton = (RadioButton) mView.findViewById(R.id.file);
        mSettingButton = (RadioButton) mView.findViewById(R.id.setting);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        mRadioGroup = (RadioGroup) mView.findViewById(R.id.function);
        //mRefreshView = (SwipeRefreshLayout) mViewPager.findViewById(R.id.refresh);
    }

    public void setCourse(Course course) {
        if (course == null) {
            //出现了问题
            Toast.makeText(getActivity(), "出现了点问题", Toast.LENGTH_SHORT).show();
        } else {
            mCourse = course;
            int currentItem = mViewPager.getCurrentItem();
            BaseFragment baseFragment = mFragments.get(currentItem);
            baseFragment.refresh(mCourse);
            Log.e("测试",mCourse.getCId()+"") ;
            //mRefreshView.setRefreshing(false);
            //请求网络
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course:
                mCourseButton.setChecked(true);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.file:
                mFileButton.setChecked(true);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.setting:
                mSettingButton.setChecked(true);
                mViewPager.setCurrentItem(2);
                break;
        }

    }

    @Override
    public void refresh(Course course) {
        int currentItem = mViewPager.getCurrentItem();
        BaseFragment baseFragment = mFragments.get(currentItem);
        baseFragment.refresh(course);
    }
}
