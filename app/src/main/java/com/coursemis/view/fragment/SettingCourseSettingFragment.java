package com.coursemis.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.coursemis.view.activity.CourseEditActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Arrays;

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
 * Created by zhxchao on 2018/3/22.
 */

public class SettingCourseSettingFragment extends BaseFragment
        implements View.OnClickListener {

    //public Context context;///
    private AsyncHttpClient client = new AsyncHttpClient();

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
    private Button mCommit;

    @Override
    public void refresh(Course course) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting_course_setting, null);
        initView();
        initData();
        initAdapterAndListener();
        return mView;
    }

    private void initData() {
        RequestParams params = new RequestParams();
        params.put("courseid", mCourse.getCId() + "");
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
                        //super.onSuccess(arg0, arg1);
                    }

                });
        mCommit.setOnClickListener(this);
    }

    private void initView() {
        name_course = (EditText) mView.findViewById(R.id.name_course_edit);
        time_course_button = (Button) mView.findViewById(R.id.time_course_button);
        time_course_editText = (EditText) mView.findViewById(R.id.time_course_editText);
        time_course_2 = (Spinner) mView.findViewById(R.id.time_course_2);
        time_course_3 = (Spinner) mView.findViewById(R.id.time_course_3);
        address_course = (EditText) mView.findViewById(R.id.address_course);
        mCommit = (Button) mView.findViewById(R.id.commit);
        time_course_3.setEnabled(false);
    }

    private void initAdapterAndListener() {
        mStartAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mTimeStart);
        mStartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mEndAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mTimeEnd);
        mEndAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_course_2.setAdapter(mStartAdapter);
        time_course_3.setAdapter(mEndAdapter);
        time_course_button.setOnClickListener(this);
        time_course_2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        startclass = arg2 + 1;
                        int selectedItemPosition = time_course_2.getSelectedItemPosition();
                        Log.e("选择", selectedItemPosition + "");
                        System.arraycopy(mTimeStart, selectedItemPosition, mTimeEnd, 0, mTimeStart.length - selectedItemPosition);
                        mTimeEnd = Arrays.copyOf(mTimeStart, mTimeStart.length - selectedItemPosition);
                        Log.e("截取", mTimeEnd.length + "");
                        mEndAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });
        time_course_3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

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

    private void commit() {
        client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("courseid", mCourse.getCId() + "");
        params.put("name_course", name_course.getText().toString().trim());
        params.put("weekday", weekday + "");
        params.put("startclass", startclass + "");
        params.put("endclass", endclass + "");
        params.put("address_course", address_course.getText().toString().trim());

        client.post(HttpUtil.server_course_edit, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        Toast.makeText(getActivity(), "测试", Toast.LENGTH_SHORT).show();
                        // TODO Auto-generated method stub
                        getActivity().finish();
                        //super.onSuccess(arg0, arg1);
                    }

                    @Override
                    public void onFailure(String responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                commit();
                break;
            case R.id.time_course_button:
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("选择星期数")
                        .setItems(mWeeks, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                time_course_editText.setText(mWeeks[which]);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                break;
        }
    }
}
