package com.coursemis.view.activity;


import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.model.Teacher;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.util.SubActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.coursemis.service.TSignInService;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 教师登录后的界面
 */
public class WelcomeActivity extends Activity {
    public Context context;            //该activity的上下文？
    private AsyncHttpClient client;    //用于客户端连接服务器端，异步网络数据

    SharedPreferences preferences;     //声明共享首选项

    /*不同功能模块的按钮*/
    private Button button_classActivities;
    private Button button_signin;
    private Button button_homework;
    private Button button_notice;
    private Button button_courseAttendace;
    private Button button_evaluateback;
    private Button button_course;

    private String courseInfo = null;

    private int teacherid;        //记录教师id
    private String type;        //记录用户的类型（学生、教师）
    //private int tid;
    /**
     * 开启服务
     */
    private TSignInService tss = null;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            // TODO Auto-generated method stub
            P.p("这里执行了吗");
            tss = ((TSignInService.LocalBinder) arg1).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // TODO Auto-generated method stub
            tss = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        test() ;
        this.context = this;
        client = new AsyncHttpClient();

        preferences = getSharedPreferences("courseMis", 0);
        teacherid = preferences.getInt("teacherid", 0);//0为默认值
        type = preferences.getString("type", "");

        Toast.makeText(context, "type:" + type, Toast.LENGTH_SHORT).show();

        button_classActivities = (Button) findViewById(R.id.classActivities);
        button_signin = (Button) findViewById(R.id.signin);
        button_homework = (Button) findViewById(R.id.homeworkmanage);
        button_notice = (Button) findViewById(R.id.notice);
        button_evaluateback = (Button) findViewById(R.id.evaluateback);
        button_course = (Button) findViewById(R.id.coursemanage);
        button_courseAttendace = (Button) findViewById(R.id.courseAttendace);

        button_courseAttendace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                P.p(" tid     " + teacherid);
                RequestParams params = new RequestParams();
                params.put("tid", teacherid + "");
                params.put("action", "search_teacherCourse");
                client.post(HttpUtil.server_teacher_course, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                JSONArray object = arg1.optJSONArray("result");
                                P.p(object.toString() + 1111);

                                if (object.length() == 0) {
                                    Toast.makeText(WelcomeActivity.this, "您还没教授任何课程!", Toast.LENGTH_SHORT).show();
                                } else {
                                    final ArrayList<String> list = new ArrayList<String>();
                                    for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                        JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                        P.p(object_temp.toString() + 2222);
                                        list.add(i, (object_temp.optInt("CId") + " " + (object_temp.optString("CName"))));
                                    }

                                    /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    ListView lv = new ListView(WelcomeActivity.this);
                                    ArrayAdapter<String> a = new ArrayAdapter<String>(WelcomeActivity.this,
                                            R.layout.listview_item_1, list);
                                    lv.setAdapter(a);
                                    LinearLayout listViewLayout = new LinearLayout(WelcomeActivity.this);
                                    listViewLayout.setOrientation(LinearLayout.VERTICAL);
                                    listViewLayout.addView(lv, lp);
                                    final AlertDialog dialog = new AlertDialog.Builder(WelcomeActivity.this)
                                            .setTitle("选择课程").setView(listViewLayout)//在这里把写好的这个listview的布局加载dialog中
                                            .create();*/
                                    String[] objects = list.toArray(new String[list.size()]);
                                    AlertDialog dialog = new AlertDialog.Builder(WelcomeActivity.this)
                                            .setTitle("选择课程")
                                            .setItems(objects, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    final Intent serviceIntent = new Intent(WelcomeActivity.this, TSignInService.class);
                                                    bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                                                    courseInfo = list.get(which);
                                                    P.p("1212121"+courseInfo);
                                                    RequestParams params = new RequestParams();
                                                    params.put("courseInfo", courseInfo);
                                                    client.post(HttpUtil.server_teacher_StudentCourse, params,
                                                            new JsonHttpResponseHandler() {
                                                                @Override
                                                                public void onSuccess(int arg0, JSONObject arg1) {
                                                                    JSONArray object = arg1.optJSONArray("result");

                                                                    if (object.length() == 0) {
                                                                        Toast.makeText(WelcomeActivity.this, "你这门课没有学生选修!", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        ArrayList<String> list = new ArrayList<String>();
                                                                        tss.signInServiceInfo(list, arg1);
                                                    /*	list.add(0, "学号"+"    "+" 姓名"+"    "+" 已到次数"+"    "+"总点到次数");
														for(int i=1;i<=arg1.optJSONArray("result").length();i++){
															JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i-1);
															P.p(object_temp.toString()+2222);
															list.add(i, (object_temp.optInt("SNumber")+"    "+object_temp.optString("SName")+"    "+object_temp.optString("SCPointNum")+"    "+object_temp.optString("ScPointTotalNum")));
															}*/

                                                                        Intent intent = new Intent(WelcomeActivity.this, TCourseSignInActivity.class);
                                                                        intent.putStringArrayListExtra("studentCourseSignInInfo", list);
                                                                        P.p("!@#$%^&*@#$%^&*#$%^&");
                                                                        startActivity(intent);
                                                                    }
                                                                    super.onSuccess(arg0, arg1);
                                                                }
                                                            });

                                                    dialog.dismiss();
                                                }
                                            }).create() ;
                                    dialog.show();

