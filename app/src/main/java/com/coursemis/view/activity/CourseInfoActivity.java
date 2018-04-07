package com.coursemis.view.activity;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Coursetime;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.SubActivity;
import com.coursemis.view.myView.TitleView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Dialog;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CourseInfoActivity extends Activity {
    public Context context;///

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private int courseid;
    private int teacherid;
    private String coursename;

    //private Button back;
    //private TextView top_title;
    //private Button button_function;
    private TextView name_course;
    private TextView weeknum_course;
    private TextView address_course;
    private TextView daytime_course;
    private AsyncHttpClient client;
    private TitleView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initTitle();
        //teacherid = intent.getExtras().getInt("teacherid");

        //返回按钮
        /*back = (Button) findViewById(R.id.reback_btn);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CourseInfoActivity.this.finish();
            }

        });*/

        /*top_title = (TextView) findViewById(R.id.title);
        top_title.setText("         课程信息           ");
        button_function = (Button) findViewById(R.id.function_btn);*/

        /*button_function.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //定义一个AlertDialog.Builder对象
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //装载/res/layout/dialog_function.xml界面布局
                LinearLayout functionForm = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.dialog_function, null);
                // 设置对话框显示的View对象
                builder.setView(functionForm);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                        create();
                builder.show();
                TextView course_edit = (TextView) functionForm.findViewById(R.id.course_edit);
                TextView course_student_delall = (TextView) functionForm.findViewById(R.id.course_student_delall);
                TextView course_student_manage = (TextView) functionForm.findViewById(R.id.course_student_manage);


                course_edit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(CourseInfoActivity.this, CourseEditActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("courseid", courseid);
                        //bundle.putInt("coursetimeid", coursetimeid);
                        bundle.putInt("teacherid", teacherid);
                        bundle.putString("action", "edit");
                        // 将课程ID作为额外参数传过去
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });
                course_student_delall.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        RequestParams params3 = new RequestParams();
                        params3.put("courseid", courseid + "");
                        params3.put("teacherid", teacherid + "");
                        params3.put("action", "course_teacher");///
                        client.post(HttpUtil.server_student_del_all, params3,
                                new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int arg0, JSONObject arg1) {
                                        // TODO Auto-generated method stub

                                        String delAllStudentOfCourse_msg = arg1.optString("result");
                                        DialogUtil.showDialog(context, delAllStudentOfCourse_msg, true);

                                        super.onSuccess(arg0, arg1);
                                    }

                                });
                    }

                });
                course_student_manage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent2 = new Intent(CourseInfoActivity.this, StudentCourseActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("courseid", courseid);
                        bundle2.putInt("teacherid", teacherid);
                        // 将课程ID作为额外参数传过去
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                    }

                });



				*//*final String[] arrayFunction = new String[] { "编辑", "删除", "学生管理", "学生清除" };

				  Dialog alertDialog = new AlertDialog.Builder(context).
				    //setTitle("你喜欢吃哪种水果？").
				    setIcon(R.drawable.i)
				    .setItems(arrayFunction, new DialogInterface.OnClickListener() {

				     @Override
				     public void onClick(DialogInterface dialog, int which) {
				      switch(which){
				      case 0: //编辑
				    	  Intent intent = new Intent(CourseInfoActivity.this, CourseEditActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("courseid", courseid);
							//bundle.putInt("coursetimeid", coursetimeid);
							bundle.putInt("teacherid", teacherid);
							bundle.putString("action", "edit");
							// 将课程ID作为额外参数传过去
							intent.putExtras(bundle);
							startActivity(intent);

				    	  break;
				      case 1: //删除
				    	  //DialogUtil.showDialog(context, "你确定要将课程"+coursename+"删除吗？", false);
							RequestParams params = new RequestParams();
							params.put("courseid", courseid+"");
							params.put("teacherid", teacherid+"");
							params.put("action", "course_teacher");///
							client.post(HttpUtil.server_course_del, params,
									new JsonHttpResponseHandler() {
										@Override
										public void onSuccess(int arg0, JSONObject arg1) {
											// TODO Auto-generated method stub
											String addCourse_msg = arg1.optString("result");
											DialogUtil.showDialog(context, addCourse_msg, true);

											Intent intent = new Intent(CourseInfoActivity.this, CourseActivity.class);
											Bundle bundle = new Bundle();
											bundle.putInt("teacherid", teacherid);
											intent.putExtras(bundle);
											CourseInfoActivity.this.startActivity(intent);
											CourseInfoActivity.this.finish();

											super.onSuccess(arg0, arg1);
										}

									});
				    	  break;
				      case 2:
				    	  Intent intent2 = new Intent(CourseInfoActivity.this, StudentCourseActivity.class);
							Bundle bundle2 = new Bundle();
							bundle2.putInt("courseid", courseid);
							bundle2.putInt("teacherid", teacherid);
							// 将课程ID作为额外参数传过去
							intent2.putExtras(bundle2);
							startActivity(intent2);


				    	  break;
				      case 3:
				    	  RequestParams params3 = new RequestParams();
							params3.put("courseid", courseid+"");
							params3.put("teacherid", teacherid+"");
							params3.put("action", "course_teacher");///
							client.post(HttpUtil.server_student_del_all, params3,
									new JsonHttpResponseHandler() {
										@Override
										public void onSuccess(int arg0, JSONObject arg1) {
											// TODO Auto-generated method stub

											String delAllStudentOfCourse_msg = arg1.optString("result");
											DialogUtil.showDialog(context, delAllStudentOfCourse_msg, true);

											super.onSuccess(arg0, arg1);
										}

									});

				    	  break;
				      }


				     }
				    }).
				    setNegativeButton("取消", new DialogInterface.OnClickListener() {

				     @Override
				     public void onClick(DialogInterface dialog, int which) {
				      // TODO Auto-generated method stub
				     }
				    }).
				    create();
				  alertDialog.show();*//*

            }

        });*/

    }

    private void initTitle() {
        mTitleView.setTitle("课程信息");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                CourseInfoActivity.this.finish();
            }
        });
        mTitleView.setRightButton("操作", new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                final String[] arrayFunction = new String[]{"编辑", "学生管理"};

                Dialog alertDialog = new AlertDialog.Builder(context)
                        .setIcon(R.drawable.i)
                        .setItems(arrayFunction, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: //编辑
                                        Intent intent = new Intent(CourseInfoActivity.this, CourseEditActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("courseid", courseid);
                                        //bundle.putInt("coursetimeid", coursetimeid);
                                        bundle.putInt("teacherid", teacherid);
                                        bundle.putString("action", "edit");
                                        // 将课程ID作为额外参数传过去
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, SubActivity.COURSEEDIT);
                                        //startActivity(intent);

                                        break;
                                    case 1:
                                        Intent intent2 = new Intent(CourseInfoActivity.this, StudentCourseActivity.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putInt("courseid", courseid);
                                        bundle2.putInt("teacherid", teacherid);
                                        // 将课程ID作为额外参数传过去
                                        intent2.putExtras(bundle2);
                                        startActivity(intent2);


                                        break;
                                }


                            }
                        }).
                                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                }).
                                create();
                alertDialog.show();

            }

        });
    }

    private void initData() {
        this.context = this;
        client = new AsyncHttpClient();

        preferences = getSharedPreferences("courseMis", 0);
        editor = preferences.edit();

        teacherid = preferences.getInt("teacherid", 0);//0为默认值
        Intent intent = getIntent();
        courseid = intent.getExtras().getInt("courseid");


        RequestParams params = new RequestParams();
        //params.put("name", username.getText().toString().trim());
        params.put("courseid", courseid + "");

        //params.put("type", type);//用户类型
        params.put("action", "course_teacher");///

        client.post(HttpUtil.server_course_info, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        // TODO Auto-generated method stub

                        JSONObject object = arg1.optJSONArray("result").optJSONObject(0);
                        //TODO 需要删除的临时性代码
                        if (object == null) {
                            //空数据
                            Toast.makeText(CourseInfoActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                            CourseInfoActivity.this.finish();
                            return;
                        }

                        Coursetime coursetime = new Coursetime();
                        String address = "哈哈哈";
                        object.optString("CtAddress");
                        coursetime.setCtAddress((address == null) ? "忘记填地址了" : address.toString());
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

                        coursename = course.getCName();
                        String[] week = {"单周", "双周", "每周"};
                        String[] day = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

                        name_course.setText(course.getCName());

                        String week_day = week[coursetime.getCtWeekChoose() - 1] + day[coursetime.getCtWeekDay() - 1];
                        weeknum_course.setText(week_day);
                        address_course.setText(coursetime.getCtAddress());
                        String daytime = "第" + coursetime.getCtStartClass() + "节至第" + coursetime.getCtEndClass() + "节";
                        daytime_course.setText(daytime);

                        super.onSuccess(arg0, arg1);
                    }

                });

    }

    private void initView() {
        setContentView(R.layout.activity_course_info);
        mTitleView = (TitleView) findViewById(R.id.course_info_title);
        name_course = (TextView) findViewById(R.id.name_course);
        weeknum_course = (TextView) findViewById(R.id.weeknum_course);
        address_course = (TextView) findViewById(R.id.address_course);
        daytime_course = (TextView) findViewById(R.id.daytime_course);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_info, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SubActivity.COURSEEDIT){
            Log.e("测试","回来了") ;
            initData();
        }
    }
}
