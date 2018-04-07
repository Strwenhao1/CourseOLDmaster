package com.coursemis.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Coursetime;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.SubActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Arrays;

public class CourseEditActivity extends Activity {

    public Context context;///
    private AsyncHttpClient client;

    SharedPreferences preferences;

    //private Button back;
    //private TextView top_title;
    //private Button finise_edit;
    private EditText name_course;
    private Button time_course_button;
    private EditText time_course_editText;
    //private Spinner time_course_1;
    private Spinner time_course_2, time_course_3;
    //private ArrayAdapter<String> adapter_1;
    private ArrayAdapter<String> mStartAdapter;
    private static String[] mWeeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static String[] mTimeStart = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private static String[] mTimeEnd = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private EditText address_course;

    private int COURSETIME = 0;
    private int weekday;
    private int startclass;
    private int endclass;

    private int teacherid;
    //一下用于课程编辑
    private String action;
    private int courseid;
    private TitleView mTitleView;
    private ArrayAdapter<String> mEndAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initTitle();
        initAdapterAndListener() ;
    }

    private void initAdapterAndListener() {
        mStartAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTimeStart);
        mStartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mEndAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTimeEnd);
        mEndAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_course_2.setAdapter(mStartAdapter);
        time_course_3.setAdapter(mEndAdapter);
        time_course_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context).setTitle("选择星期数")
                        .setItems(mWeeks, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                time_course_editText.setText(mWeeks[which]);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();


            }
        });
        time_course_2.setOnItemSelectedListener(
                new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        startclass = arg2 + 1;
                        int selectedItemPosition = time_course_2.getSelectedItemPosition();
                        Log.e("选择",selectedItemPosition+"") ;
                        System.arraycopy(mTimeStart,selectedItemPosition,mTimeEnd,0,mTimeStart.length-selectedItemPosition);
                        mTimeEnd = Arrays.copyOf(mTimeStart, mTimeStart.length - selectedItemPosition);
                        Log.e("截取",mTimeEnd.length+"") ;
                        mEndAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });
        time_course_3.setOnItemSelectedListener(
                new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        endclass = arg2 + 1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });


    }

    private void initTitle() {
        mTitleView.setTitle("课程编辑");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                CourseEditActivity.this.finish();
            }
        });
        mTitleView.setRightButton("确定", new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                // TODO Auto-generated method stub
                RequestParams params = new RequestParams();
                params.put("courseid", courseid + "");
                params.put("name_course", name_course.getText().toString().trim());
                params.put("weekday", weekday + "");
                params.put("startclass", startclass + "");
                params.put("endclass", endclass + "");
                params.put("address_course", address_course.getText().toString().trim());
                //DialogUtil.showDialog(context, "address_course: " + address_course.getText().toString(), true);
                client.post(HttpUtil.server_course_edit, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                // TODO Auto-generated method stub
                                CourseEditActivity.this.finishActivity(SubActivity.COURSEEDIT);
                                CourseEditActivity.this.finish();
                                super.onSuccess(arg0, arg1);
                            }

                            @Override
                            public void onFailure(String responseBody, Throwable error) {
                                Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void initData() {
        this.context = this;
        client = new AsyncHttpClient();

        preferences = getSharedPreferences("courseMis", 0);

        teacherid = preferences.getInt("teacherid", 0);//0为默认值

        Intent intent = getIntent();
        courseid = intent.getExtras().getInt("courseid");


        RequestParams params = new RequestParams();
        params.put("courseid", courseid + "");
        params.put("action", "course_info");///

        client.post(HttpUtil.server_course_info, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        // TODO Auto-generated method stub

                        JSONObject object = arg1.optJSONArray("result").optJSONObject(0);
                        Coursetime coursetime = new Coursetime();
                        coursetime.setCtAddress(object.optString("CtAddress").toString());
                        coursetime.setCtEndClass(object.optInt("CtEndClass"));
                        coursetime.setCtId(object.optInt("CtId"));
                        coursetime.setCtStartClass(object.optInt("CtStartClass"));
                        coursetime.setCtWeekChoose(object.optInt("CtWeekChoose"));
                        coursetime.setCtWeekDay(object.optInt("CtWeekDay"));
                        coursetime.setCtWeekNum(object.optInt("CtWeekNum"));

                        Course course = new Course();
                        course.setCId(object.optJSONObject("course").optInt("CId"));
                        course.setCName(object.optJSONObject("course").optString("CName"));
                        coursetime.setCourse(course);

                        name_course.setText(course.getCName());

                        String[] day = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                        weekday = coursetime.getCtWeekDay();
                        String week_day_temp = day[coursetime.getCtWeekDay() - 1];
                        time_course_editText.setText(week_day_temp);

                        //time_course_1.setSelection(coursetime.getCtWeekDay()-1);
                        time_course_2.setSelection(coursetime.getCtStartClass() - 1);
                        time_course_3.setSelection(coursetime.getCtEndClass() - 1);
                        address_course.setText(coursetime.getCtAddress());


                        int selectedItemPosition = time_course_2.getSelectedItemPosition();
                        mTimeEnd = Arrays.copyOf(mTimeStart, mTimeStart.length - selectedItemPosition);
                        time_course_3.setEnabled(true);
                        super.onSuccess(arg0, arg1);
                    }

                });

    }

    private void initView() {
        setContentView(R.layout.activity_course_edit);
        mTitleView = (TitleView) findViewById(R.id.course_edit_title);
        name_course = (EditText) findViewById(R.id.name_course_edit);
        time_course_button = (Button) findViewById(R.id.time_course_button);
        time_course_editText = (EditText) findViewById(R.id.time_course_editText);
        time_course_2 = (Spinner) findViewById(R.id.time_course_2);
        time_course_3 = (Spinner) findViewById(R.id.time_course_3);
        address_course = (EditText) findViewById(R.id.address_course);

        time_course_3.setEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == COURSETIME && resultCode == Activity.RESULT_OK) {
            Bundle bundle = intent.getExtras();
            String resultTime = bundle.getString("time_1");
            weekday = bundle.getInt("weekday_1");
            System.out.println(resultTime);
            time_course_editText.setText(resultTime);
            Log.e("测试","ceshi") ;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_edit, menu);
        return true;
    }

}
