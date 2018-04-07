package com.coursemis.view.activity;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.SubActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CourseAddActivity extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initTitle();
        /*Intent intent = getIntent();
        teacherid = intent.getExtras().getInt("teacherid");*/

        //返回按钮
		/*back=(Button)findViewById(R.id.reback_btn);
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CourseAddActivity.this.finish();
			}
			
		});*/
		
		/*top_title = (TextView)findViewById(R.id.title);
		top_title.setText("         新增课程             ");*/
        //finise_add = (Button) findViewById(R.id.continue_btn);

        adapter_1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_1);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_2);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        time_course_1.setAdapter(adapter_1);
        time_course_2.setAdapter(adapter_2);
        time_course_3.setAdapter(adapter_2);

        time_course_1.setOnItemSelectedListener(
                new OnItemSelectedListener() {

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
                new OnItemSelectedListener() {

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
                new OnItemSelectedListener() {

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
		
		/*finise_add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("teacherid", teacherid+"");
				params.put("name_course", name_course.getText().toString().trim());
				params.put("weekday", weekday+"");
				params.put("startclass", startclass+"");
				params.put("endclass", endclass+"");
				params.put("address_course", address_course.getText().toString().trim());
				
				client.post(HttpUtil.server_course_add , params,
						new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, JSONObject arg1) {
								// TODO Auto-generated method stub
								
								String addCourse_msg = arg1.optString("result");
								DialogUtil.showDialog(context, addCourse_msg, true);
								
								Intent intent = new Intent(CourseAddActivity.this, CourseActivity.class);
								Bundle bundle = new Bundle(); 
								bundle.putInt("teacherid", teacherid);
								intent.putExtras(bundle);
								CourseAddActivity.this.startActivity(intent);
								CourseAddActivity.this.finish();
								
								super.onSuccess(arg0, arg1);
							}

						});
				
			}
			
		});
		*/
    }

    private void initTitle() {
        mTitleView.setTitle("添加课程");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                CourseAddActivity.this.finish();
            }
        });
        mTitleView.setRightButton("完成", new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                RequestParams params = new RequestParams();
                params.put("teacherid", teacherid + "");
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

                                String addCourse_msg = arg1.optString("result");
                                DialogUtil.showDialog(context, addCourse_msg, true);

                                /*Intent intent = new Intent(CourseAddActivity.this, CourseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("teacherid", teacherid);
                                intent.putExtras(bundle);
                                CourseAddActivity.this.startActivity(intent);
                                CourseAddActivity.this.finish();
                                */
                                CourseAddActivity.this.finishActivity(SubActivity.SUCCESS);
                                super.onSuccess(arg0, arg1);
                            }

                        });
            }
        });
    }

    private void initData() {
        this.context = this;
        client = new AsyncHttpClient();

        preferences = getSharedPreferences("courseMis", 0);
        editor = preferences.edit();

        teacherid = preferences.getInt("teacherid", 0);//0为默认值
    }

    private void initView() {
        setContentView(R.layout.activity_course_add);
        mTitleView = (TitleView) findViewById(R.id.course_add_title);

        name_course = (EditText) findViewById(R.id.name_course);
        time_course_1 = (Spinner) findViewById(R.id.time_course_1);
        time_course_2 = (Spinner) findViewById(R.id.time_course_2);
        time_course_3 = (Spinner) findViewById(R.id.time_course_3);
        address_course = (EditText) findViewById(R.id.address_course);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_add, menu);
        return true;
    }

}
