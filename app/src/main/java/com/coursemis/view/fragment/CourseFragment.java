package com.coursemis.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.listener.SignInClickListener;
import com.coursemis.model.AskStudent;
import com.coursemis.model.Course;
import com.coursemis.model.Coursetime;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.view.activity.TAskQuestionActivity;
import com.coursemis.view.activity.TSecondActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class CourseFragment extends BaseFragment
        implements View.OnClickListener {

    private AsyncHttpClient client;
    private TextView name_course;
    private TextView weeknum_course;
    private TextView address_course;
    private TextView daytime_course;
    private Coursetime mCoursetime;
    private String[] mDay;
    private String[] mWeek;
    private SwipeRefreshLayout mRefresh;
    private Button mQuiz;
    private Button mCallName;
    private Button mSignIn;
    private Button mFeedBack;

    @Override
    public void refresh(Course course) {
        mCourse = course;
        mSignIn.setOnClickListener(new SignInClickListener(getActivity(), mTeacher, mCourse));
        mCallName.setOnClickListener(this);
        mQuiz.setOnClickListener(this);
        mFeedBack.setOnClickListener(this);
        refresh();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_course, null);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        client = new AsyncHttpClient();
        mWeek = new String[]{"单周", "双周", "每周"};
        mDay = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mCourse != null) {
                    refresh();
                }
            }
        });

    }

    private void initView() {
        name_course = (TextView) mView.findViewById(R.id.name_course);
        weeknum_course = (TextView) mView.findViewById(R.id.weeknum_course);
        address_course = (TextView) mView.findViewById(R.id.address_course);
        daytime_course = (TextView) mView.findViewById(R.id.daytime_course);
        mRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.refresh);
        mQuiz = (Button) mView.findViewById(R.id.quiz);
        mCallName = (Button) mView.findViewById(R.id.callName);
        mSignIn = (Button) mView.findViewById(R.id.signIn);
        mFeedBack = (Button) mView.findViewById(R.id.feedBack);
    }

    @Override
    public void onStart() {
        if (mCoursetime != null) {
            name_course.setText(mCoursetime.getCourse().getCName());
            String week_day = mWeek[mCoursetime.getCtWeekChoose() - 1] + mDay[mCoursetime.getCtWeekDay() - 1];
            weeknum_course.setText(week_day);
            address_course.setText(mCoursetime.getCtAddress());
            String daytime = "第" + mCoursetime.getCtStartClass() + "节至第" + mCoursetime.getCtEndClass() + "节";
            daytime_course.setText(daytime);
        }
        super.onStart();
    }

    private void refresh() {
        Log.e("测试1", mCourse.getCId() + "");
        RequestParams params = new RequestParams();
        params.put("courseid", mCourse.getCId() + "");
        params.put("action", "course_teacher");///
        client.post(HttpUtil.server_course_info, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONObject object = arg1.optJSONArray("result").optJSONObject(0);
                        //TODO 需要删除的临时性代码
                        if (object == null) {
                            //空数据
                            Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Gson gson = new Gson();
                        mCoursetime = gson.fromJson(object.toString(), Coursetime.class);
                        mCoursetime.setCtWeekDay(object.optInt("CtWeekDay"));
                        mCoursetime.setCtWeekChoose(object.optInt("CtWeekChoose"));
                        mCoursetime.setCtEndClass(object.optInt("CtEndClass"));
                        mCoursetime.setCtStartClass(object.optInt("CtStartClass"));
                        mCoursetime.setCtAddress(object.optString("CtAddress"));
                        Log.e("测试", mCoursetime.getCtAddress() + "");
                        name_course.setText(mCoursetime.getCourse().getCName());
                        String week_day = mWeek[mCoursetime.getCtWeekChoose() - 1] + mDay[mCoursetime.getCtWeekDay() - 1];
                        weeknum_course.setText(week_day);
                        address_course.setText(mCoursetime.getCtAddress());
                        String daytime = "第" + mCoursetime.getCtStartClass() + "节至第" + mCoursetime.getCtEndClass() + "节";
                        daytime_course.setText(daytime);
                        mRefresh.setRefreshing(false);
                        super.onSuccess(arg0, arg1);
                    }

                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callName:
                //点名
                RequestParams params = new RequestParams();
                params.put("cid", mCourse.getCId() + "");
                client.post(HttpUtil.server_teacher_course_studentNames, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                JSONArray object = arg1.optJSONArray("result");
                                P.p(object.toString() + 1111);

                                if (object.length() == 0) {
                                    Toast.makeText(getActivity(), "您这门课没有学生选修!", Toast.LENGTH_SHORT).show();
                                } else {
                                    ArrayList<String> list = new ArrayList<String>();
                                    for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                        JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                        P.p(object_temp.toString() + 2222);
                                        list.add(i, (object_temp.optInt("scId") + " " + object_temp.optInt("SNumber") + (object_temp.optString("SName"))));
                                    }
                                    final ArrayList<Integer> select = new ArrayList<Integer>();
                                    final String[] strings = list.toArray(new String[list.size()]);
                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                            .setMultiChoiceItems(strings, null, new DialogInterface.OnMultiChoiceClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                                    select.add(which);
                                                }
                                            })
                                            .setPositiveButton("点名", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    RequestParams params = new RequestParams();
                                                    for (int i = 0; i < select.size(); i++) {
                                                        params.put(i + "", strings[i]);
                                                    }
                                                    client.post(HttpUtil.server_teacher_courseStudentCount, params,
                                                            new JsonHttpResponseHandler() {
                                                                @Override
                                                                public void onSuccess(int arg0, JSONObject arg1) {
                                                                    Toast.makeText(getActivity(),"设置成功",Toast.LENGTH_SHORT).show();
                                                                    super.onSuccess(arg0, arg1);
                                                                }
                                                            });
                                                }
                                            })
                                            .create();
                                    alertDialog.show();
                                }

                                super.onSuccess(arg0, arg1);
                            }
                        });

                break;
            case R.id.quiz:
                //提问
                RequestParams p = new RequestParams();
                p.put("cid", mCourse.getCId() + "");
                p.put("tid", mTeacher.getTId() + "");
                client.post(HttpUtil.server_teacher_course_randomAsk, p, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONArray object = arg1.optJSONArray("result");
                        if (object.length() == 0) {
                            Toast.makeText(getActivity(), "您这门课没有学生选修!", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<String> list = new ArrayList<String>();
                            JSONObject object_temp = null;
                            //返回的列表只有一个对象
                            System.out.println("=========>");
                            Intent intent = new Intent(getActivity(), TAskQuestionActivity.class);
                            for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                AskStudent askStudent = new AskStudent(object_temp.optInt("scId"), object_temp.optString("cName"), object_temp.optInt("SNumber"), object_temp.optString("SName"));
                                intent.putExtra("askStudent", askStudent);
                            }
                            /*intent.putExtra("tmn_c_info", courseInfo.substring(2, courseInfo.length()) + " 第"
                                    + courseWeek + "周" + " 上课时间" + courseTime.substring(2, courseTime.length()));*/
                            startActivity(intent);
                        }
                        super.onSuccess(arg0, arg1);
                    }
                });
                /*Intent ran = new Intent(getActivity(), TRandomAskActivity.class);//
                Bundle bundle1 = new Bundle();
                ran.putExtras(bundle1);
                getActivity().startActivity(ran);*/
                break;
            case R.id.feedBack :
                String[] items = new String[]{"显示柱状图","显示曲线图","显示饼状图","显示学生建议"} ;
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1,items) ;
                AlertDialog d = new AlertDialog.Builder(getActivity())
                        .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent() ;
                                intent.putExtra("teacher",mTeacher) ;
                                intent.putExtra("course",mCourse) ;
                                intent.setClass(getActivity(),TSecondActivity.class) ;
                                switch (which){
                                    case 0:
                                        intent.putExtra(TSecondActivity.TYPE,TSecondActivity.Histogram) ;
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        intent.putExtra(TSecondActivity.TYPE,TSecondActivity.LineChart) ;
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        intent.putExtra(TSecondActivity.TYPE,TSecondActivity.SectorChart) ;
                                        startActivity(intent);
                                        break;
                                    case 3:
                                        break;
                                }
                            }
                        }).create() ;
                d.show();
                Toast.makeText(getActivity(),"反馈",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*public void signInServiceInfo(ArrayList<String> list, JSONObject arg1) {
        list.add(0, "学号" + "_" + "姓名" + "_" + " 已到次数" + "_" + "总点到次数");
        for (int i = 1; i <= arg1.optJSONArray("result").length(); i++) {
            JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i - 1);
            P.p(object_temp.toString() + 2222);
            list.add(i, (object_temp.optInt("SNumber") + "_" + object_temp.optString("SName") + "_" + object_temp.optString("SCPointNum") + "_" + object_temp.optString("ScPointTotalNum")));
        }
    }*/
}
