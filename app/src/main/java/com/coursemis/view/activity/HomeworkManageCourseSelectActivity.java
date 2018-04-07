package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.coursemis.R;
import com.coursemis.adapter.HomeworkSelectAdapter;
import com.coursemis.util.P;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

public class HomeworkManageCourseSelectActivity extends Activity {
    //private ListView hms = null;
    private ArrayList<String> courseInfo = null;
    Intent intent = null;
    Intent intent_temp = null;
    LinearLayout layout = null;
    //Button back=null;
    int tid;
    private AsyncHttpClient client = new AsyncHttpClient();
    private TitleView mTitleView;
    private RecyclerView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
        initData();


        /*back=(Button)findViewById(R.id.homeworkmanagecourseselect_back);
        back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}});*/
//		for(int i=0;i<courseInfo.size();i++)
//		{
//			final Button btn = new Button(this);
//			btn.setText(courseInfo.get(i));
//			P.p(courseInfo.get(i));
//			btn.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//				}
//				
//			});
//			
//			
//			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//			ll.addView(btn,lp1);
//		}

        initTitle();

    }

    private void initIntent() {
        intent = getIntent();
        courseInfo = intent.getStringArrayListExtra("courselist");
    }


    private void initData() {
        mListView.setLayoutManager(new LinearLayoutManager(HomeworkManageCourseSelectActivity.this));
        mListView.setAdapter(new HomeworkSelectAdapter(HomeworkManageCourseSelectActivity.this,
                courseInfo));
        /*ArrayList<String> courseInfo_temp = new ArrayList<String>();
        for (int i = 0; i < courseInfo.size(); i++) {
            String temp = courseInfo.get(i);
            courseInfo_temp.add("课程名：		" + temp.substring(temp.indexOf(" ") + 1, temp.length()));
        }

        ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_checked, courseInfo_temp);
        *//*hms.setAdapter(aaRadioButtonAdapter);
        hms.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        hms.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String uriString = "" + courseInfo.get(arg2);
                RequestParams params = new RequestParams();
                SharedPreferences sharedata = getSharedPreferences("courseMis", 0);
                tid = Integer.parseInt(sharedata.getString("userID", null));
                params.put("tid", tid + "");
                params.put("courseinfo", uriString);
                Log.e("点击","点击") ;
                client.post(HttpUtil.server_teacher_homework_select, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                JSONArray object = arg1.optJSONArray("result");
                                P.p(object.toString() + 1111);

                                if (object.length() == 0) {
                                    Toast.makeText(HomeworkManageCourseSelectActivity.this, "这门课程您还没有布置过作业", Toast.LENGTH_SHORT).show();
                                    ArrayList<String> list = new ArrayList<String>();
                                    Intent intent = new Intent(HomeworkManageCourseSelectActivity.this, HomeworkManageCourseSelectInfoActivity.class);
                                    intent.putStringArrayListExtra("sourcemanagelist", list);
                                    intent.putExtra("title", courseInfo.get(arg2));
                                    startActivity(intent);

                                } else {
                                    ArrayList<String> list = new ArrayList<String>();
                                    for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                        JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                        P.p(object_temp.toString() + 2222);
                                        list.add(i, (object_temp.optInt("smid") + " " + (object_temp.optString("smname") + " " + object_temp.optString("smpath"))));
                                    }

                                    Intent intent = new Intent(HomeworkManageCourseSelectActivity.this, HomeworkManageCourseSelectInfoActivity.class);
                                    intent.putStringArrayListExtra("sourcemanagelist", list);
                                    intent.putExtra("title", courseInfo.get(arg2));
                                    startActivity(intent);
                                }

                                super.onSuccess(arg0, arg1);
                            }
                        });
            }
        });*/

        P.p(courseInfo.size() + "" + "111111111111111");
    }

    private void initView() {
        setContentView(R.layout.activity_homeworkmanagecourseselect);
        LinearLayout ll = (LinearLayout) findViewById(R.id.hwmcs);
        mTitleView = (TitleView) findViewById(R.id.homework_title);
        //hms = (ListView) findViewById(R.id.homeworkmanagecourseListview);
        mListView = (RecyclerView) findViewById(R.id.homeworkmanage_list);
    }

    private void initTitle() {
        mTitleView.setTitle("作业管理");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                HomeworkManageCourseSelectActivity.this.finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homework_manage_course_select, menu);
        return true;
    }

}
