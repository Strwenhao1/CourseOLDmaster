package com.coursemis.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.coursemis.R;
import com.coursemis.model.LocationData;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.util.SubActivity;
import com.coursemis.view.myView.SelectItemView;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class TStartSignInActivity extends Activity {
    private String noticeInfo1 = null;
    private String noticeInfo2 = null;
    private String noticeInfo3 = null;
    private Calendar dt = Calendar.getInstance();

    private SelectItemView btn_3 = null;
    private SelectItemView btn_4 = null;
    private SelectItemView btn_5 = null;
    private SelectItemView btn_6 = null;
    private int tid;
    private AsyncHttpClient client = null;
    private String courseInfo = null;
    private String courseWeek = null;
    private String courseTime = null;
    //private TextView textView_1 = null;
    private int signInHour = 0;
    private int signInMinute = 0;
    private int cid;
    private TitleView mTitle = null;
    private Location currentLocation;
    private String best;
    //private TextView mTv = null;

    private SelectItemView mStartBtn;
    private static int count = 1;
    private Vibrator mVibrator01 = null;
    private LocationClient mLocClient;
    public static String TAG = "LocTestDemo";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initLocation();
        initTitle();
        initData();
        initClick();
    }

    private void initData() {
        client = new AsyncHttpClient();
    }

    private void initClick() {
        mStartBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocClient.isStarted()){
                    mStartBtn.setSignInResultName("定位中...");
                }else {
                    mLocClient.start();
                    mStartBtn.setSignInResultName("定位成功");
                }
                Log.d(TAG, "... mStartBtn onClick... pid=" + Process.myPid() + " count=" + count++);
            }
        });
        btn_3.setOnClickListener(new OnClickListener() {

                                     @Override
                                     public void onClick(View arg0) {
                                         // TODO Auto-generated method stub
                                         SharedPreferences sharedata = getSharedPreferences("courseMis", 0);
                                         tid = Integer.parseInt(sharedata.getString("userID", null));
                                         P.p(" tid     " + tid);
                                         RequestParams params = new RequestParams();
                                         params.put("tid", tid + "");
                                         params.put("action", "search_teacherCourse");
                                         client.post(HttpUtil.server_teacher_course, params,
                                                 new JsonHttpResponseHandler() {

                                                     @Override
                                                     public void onSuccess(int arg0, JSONObject arg1) {
                                                         JSONArray object = arg1.optJSONArray("result");
                                                         P.p(object.toString() + 1111);
                                                         Log.e("选择课程", arg1.toString());
                                                         if (object.length() == 0) {
                                                             Toast.makeText(TStartSignInActivity.this, "您还没教授任何课程!", Toast.LENGTH_SHORT).show();
                                                         } else {
                                                             ArrayList<String> list = new ArrayList<String>();
                                                             for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                                                 JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                                                 P.p(object_temp.toString() + 2222);
                                                                 list.add(i, (object_temp.optInt("CId") + " " + (object_temp.optString("CName"))));
                                                             }

                                                             Intent intent = new Intent(TStartSignInActivity.this, TMentionNameActivity_1.class);
                                                             intent.putStringArrayListExtra("courselist", list);

                                                             startActivityForResult(intent, SubActivity.SUBACTIVITY_1);
                                                         }

                                                         super.onSuccess(arg0, arg1);
                                                     }
                                                 });

                                     }
                                 }
        );


        btn_4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (courseInfo == null) {
                    Toast.makeText(TStartSignInActivity.this, "请先选择课程!", Toast.LENGTH_SHORT).show();
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
                                    Log.e("课程周数", arg1.toString());
                                    if (object.length() == 0) {
                                        Toast.makeText(TStartSignInActivity.this, "查询失败!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        String weekchoose = object.optString("weekchoose");
                                        P.p(weekchoose + "weekchoose");
                                        String weeknumber = object.optString("weeknumber");
                                        P.p(weeknumber + "weeknumber");
                                        Intent intent = new Intent(TStartSignInActivity.this, TMentionNameActivity_2.class);

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


        btn_5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (courseInfo == null || courseWeek == null) {
                    Toast.makeText(TStartSignInActivity.this, "请先选择课程再选择上课周!", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(TStartSignInActivity.this, "查询失败!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ArrayList<String> list = new ArrayList<String>();

                                        for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                            JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);

                                            list.add(i, object_temp.optInt("weekday") + " " + object_temp.optInt("startclass") + " " + object_temp.optInt("endclass"));
                                        }
                                /*intent.putStringArrayListExtra("courselist", list);
                                intent.putExtra("courelist1", list);*/
                                        Intent intent = new Intent(TStartSignInActivity.this, TMentionNameActivity_3.class);
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


        btn_6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                showDialog(1);


            }
        });
    }

    private void initLocation() {
        LocationData.latitude = 0.0;
        LocationData.longitude = 0.0;
        LocationData.radius = 0.0f;
        mLocClient = ((Location) getApplication()).mLocationClient;
        //((Location) getApplication()).mTv = mTv;
        mVibrator01 = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        ((Location) getApplication()).mVibrator01 = mVibrator01;
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double latitude = bdLocation.getLatitude();    //获取纬度信息
                double longitude = bdLocation.getLongitude();    //获取经度信息
                float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f
                String coorType = bdLocation.getCoorType();
                int errorCode = bdLocation.getLocType();
                String city = bdLocation.getCity();
                LocationData.latitude = latitude;
                LocationData.longitude = longitude;
                LocationData.radius = radius;
                mLocClient.stop();
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {

            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        mLocClient.setLocOption(option);
    }

    private void initTitle() {
        mTitle.setTitle("课堂签到");
        mTitle.setLeftButton(R.string.back, new TitleView.OnLeftButtonClickListener() {

            public void onClick(View button) {
                finish();
            }

        });
        mTitle.setRightButton(R.string.signin, new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View arg0) {
                if (courseInfo == null || courseWeek == null || courseTime == null
                        || (signInHour == 0 && signInMinute == 0)
                        || LocationData.latitude == 0.0 || LocationData.longitude == 0
                        || LocationData.radius == 0.0f) {
                    Log.e("测试签到信息", (courseInfo == null) + "..."
                            + (courseWeek == null) + "..."
                            + (courseTime == null) + "..."
                            + (signInHour == 0 && signInMinute == 0) + "..."
                            + (LocationData.latitude == 0.0) + "..."
                            + (LocationData.longitude == 0) + "..."
                            + (LocationData.radius == 0.0));
                    Log.e("测试定位信息", LocationData.latitude + "...." + LocationData.longitude + "...." + LocationData.radius);
                    Toast.makeText(TStartSignInActivity.this, "签到信息没有设置完整!", Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams params = new RequestParams();
                    params.put("cid", courseInfo.substring(0, courseInfo.indexOf(" ")));
                    params.put("signInHour", signInHour + "");
                    params.put("signInMinute", signInMinute + "");
                    params.put("latitude", LocationData.latitude + "");
                    params.put("longitude", LocationData.longitude + "");
                    //将教师Id上传上去
                    params.put("tid", tid);
                    client.post(HttpUtil.server_teacher_SignIn, params,
                            new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int arg0, JSONObject arg1) {
                                }
                            });

                    Toast.makeText(TStartSignInActivity.this, "签到已经开启", Toast.LENGTH_SHORT).show();
                    finish();


                }


            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_tstartsigninactivity);
        mTitle = (TitleView) findViewById(R.id.tStart_signIn);
        //mTv = (TextView) findViewById(R.id.TSignIn_11);
        mStartBtn = (SelectItemView) findViewById(R.id.TeacherStarLoc);
        btn_3 = (SelectItemView) findViewById(R.id.TSignIn_3);
        btn_4 = (SelectItemView) findViewById(R.id.TSignIn_4);
        btn_5 = (SelectItemView) findViewById(R.id.TSignIn_5);
        btn_6 = (SelectItemView) findViewById(R.id.TSignIn_6);
        //textView_1 = (TextView) findViewById(R.id.TSignIn_7);
    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0: // 返回DatePickerDialog对话框
                DatePickerDialog dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                            }
                        }, dt.get(Calendar.YEAR),
                        dt.get(Calendar.MONTH),
                        dt.get(Calendar.DAY_OF_MONTH));
                return dDialog;
            case 1: // 返回TimePickerDialog对话框
                TimePickerDialog tDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                //textView_1.setText(noticeInfo1 + "\r\n" + noticeInfo2 + "\r\n" + noticeInfo3 + "\r\n" + "您设置了 " + hourOfDay + " 小时" + " " + minute + " 分钟");
                                signInHour = hourOfDay;
                                signInMinute = minute;
                                P.p("minute" + minute);
                                P.p("hourOfDay" + hourOfDay);
                                btn_6.setSignInResultName("已设置");
                            }
                        }, dt.get(Calendar.HOUR), dt.get(Calendar.MINUTE), true);
                tDialog.setTitle("设置签到时间");
                return tDialog;
        }
        return null;
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
                    //textView_1.setText(noticeInfo1);
                    btn_3.setSignInResultName(uriData.toString().substring(uriData.toString().indexOf(" ") + 1, uriData.toString().length()));
                }
                break;
            case SubActivity.SUBACTIVITY_2:
                if (resultCode == RESULT_OK) {
                    noticeInfo2 = null;
                    Uri uriData = data.getData();
                    courseWeek = uriData.toString();
                    courseTime = null;
                    noticeInfo2 = "选择课程第" + uriData.toString() + "周";
                    //textView_1.setText(noticeInfo1 + "\r\n" + noticeInfo2);
                    btn_4.setSignInResultName(uriData.toString());
                }
                break;
            case SubActivity.SUBACTIVITY_3:
                if (resultCode == RESULT_OK) {
                    noticeInfo3 = null;
                    Uri uriData = data.getData();
                    courseTime = uriData.toString();
                    noticeInfo3 = "时间" + uriData.toString().substring(2, uriData.toString().length());
                    //textView_1.setText(noticeInfo1 + "\r\n" + noticeInfo2 + "\r\n" + noticeInfo3);
                    btn_5.setSignInResultName(uriData.toString().substring(2, uriData.toString().length()));
                }
                break;

        }

    }


}
