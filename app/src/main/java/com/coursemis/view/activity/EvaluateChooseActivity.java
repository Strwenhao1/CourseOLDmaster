package com.coursemis.view.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.adapter.EvaluateChooseAdapter;
import com.coursemis.model.Course;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Sivan
 * @2017-5-4下午7:56:22
 * @描述 教师选择要查看教学反馈的课程
 */
public class EvaluateChooseActivity extends Activity {

    //private Button			bt_back;
    private ListView lv_course_teacher;
    private AsyncHttpClient client;
    private int teacherid;
    //private EvaluateChooseAdapter evaluateChooseAdapter;
    private List<Course> courseList = new ArrayList<Course>();
    private TitleView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_evaluate_choose);
        mTitleView = (TitleView) findViewById(R.id.evaluate_choose_title);
        lv_course_teacher = (ListView) findViewById(R.id.lv_course_teacher);
    }

    private void initData() {

        client = new AsyncHttpClient();
        Intent intent = getIntent();
        teacherid = intent.getExtras().getInt("teacherid");

        /*if (evaluateChooseAdapter == null) {
            evaluateChooseAdapter = new EvaluateChooseAdapter(this, courseList, teacherid);
        }*/

        //lv_course_teacher.setAdapter(evaluateChooseAdapter);
        RequestParams params = new RequestParams();
        params.put("tid", teacherid + "");
        client.post(HttpUtil.server_teacher_course, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        super.onSuccess(arg0, arg1);
                        for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                            Course course = new Course();
                            JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
                            course.setCId(object.optInt("CId"));
                            course.setCName(object.optString("CName"));
                            courseList.add(course);
                        }
                        String [] arr = new String[courseList.size()] ;
                        Log.e("测试",""+courseList.size()) ;
                        for (int i = 0 ; i<courseList.size();i++){
                            arr[i] = courseList.get(i).getCName() ;
                            Log.e("测试",arr[i]) ;
                        }
                        final ArrayAdapter<String> arrayAdapter =
                                new ArrayAdapter<String>(EvaluateChooseActivity.this,
                                        R.layout.text_list_item_1,arr) ;
                        lv_course_teacher.setAdapter(arrayAdapter);
                        lv_course_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(EvaluateChooseActivity.this,EvaluateGetActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("teacherid", teacherid);
                                bundle.putInt("courseid", courseList.get(position).getCId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        //arrayAdapter.notifyDataSetChanged();
                        //evaluateChooseAdapter.notifyDataSetChanged();
                    }
                });

        initTitle();
    }

    private void initTitle() {
        mTitleView.setTitle("查看课程反馈");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                EvaluateChooseActivity.this.finish();
            }
        });
    }
}
