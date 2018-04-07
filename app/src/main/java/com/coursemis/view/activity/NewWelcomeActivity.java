package com.coursemis.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Teacher;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.fragment.ContentFragment;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
 * Created by zhxchao on 2018/3/12.
 */

public class NewWelcomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavView;
    private List<Course> mCourseList = new ArrayList<>();
    private Toolbar mToolbar;
    private TextView mTitle;
    private String mType;
    private Teacher mTeacher;
    private DrawerLayout mDrawer;
    private FrameLayout mContent;
    private ContentFragment mContentFragment;
    private Calendar dt = Calendar.getInstance();
    private int signInHour = 0;
    private int signInMinute = 0;
    private boolean mSuccess = false ;
    //private TitleView mNavTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initView();
        initData();
        initNav();

    }

    private void initIntent() {
        mTeacher = (Teacher) getIntent().getSerializableExtra("teacher");
        mType = getIntent().getStringExtra("type");
    }

    private void initData() {
        EventBus.getDefault().register(this);
        getDataFromInternet();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mContentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("teacher", mTeacher);
        mContentFragment.setArguments(bundle);
        transaction.replace(R.id.content, mContentFragment);
        transaction.commit();
    }

    private void getDataFromInternet() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("tid", mTeacher.getTId() + "");
        params.put("action", "search_teacherCourse");
        client.post(HttpUtil.server_teacher_course, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONArray object = arg1.optJSONArray("result");
                        if (object.length() == 0) {
                            Toast.makeText(NewWelcomeActivity.this, "您还没教授任何课程!", Toast.LENGTH_SHORT).show();
                        } else {
                            Menu menu = mNavView.getMenu();
                            menu.clear();
                            mCourseList.clear();
                            for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                Gson gson = new Gson();
                                Course course = gson.fromJson(object_temp.toString(), Course.class);
                                mCourseList.add(course);
                                //Log.e("测试", course.getCName());
                                //menu.add(course.getCName());
                                menu.add(1,i,0,course.getCName()) ;
                            }
                            //menu.addSubMenu("添加课程");
                            menu.add(R.id.course, R.id.add_course, 0, "添加课程");
                            mTitle.setText(mCourseList.get(0).getCName());
                            mContentFragment.setCourse(mCourseList.get(0));
                        }
                        super.onSuccess(arg0, arg1);
                    }
                });
    }

    private void initNav() {
        mNavView.setNavigationItemSelectedListener(this);
        View headerView = mNavView.getHeaderView(0);
        TextView mUserName = (TextView) headerView.findViewById(R.id.userName);
        mUserName.setText(mTeacher.getTName());
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.back, R.string.msg_content);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        mToolbar.setTitleTextColor(mToolbar.getSolidColor());

    }


    private void initView() {
        setContentView(R.layout.activity_new_welcome);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.title);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContent = (FrameLayout) findViewById(R.id.content);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId()>mCourseList.size()){
            //添加课程
            /*Intent i = new Intent(this,CourseAddActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("teacherid", mTeacher.getTId());
            i.putExtras(bundle);
            startActivityForResult(i, SubActivity.SUCCESS);*/
            Intent i = new Intent() ;
            i.putExtra("teacher",mTeacher) ;
            i.putExtra(TSecondActivity.TYPE,TSecondActivity.ADDCOURSE) ;
            i.setClass(this,TSecondActivity.class) ;
            startActivity(i);
        }else {
            String title = item.getTitle().toString();
            mTitle.setText(title);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            int itemId = item.getItemId();
            Course course = mCourseList.get(itemId);
            mContentFragment.setCourse(course);
        }
        return true;
    }

    /**
     * 刷新课程列表的方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if (event.equals("addCourse")){
            //刷新
            Toast.makeText(this,"刷新",Toast.LENGTH_SHORT).show();
            getDataFromInternet();
        }
    }
}
