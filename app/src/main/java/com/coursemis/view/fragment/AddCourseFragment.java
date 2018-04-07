package com.coursemis.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.coursemis.event.RefreshEvent;
import com.coursemis.model.Course;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.SubActivity;
import com.coursemis.view.activity.CourseAddActivity;
import com.coursemis.view.activity.NewWelcomeActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

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

public class AddCourseFragment extends BaseFragment
        implements View.OnClickListener {
    public Context context;///
    private AsyncHttpClient client;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //private Button back;
    //private TextView top_title;
    //private Button finise_add;
    private EditText name_course;
    private Spinner time_course_1, time_course_2, time_course_3;
    private ArrayAdapter<String> adapter_1;
    private ArrayAdapter<String> adapter_2;
    private static String[] time_1 = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static String[] time_2 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private EditText address_course;

    private int weekday;
    private int startclass;
    private int endclass;

    private int teacherid;
    private TitleView mTitleView;
    private Button mCommit;


    @Override
    public void refresh(Course course) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_course, null);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        EventBus.getDefault().register(new NewWelcomeActivity());
        client = new AsyncHttpClient();
        adapter_1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time_1);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time_2);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        time_course_1.setAdapter(adapter_1);
        time_course_2.setAdapter(adapter_2);
        time_course_3.setAdapter(adapter_2);

        time_course_1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        weekday = arg2 + 1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });
        time_course_2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        startclass = arg2 + 1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });
        time_course_3.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        endclass = arg2 + 1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });
        mCommit.setOnClickListener(this);
    }

    private void initView() {
        mTitleView = (TitleView) mView.findViewById(R.id.course_add_title);

        name_course = (EditText) mView.findViewById(R.id.name_course);
        time_course_1 = (Spinner) mView.findViewById(R.id.time_course_1);
        time_course_2 = (Spinner) mView.findViewById(R.id.time_course_2);
        time_course_3 = (Spinner) mView.findViewById(R.id.time_course_3);
        address_course = (EditText) mView.findViewById(R.id.address_course);
        mCommit = (Button) mView.findViewById(R.id.commit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commit:
                RequestParams params = new RequestParams();
                params.put("teacherid", mTeacher.getTId() + "");
                params.put("name_course", name_course.getText().toString().trim());
                params.put("weekday", weekday + "");
                params.put("startclass", startclass + "");
                params.put("endclass", endclass + "");
                params.put("address_course", address_course.getText().toString().trim());

                client.post(HttpUtil.server_course_add, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                // TODO Auto-generated method stub

                                /*String addCourse_msg = arg1.optString("result");
                                DialogUtil.showDialog(context, addCourse_msg, true);

                                *//**//*Intent intent = new Intent(CourseAddActivity.this, CourseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("teacherid", teacherid);
                                intent.putExtras(bundle);
                                CourseAddActivity.this.startActivity(intent);
                                CourseAddActivity.this.finish();
                                *//**//*
                                CourseAddActivity.this.finishActivity(SubActivity.SUCCESS);
                                super.onSuccess(arg0, arg1);*/
                                Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                                //刷新
                                EventBus.getDefault().post("addCourse");
                                EventBus.getDefault().unregister(this);
                                getActivity().finish();
                            }

                        });
                break;
        }
    }
}
