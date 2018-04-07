package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Coursetime;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.myView.TitleView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoursetableActivity extends Activity {
    public Context context;
    private AsyncHttpClient client;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private int studentid;

    private TableLayout layout;
    private Button back;
    private TextView top_title;

    private String[][] title;
    String[] courseinfo;
    private TitleView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initData();
    }

    /**
     * 初始化标题
     */
    private void initTitleInfo() {
        mTitle.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                CoursetableActivity.this.finish();
            }
        });
        mTitle.setTitle("课程表");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        this.context = this;
        client = new AsyncHttpClient();
        preferences = getSharedPreferences("courseMis", 0);
        editor = preferences.edit();
        studentid = preferences.getInt("studentid", 0);//0为默认值
        RequestParams params = new RequestParams();
        params.put("studentid", studentid + "");
        params.put("action", "course_student");///

        //设置RecyclerView
        client.post(HttpUtil.server_course_student, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        showCourseTable(arg0,arg1) ;
                        super.onSuccess(arg0, arg1);
                    }

                });
    }

    /**
     * 展示课程表
     * @param arg0
     * @param arg1
     */
    private void showCourseTable(int arg0, JSONObject arg1) {
        // TODO Auto-generated method stub
        List<Course> courseList = new ArrayList<Course>();
        List<Coursetime> coursetimeList = new ArrayList<Coursetime>();

        for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
            JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
            Log.e("课表", object.toString());
            Gson gson = new Gson();
            Course course = gson.fromJson(object.toString(), Course.class);
            Coursetime coursetime = gson.fromJson(object.toString(), Coursetime.class);
            courseList.add(course);
            coursetimeList.add(coursetime);
        }
        if (courseList.size() > 0) {
            String[] firstrow = new String[]{
                    "课程名称", "上课时间", "上课地点"
            };
            TableRow rowfirst = new TableRow(context);
            for (int i = 0; i < 3; i++) {
                TextView textView = new TextView(context);
                textView.setText(firstrow[i]);
                textView.setGravity(Gravity.CENTER);      //居中
                textView.getPaint().setFakeBoldText(true);//加粗
                textView.setTextColor(0xFFFFB6C1);
                textView.setTextSize(18);
                textView.setBackgroundResource(R.drawable.table_shape);
                rowfirst.addView(textView);
            }
            //rowfirst.setBackgroundColor(0xffff7777);
            layout.addView(rowfirst);
        } else {
            TableRow rowfirst = new TableRow(context);
            TextView textView = new TextView(context);
            textView.setText("该学生没有课程！");
            rowfirst.addView(textView);
            layout.addView(rowfirst);
        }

        for (int i = 0; i < courseList.size(); i++) {
            TableRow row = new TableRow(context);
            TextView cname = new TextView(context);
            cname.setText(courseList.get(i).getCName());

            TextView ctime = new TextView(context);
            String[] week = {"单周", "双周", "每周"};
            String[] day = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
            String week_day = week[coursetimeList.get(i).getCtWeekChoose() - 1] + day[coursetimeList.get(i).getCtWeekDay() - 1];
            String daytime = "第" + coursetimeList.get(i).getCtStartClass() + "节至第" + coursetimeList.get(i).getCtEndClass() + "节";
            ctime.setText(week_day + daytime);

            TextView caddress = new TextView(context);
            caddress.setText(coursetimeList.get(i).getCtAddress());

            cname.setBackgroundResource(R.drawable.table_shape);
            ctime.setBackgroundResource(R.drawable.table_shape);
            caddress.setBackgroundResource(R.drawable.table_shape);
            cname.setTextColor(0xff9932CC);
            ctime.setTextColor(0xff9932CC);
            caddress.setTextColor(0xff9932CC);
            row.addView(cname);
            row.addView(ctime);
            row.addView(caddress);
            //row.setBackgroundColor(0x5555ffff);
            layout.addView(row);

        }
    }
    /**
     * 初始化界面
     */
    private void initView() {
        setContentView(R.layout.activity_coursetable);
        layout = (TableLayout) findViewById(R.id.table);
//        mTitle = (TitleView) findViewById(R.id.courseTableTitle);
        initTitleInfo() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.coursetable, menu);
        return true;
    }

}