                                    /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                            final Intent serviceIntent = new Intent(WelcomeActivity.this, TSignInService.class);
                                            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                                            courseInfo = list.get(arg2);
                                            P.p("1212121");
                                            RequestParams params = new RequestParams();
                                            params.put("courseInfo", courseInfo);
                                            client.post(HttpUtil.server_teacher_StudentCourse, params,
                                                    new JsonHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int arg0, JSONObject arg1) {
                                                            JSONArray object = arg1.optJSONArray("result");

                                                            if (object.length() == 0) {
                                                                Toast.makeText(WelcomeActivity.this, "你这门课没有学生选修!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                ArrayList<String> list = new ArrayList<String>();
                                                                tss.signInServiceInfo(list, arg1);
                                                    *//*	list.add(0, "学号"+"    "+" 姓名"+"    "+" 已到次数"+"    "+"总点到次数");
														for(int i=1;i<=arg1.optJSONArray("result").length();i++){
															JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i-1);
															P.p(object_temp.toString()+2222);
															list.add(i, (object_temp.optInt("SNumber")+"    "+object_temp.optString("SName")+"    "+object_temp.optString("SCPointNum")+"    "+object_temp.optString("ScPointTotalNum")));
															}*//*

                                                                Intent intent = new Intent(WelcomeActivity.this, TCourseSignInActivity.class);
                                                                intent.putStringArrayListExtra("studentCourseSignInInfo", list);
                                                                P.p("!@#$%^&*@#$%^&*#$%^&");
                                                                startActivity(intent);
                                                            }
                                                            super.onSuccess(arg0, arg1);
                                                        }
                                                    });

                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();*/
                                }

                                super.onSuccess(arg0, arg1);
                            }
                        });

            }
        });

        button_signin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(WelcomeActivity.this, TStartSignInActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putSerializable("courseList", (Serializable) courseList);
                bundle.putInt("teacherid", teacherid);
                intent.putExtras(bundle);
                WelcomeActivity.this.startActivity(intent);
            }
        });


        button_homework.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                P.p(" tid     " + teacherid);
                RequestParams params = new RequestParams();
                params.put("tid", teacherid + "");
                params.put("action", "search_teacherCourse");
                client.post(HttpUtil.server_teacher_course, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                JSONArray object = arg1.optJSONArray("result");
                                P.p(object.toString() + 1111);

                                if (object.length() == 0) {
                                    Toast.makeText(WelcomeActivity.this, "您还没教授任何课程!", Toast.LENGTH_SHORT).show();
                                } else {
                                    ArrayList<String> list = new ArrayList<String>();
                                    for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                        JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                        P.p(object_temp.toString() + 2222);
                                        list.add(i, (object_temp.optInt("CId") + " " + (object_temp.optString("CName"))));
                                    }

                                    Intent intent = new Intent(WelcomeActivity.this, HomeworkManageCourseSelectActivity.class);
                                    intent.putStringArrayListExtra("courselist", list);

                                    startActivityForResult(intent, SubActivity.SUBACTIVITY_1);
                                }

                                super.onSuccess(arg0, arg1);
                            }
                        });
            }
        });

        //课堂活动按钮 （1. 点名 ，2 。随机提问  3. 课堂测试（此功能是否实现待定）
        button_classActivities.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //注释的内容为点名功能，此功能作为修改后界面的一个子功能展示
//				Intent i = new Intent(WelcomeActivity.this, TMentionNameActivity.class);//
//				Bundle bundle = new Bundle(); 
//				i.putExtras(bundle);
//				WelcomeActivity.this.startActivity(i);
                //为了避免与courseActivity（课程管理界面同名 。） 使用ClassActivities（课程活动）
                Intent classActivity = new Intent(WelcomeActivity.this, ClassActivities.class);
                WelcomeActivity.this.startActivity(classActivity);

            }

        });

        //这一块是我加的内容
        button_notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, TabMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("teacherid", teacherid);
                intent.putExtras(bundle);
                WelcomeActivity.this.startActivity(intent);
            }
        });

        button_evaluateback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, EvaluateChooseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("teacherid", teacherid);
                intent.putExtras(bundle);
                WelcomeActivity.this.startActivity(intent);
            }

        });


        button_course.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(WelcomeActivity.this, CourseActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putSerializable("courseList", (Serializable) courseList);
                bundle.putInt("teacherid", teacherid);
                intent.putExtras(bundle);
                WelcomeActivity.this.startActivity(intent);
            }
        });
    }
    //TODO 删除
    private void test() {
        Teacher teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        String type = getIntent().getStringExtra("type");
        Intent intent = new Intent(this, NewWelcomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("teacher", teacher);
        bundle.putInt("teacherid", teacher.getTId());
        bundle.putString("type", type);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) ;
        startActivity(intent);
        finish();
    }

    public void ButtonOnclick_ShareMedia(View view) {
        RequestParams params = new RequestParams();
        //params.put("sid", sid+"");
        client.post(HttpUtil.server_getMedia, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONArray object = arg1.optJSONArray("result");

                        if (object.length() == 0) {
                            Toast.makeText(WelcomeActivity.this, "当前没有任何资源共享!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(WelcomeActivity.this, TUploadAudioActivity.class);
                            intent.putStringArrayListExtra("mediainfolist", new ArrayList<String>());
                            startActivity(intent);
                        } else {
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                P.p(object_temp.toString() + 2222);
                                list.add(i, object_temp.optString("smname"));
                            }

                            Intent intent = new Intent(WelcomeActivity.this, TUploadAudioActivity.class);
                            intent.putStringArrayListExtra("mediainfolist", list);
                            startActivity(intent);
                        }


                        super.onSuccess(arg0, arg1);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, 0, "密码修改");
        menu.add(0, 2, 0, "个人信息查看");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case 1:
                Intent i = new Intent(WelcomeActivity.this, PasswordChangeActivity.class);//
                Bundle bundle = new Bundle();
                bundle.putInt("teacherid", teacherid);
                bundle.putString("type", "教师");
                i.putExtras(bundle);
                WelcomeActivity.this.startActivity(i);
                break;
            case 2:
                break;
        }
        return true;
    }
}
