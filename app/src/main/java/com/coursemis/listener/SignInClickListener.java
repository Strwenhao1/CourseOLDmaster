package com.coursemis.listener;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.LocationData;
import com.coursemis.model.Teacher;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.view.activity.Location;
import com.coursemis.view.activity.NewWelcomeActivity;
import com.coursemis.view.activity.TCourseSignInActivity;
import com.coursemis.view.myView.SelectItemView;
import com.coursemis.view.myView.SignInListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

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

public class SignInClickListener implements View.OnClickListener {
    private Context mContext;
    private Teacher mTeacher;
    private Course mCourse;
    private AsyncHttpClient client;
    private LocationClient mLocClient;
    private SelectItemView mStartBtn;
    private SelectItemView mCourseWeek;
    private SelectItemView mCourseTime;
    private SelectItemView mSignInLimit;
    private int mCourseWeekSelect;
    private Calendar dt = Calendar.getInstance();
    private Button mOpen;
    private boolean courseTime = false ;
    private int signInHour = 0 ;
    private int signInMinute = 0 ;


    public SignInClickListener(Context context, Teacher teacher, Course course) {
        mContext = context;
        mTeacher = teacher;
        mCourse = course;
        client = new AsyncHttpClient();
    }

    @Override
    public void onClick(View v) {
        String[] list = new String[]{"设置签到", "签到情况"};
        ArrayAdapter<String> arrayAdapter = new
                ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showSetSignIn();
                                break;
                            case 1:
                                showSignInInfo() ;
                                break;
                        }
                    }
                }).create();
        alertDialog.show();

    }

    private void showSignInInfo() {
        RequestParams params = new RequestParams();
        params.put("courseInfo",mCourse.getCId()+" "+mCourse.getCName());
        client.post(HttpUtil.server_teacher_StudentCourse, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONArray object = arg1.optJSONArray("result");
                        if (object.length() == 0) {
                            Toast.makeText(mContext, "你这门课没有学生选修!", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<String> list = new ArrayList<String>();
                            //Log.e("测试","list"+list);
                            signInServiceInfo(list, arg1);
                            SignInListView signInListView = new SignInListView(mContext) ;
                            signInListView.setList(list) ;
                            final AlertDialog dialog = new AlertDialog.Builder(mContext)
                                    .setView(signInListView)
                                    .setCancelable(false)
                                    .create() ;
                            dialog.show();
                            signInListView.mQuit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        super.onSuccess(arg0, arg1);
                    }
                });
    }

    private void showSetSignIn() {
        setLocation();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.view_signin_set, null);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setView(inflate)
                .create();
        alertDialog.show();
        mStartBtn = (SelectItemView) inflate.findViewById(R.id.starLoc);
        mCourseWeek = (SelectItemView) inflate.findViewById(R.id.courseWeek);
        mCourseTime = (SelectItemView) inflate.findViewById(R.id.courseTime);
        mSignInLimit = (SelectItemView) inflate.findViewById(R.id.signInLimit);
        mOpen = (Button) inflate.findViewById(R.id.open);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocClient.isStarted()) {
                    mStartBtn.setSignInResultName("定位中...");
                } else {
                    mLocClient.start();
                    mStartBtn.setSignInResultName("定位成功");
                }
            }
        });
        mCourseWeek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mCourse == null) {
                    Log.e("测试", (mCourse == null) + "");
                    Toast.makeText(mContext, "请先选择课程!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("测试", "mCourseWeek.setOnClickListener" + mCourse.getCId());
                    RequestParams params = new RequestParams();
                    params.put("cid", mCourse.getCId() + "");
                    client.post(HttpUtil.server_teacher_course_week, params,
                            new JsonHttpResponseHandler() {
                                public void onSuccess(int arg0, JSONObject arg1) {
                                    JSONObject object = arg1.optJSONArray("result").optJSONObject(0);
                                    P.p(object.toString() + "1111");
                                    Log.e("课程周数", arg1.toString());
                                    if (object.length() == 0) {
                                        Toast.makeText(mContext, "查询失败!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        String weekchoose = object.optString("weekchoose");
                                        String weeknumber = object.optString("weeknumber");
                                        final ArrayList<String> courseWeek = new ArrayList<String>();
                                        final ArrayList<String> courseWeek_temp = new ArrayList<String>();
                                        switch (Integer.parseInt(weekchoose)) {
                                            case 1:
                                                for (int i = 1; i <= Integer.parseInt(weeknumber); i = i + 2) {
                                                    courseWeek.add(i + "");
                                                    courseWeek_temp.add("第				" + i + "				周");
                                                }
                                                break;
                                            case 2:
                                                for (int i = 2; i <= Integer.parseInt(weeknumber); i = i + 2) {
                                                    courseWeek.add(i + "");
                                                    courseWeek_temp.add("第				" + i + "				周");
                                                }
                                                break;


                                            case 3:
                                                for (int i = 1; i <= Integer.parseInt(weeknumber); i = i + 1) {
                                                    courseWeek.add(i + "");
                                                    courseWeek_temp.add("第				" + i + "				周");
                                                }
                                                break;
                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, courseWeek_temp);
                                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mCourseWeek.setSignInResultName(courseWeek.get(which));
                                                        mCourseWeekSelect = Integer.parseInt(courseWeek.get(which));
                                                    }
                                                }).create();
                                        dialog.show();
                                    }

                                    super.onSuccess(arg0, arg1);
                                }


                            });


                }


            }
        });


        mCourseTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mCourse == null || mCourseWeekSelect == 0) {
                    Toast.makeText(mContext, "请先选择课程再选择上课周!", Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams params = new RequestParams();
                    params.put("cid", mCourse.getCId() + "");
                    client.post(HttpUtil.server_teacher_course_time, params,
                            new JsonHttpResponseHandler() {
                                public void onSuccess(int arg0, JSONObject arg1) {
                                    JSONArray object = arg1.optJSONArray("result");
                                    P.p(object.toString() + "1111");

                                    if (object.length() == 0) {
                                        Toast.makeText(mContext, "查询失败!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        final ArrayList<String> list = new ArrayList<String>();

                                        for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                            JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);

                                            list.add(i, object_temp.optInt("weekday") + " " + object_temp.optInt("startclass") + " " + object_temp.optInt("endclass"));
                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
                                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mCourseTime.setSignInResultName(list.get(which));
                                                        courseTime = true ;
                                                    }
                                                }).create();
                                        dialog.show();

                                    }

                                    super.onSuccess(arg0, arg1);
                                }


                            });


                }
            }
        });


        mSignInLimit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                TimePickerDialog tDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                signInHour = hourOfDay ;
                                signInMinute = minute ;
                                mSignInLimit.setSignInResultName(hourOfDay+":"+minute);
                            }
                        }, dt.get(Calendar.HOUR), dt.get(Calendar.MINUTE), true);
                tDialog.setTitle("设置签到时间");
                tDialog.show();

            }
        });
        mOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCourseWeekSelect == 0 || mCourseTime == null
                        || (signInHour == 0 && signInMinute == 0)
                        || LocationData.latitude == 0.0 || LocationData.longitude == 0
                        || LocationData.radius == 0.0f) {
                    Toast.makeText(mContext, "签到信息没有设置完整!", Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams params = new RequestParams();
                    params.put("cid", mCourse.getCId()+"");
                    params.put("signInHour", signInHour + "");
                    params.put("signInMinute", signInMinute + "");
                    params.put("latitude", LocationData.latitude + "");
                    params.put("longitude", LocationData.longitude + "");
                    //将教师Id上传上去
                    params.put("tid", mTeacher.getTId()+"");
                    client.post(HttpUtil.server_teacher_SignIn, params,
                            new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int arg0, JSONObject arg1) {
                                }
                            });

                    Toast.makeText(mContext, "签到已经开启", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLocation() {
        NewWelcomeActivity activity = (NewWelcomeActivity) mContext;
        mLocClient = ((Location) activity.getApplication()).mLocationClient;
        //((Location) getApplication()).mTv = mTv;
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

    public void signInServiceInfo(ArrayList<String> list, JSONObject arg1) {
        list.add(0, "学号" + "_" + "姓名" + "_" + " 已到次数" + "_" + "总点到次数");
        for (int i = 1; i <= arg1.optJSONArray("result").length(); i++) {
            JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i - 1);
            P.p(object_temp.toString() + 2222);
            list.add(i, (object_temp.optInt("SNumber") + "_" + object_temp.optString("SName") + "_" + object_temp.optString("SCPointNum") + "_" + object_temp.optString("ScPointTotalNum")));
        }
    }
}
