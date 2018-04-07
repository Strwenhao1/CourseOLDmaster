package com.coursemis.view.activity;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.view.myView.SelectItemView;
import com.coursemis.view.myView.TitleView;
import com.coursemis.R;
import com.coursemis.util.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.coursemis.service.TSignInService;


public class TMentionNameActivity extends Activity {
    private String course = null;
    private String week = null;
    private String time = null;
    private SelectItemView SelectCourse = null;
    private SelectItemView selectWeek = null;
    private SelectItemView selectTime = null;
    private Button class_Mention = null;
    private AsyncHttpClient client = null;
    private Context context;
    private String courseInfo = null;
    private String courseWeek = null;
    private String courseTime = null;
    //private TextView textView_1 = null;
    private TextView textView_2 = null;
    private TextView textView_3 = null;
    int tid;
    private String noticeInfo1 = null;
    private String noticeInfo2 = null;
    private String noticeInfo3 = null;
    private TSignInService tss = null;
    private TitleView mTitle = null;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmentionaameactivity);

        mTitle = (TitleView) findViewById(R.id.title_dianming1);
        mTitle.setTitle("课堂点名");
        mTitle.setLeftButton(R.string.back, new TitleView.OnLeftButtonClickListener() {

            public void onClick(View button) {
                finish();
            }

        });
        mTitle.setRightButton(R.string.classmention, new TitleView.OnRightButtonClickListener() {

            public void onClick(View button) {
                // TODO Auto-generated method stub
                final Intent serviceIntent = new Intent(TMentionNameActivity.this, TSignInService.class);
                bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);


                if (courseInfo == null || courseWeek == null || courseTime == null) {
                    Toast.makeText(TMentionNameActivity.this, "您的点到课程信息还没有选择", Toast.LENGTH_SHORT).show();
                } else {
                    int k = courseInfo.indexOf(" ");
                    String cid = courseInfo.substring(0, k);

                    RequestParams params = new RequestParams();
                    params.put("cid", cid + "");
                    client.post(HttpUtil.server_teacher_course_studentNames, params,
                            new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int arg0, JSONObject arg1) {
                                    JSONArray object = arg1.optJSONArray("result");
                                    P.p(object.toString() + 1111);

                                    if (object.length() == 0) {
                                        Toast.makeText(TMentionNameActivity.this, "您这门课没有学生选修!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ArrayList<String> list = new ArrayList<String>();
                                        for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                            JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                            P.p(object_temp.toString() + 2222);
                                            list.add(i, (object_temp.optInt("scId") + " " + object_temp.optInt("SNumber") + (object_temp.optString("SName"))));
                                        }

                                        Intent intent = new Intent(TMentionNameActivity.this, TMentionNameActivity_4.class);
                                        intent.putStringArrayListExtra("course_Name_list", list);
                                        intent.putExtra("tmn_c_info", courseInfo.substring(2, courseInfo.length()) + " 第" + courseWeek + "周" + " 上课时间" + courseTime.substring(2, courseTime.length()));

                                        startActivity(intent);
                                    }

                                    super.onSuccess(arg0, arg1);
                                }
                            });


                }
            }
        });


        SelectCourse = (SelectItemView) findViewById(R.id.SelectCourse_1);
        selectWeek = (SelectItemView) findViewById(R.id.MentionNameWeeks);
        selectTime = (SelectItemView) findViewById(R.id.MentionNameTime);


        client = new AsyncHttpClient();
        this.context = this;


        SelectCourse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedata = getSharedPreferences("courseMis", 0);
                tid = Integer.parseInt(sharedata.getString("userID", null));
                RequestParams params = new RequestParams();
                params.put("tid", tid + "");
                params.put("action", "search_teacherCourse");
                client.post(HttpUtil.server_teacher_course, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                JSONArray object = arg1.optJSONArray("result");
                                P.p(object.toString() + 1111);

                                if (object.length() == 0) {
                                    Toast.makeText(TMentionNameActivity.this, "您还没教授任何课程!", Toast.LENGTH_SHORT).show();
                                } else {
                                    ArrayList<String> list = new ArrayList<String>();
                                    for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                        JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                        P.p(object_temp.toString() + 2222);
                                        list.add(i, (object_temp.optInt("CId") + " " + (object_temp.optString("CName"))));
                                    }

                                    Intent intent = new Intent(TMentionNameActivity.this, TMentionNameActivity_1.class);
                                    intent.putStringArrayListExtra("courselist", list);

                                    startActivityForResult(intent, SubActivity.SUBACTIVITY_1);
                                }

                                super.onSuccess(arg0, arg1);
                            }
                        });

            }

        });

        selectWeek.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (courseInfo == null) {
                    Toast.makeText(TMentionNameActivity.this, "请先选择课程!", Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams params = new RequestParams();
                    int k = courseInfo.indexOf(" ");
                    String cid = "";
                    for (int i = 0; i < k; i++) {
                        cid = courseInfo.charAt(i) + cid;
                    }
                    P.p(cid + "cid");
                    params.put("cid", cid);
                    client.post(HttpUtil.server_teacher_course_week, params,
                            new JsonHttpResponseHandler() {
                                public void onSuccess(int arg0, JSONObject arg1) {
                                    JSONObject object = arg1.optJSONArray("result").optJSONObject(0);
                                    P.p(object.toString() + "1111");

                                    if (object.length() == 0) {
                                        Toast.makeText(TMentionNameActivity.this, "查询失败!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        String weekchoose = object.optString("weekchoose");
                                        P.p(weekchoose + "weekchoose");
                                        String weeknumber = object.optString("weeknumber");
                                        P.p(weeknumber + "weeknumber");
                                        Intent intent = new Intent(TMentionNameActivity.this, TMentionNameActivity_2.class);

                                        intent.putExtra("weekchoose", weekchoose);
                                        intent.putExtra("weeknumber", weeknumber);
                                        startActivityForResult(intent, SubActivity.SUBACTIVITY_2);


                                    }

                                    super.onSuccess(arg0, arg1);
                                }


                            });


                }


            }
        });

        selectTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (courseInfo == null || courseWeek == null) {
                    Toast.makeText(TMentionNameActivity.this, "请先选择课程再选择上课周!", Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams params = new RequestParams();
                    int k = courseInfo.indexOf(" ");
                    String cid = "";
                    for (int i = 0; i < k; i++) {
                        cid = courseInfo.charAt(i) + cid;
                    }
                    P.p(cid + "cid");
                    params.put("cid", cid);
                    client.post(HttpUtil.server_teacher_course_time, params,
                            new JsonHttpResponseHandler() {
                                public void onSuccess(int arg0, JSONObject arg1) {
                                    JSONArray object = arg1.optJSONArray("result");
                                    P.p(object.toString() + "1111");

                                    if (object.length() == 0) {
                                        Toast.makeText(TMentionNameActivity.this, "查询失败!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ArrayList<String> list = new ArrayList<String>();

                                        for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                            JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);

                                            list.add(i, object_temp.optInt("weekday") + " " + object_temp.optInt("startclass") + " " + object_temp.optInt("endclass"));
                                        }
                                /*intent.putStringArrayListExtra("courselist", list);
								intent.putExtra("courelist1", list);*/
                                        Intent intent = new Intent(TMentionNameActivity.this, TMentionNameActivity_3.class);
                                        intent.putStringArrayListExtra("cousetime_list", list);
                                        intent.putExtra("courelist1", list);
                                        P.p("cousetime size" + list.size());
                                        startActivityForResult(intent, SubActivity.SUBACTIVITY_3);

                                    }

                                    super.onSuccess(arg0, arg1);
                                }


                            });


                }


            }
        });


    }


    private void buildDialog(int x, int y) {
        final Dialog dialog = new Dialog(this);
        P.p(1 + "");
        View view = getLayoutInflater().inflate(R.layout.activity_tmentionnameactivity_2, null); //xxx为你要显示的布局文件
        P.p(2 + "");
        dialog.setContentView(view);
        P.p(3 + "");
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        P.p(4 + "");
        switch (x) {
            case 1:
                for (int i = 1; i <= y; i = i + 2) {
                    final Button btn = new Button(this);
                    btn.setText(i + "");
                    btn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            week = (String) btn.getText();
                            textView_2.setText("选择课程 第" + week + "周");
                            dialog.dismiss();


                        }
                    });


                    P.p(5 + "");
                    dialog.addContentView(view, lp1);
                    P.p(6 + "");
                    dialog.show();

                }
                ;
                break;
            case 2:
                for (int i = 2; i <= y; i = i + 2) {
                    final Button btn = new Button(this);
                    btn.setText(i + "");
                    btn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            week = (String) btn.getText();
                            textView_2.setText("选择课程 第" + week + "周");
                            dialog.dismiss();


                        }
                    });


                    dialog.addContentView(view, lp1);
                    dialog.show();

                }
                ;
                break;


            case 3:
                for (int i = 1; i <= y; i = i + 1) {
                    final Button btn = new Button(this);
                    btn.setText(i + "");
                    btn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            week = (String) btn.getText();
                            textView_2.setText("选择课程 第" + week + "周");
                            dialog.dismiss();


                        }
                    });


                    dialog.addContentView(view, lp1);
                    dialog.show();

                }
                ;
                break;


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SubActivity.SUBACTIVITY_1:
                if (resultCode == RESULT_OK) {
                    noticeInfo1 = null;
                    Uri uriData = data.getData();
                    courseInfo = uriData.toString();
                    courseWeek = null;
                    courseTime = null;
                    noticeInfo1 = "选择" + uriData.toString().substring(uriData.toString().indexOf(" ") + 1, uriData.toString().length()) + "课程";
                    SelectCourse.setSignInResultName(uriData.toString().substring(uriData.toString().indexOf(" ") + 1, uriData.toString().length()) );
                }
                break;
            case SubActivity.SUBACTIVITY_2:
                if (resultCode == RESULT_OK) {
                    noticeInfo2 = null;
                    Uri uriData = data.getData();
                    courseWeek = uriData.toString();
                    courseTime = null;
                    noticeInfo2 = "选择课程第" + uriData.toString() + "周";
                    selectWeek.setSignInResultName(uriData.toString());
                }
                ;
                break;
            case SubActivity.SUBACTIVITY_3:
                if (resultCode == RESULT_OK) {
                    noticeInfo3 = null;
                    Uri uriData = data.getData();
                    courseTime = uriData.toString();
                    noticeInfo3 = "时间" + uriData.toString().substring(2, uriData.toString().length());
                    selectTime.setSignInResultName(uriData.toString().substring(2, uriData.toString().length()));
                }
                break;

        }

    }


}
