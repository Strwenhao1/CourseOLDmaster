package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Chart;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EvaluateGetActivity extends Activity {

    public Context context;        // /
    private AsyncHttpClient client;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private Button back;
    private ListView xingList;

    private ArrayList<Map<String, String>> list;
    private SimpleAdapter adapter;

    private int[] year_zhuxing;
    private int[][] grade_zhuxing;

    private double[] years;
    private double[] points;

    private int[] personnum;

    private int teacherid;
    private int courseid;
    private TitleView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();


    }

    private void initData() {

        this.context = this;
        client = new AsyncHttpClient();

        preferences = getSharedPreferences("courseMis", 0);
        editor = preferences.edit();

        teacherid = preferences.getInt("teacherid", 0);// 0为默认值

        Intent intent = getIntent();
        // teacherid = intent.getExtras().getInt("teacherid");
        courseid = intent.getExtras().getInt("courseid");
        //初始化Title
        mTitleView.setTitle(courseid+"");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                EvaluateGetActivity.this.finish();
            }
        });
        //初始化列表
        initList();

    }

    private void initList() {
        list = new ArrayList<Map<String, String>>();

        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "柱状图 ");
        map1.put("desc", "显示柱状图 ");
        list.add(map1);

        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("name", "曲线图 ");
        map2.put("desc", "显示曲线图 ");
        list.add(map2);

        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("name", "饼状图 ");
        map3.put("desc", "显示饼状图 ");
        list.add(map3);

        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("name", "学生建议");
        map4.put("desc", "显示学生建议");
        list.add(map4);

        // 构建 listView 的适配器
        adapter = new SimpleAdapter(this, list,
                //android.R.layout.simple_list_item_2, // SDK 库中提供的一个包含两个 TextView
                // 的 layout
                R.layout.text_list_item_2,
                new String[]{"name", "desc"}, // maps 中的两个 key
                //new int[]{android.R.id.text1, android.R.id.text2} // 两个
                // TextView
                // 的 id
                new int[] {R.id.text1,R.id.text2}
        );
        xingList.setAdapter(adapter);
        xingList.setOnItemClickListener(new MyOnItemClickListener());
    }

    private void initView() {
        setContentView(R.layout.activity_evaluate_get);
        xingList = (ListView) findViewById(R.id.xingList);
        mTitleView = (TitleView) findViewById(R.id.evaluate_title);
    }

    private class MyOnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> l, View v, int position,
                                long id) {
            // TODO Auto-generated method stub
            // 判断是柱状图，跳转到相应图形
            if (id == 0) {
                RequestParams params = new RequestParams();
                params.put("courseid", courseid + "");
                params.put("action", "course_teacher");// /
                client.post(HttpUtil.server_evaluate_zhu_get, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                // TODO Auto-generated method stub
                                year_zhuxing = new int[arg1.optJSONArray(
                                        "result").length()];
                                grade_zhuxing = new int[arg1.optJSONArray(
                                        "result").length()][3];
                                for (int i = 0; i < arg1.optJSONArray(
                                        "result").length(); i++) {
                                    JSONObject object = arg1.optJSONArray(
                                            "result").optJSONObject(i);

                                    year_zhuxing[i] = object.optInt("year");
                                    Log.e("year\t"+i,object.optInt("year")+"") ;
                                    grade_zhuxing[i][0] = object
                                            .optInt("grade1");
                                    Log.e("grade1\t"+i,object.optInt("grade1")+"") ;
                                    grade_zhuxing[i][1] = object
                                            .optInt("grade2");
                                    Log.e("grade2\t"+i,object.optInt("grade2")+"") ;
                                    grade_zhuxing[i][2] = object
                                            .optInt("grade3");
                                    Log.e("grade3\t"+i,object.optInt("grade3")+"") ;
                                }
                                Log.e("year", year_zhuxing.length + "");
                                Intent intent = new Intent(EvaluateGetActivity.this, BarChartActivity.class);
                                Chart chart = new Chart() ;
                                chart.setGrade(grade_zhuxing);
                                chart.setYear(year_zhuxing);
                                intent.putExtra("chart", chart);
                                startActivity(intent);
                                super.onSuccess(arg0, arg1);
                            }

                        });

            } else if (id == 1) {
                RequestParams params = new RequestParams();
                params.put("courseid", courseid + "");
                params.put("action", "course_teacher");// /
                client.post(HttpUtil.server_evaluate_get, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                // TODO Auto-generated method stub
                                Toast.makeText(context, "onsuccess",
                                        Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, arg1.toString(),
                                        Toast.LENGTH_SHORT).show();

                                years = new double[arg1.optJSONArray(
                                        "result").length()];
                                points = new double[arg1.optJSONArray(
                                        "result").length()];
                                for (int i = 0; i < arg1.optJSONArray(
                                        "result").length(); i++) {
                                    JSONObject object = arg1.optJSONArray(
                                            "result").optJSONObject(i);

                                    years[i] = object.optDouble("year");
                                    points[i] = object.optDouble("point");
                                }
                                /*Intent intent = new LineChartActivity().execute(
                                        context, years, points);
                                startActivity(intent);*/
                                Intent intent = new Intent(EvaluateGetActivity.this,LineChartActivity.class) ;
                                Log.e("测试",(years==null)+"....."+(points==null)) ;
                                Chart chart = new Chart() ;
                                chart.setYears(years);
                                chart.setPoint(points);
                                intent.putExtra("chart",chart) ;
                                startActivity(intent);
                                super.onSuccess(arg0, arg1);
                            }

                        });

            } else if (id == 2) {
                RequestParams params = new RequestParams();
                params.put("courseid", courseid + "");
                params.put("action", "course_teacher");// /
                client.post(HttpUtil.server_evaluate_Sms_get, params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                // TODO Auto-generated method stub

                                personnum = new int[arg1.optJSONArray(
                                        "result").length()];
                                for (int i = 0; i < arg1.optJSONArray(
                                        "result").length(); i++) {
                                    JSONObject object = arg1.optJSONArray(
                                            "result").optJSONObject(i);

                                    personnum[i] = object
                                            .optInt("personnum");
                                }
                                /*Intent achartIntent = new PieChartActivity()
                                        .execute(context, personnum);
                                startActivity(achartIntent);*/
                                Intent intent = new Intent(EvaluateGetActivity.this,PieChartActivity.class) ;
                                Chart chart = new Chart() ;
                                chart.setPersonnum(personnum);
                                intent.putExtra("chart",chart) ;
                                startActivity(intent);
                                super.onSuccess(arg0, arg1);
                            }

                        });

            } else if (id == 3) {
                Intent intent = new Intent(EvaluateGetActivity.this, EvaluateSuggestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("teacherid", teacherid);
                bundle.putInt("courseid", courseid);
                intent.putExtras(bundle);
                startActivity(intent);
                EvaluateGetActivity.this.finish();
            }
        }
    }
    /*
     * // ListItem 监听器方法 public void onListItemClick(ListView l, View v, int
	 * position, long id) { super.onListItemClick(l, v, position, id);
	 * //判断是柱状图，跳转到相应图形 if(id==0) { RequestParams params = new RequestParams();
	 * params.put("courseid", courseid+""); params.put("action",
	 * "course_teacher");/// client.post(HttpUtil.server_evaluate_zhu_get,
	 * params, new JsonHttpResponseHandler() {
	 * 
	 * @Override public void onSuccess(int arg0, JSONObject arg1) { // TODO
	 * Auto-generated method stub year_zhuxing = new
	 * int[arg1.optJSONArray("result").length()]; grade_zhuxing = new
	 * int[arg1.optJSONArray("result").length()][3]; for(int
	 * i=0;i<arg1.optJSONArray("result").length();i++){ JSONObject object =
	 * arg1.optJSONArray("result").optJSONObject(i);
	 * 
	 * year_zhuxing[i] = object.optInt("year"); grade_zhuxing[i][0] =
	 * object.optInt("grade1"); grade_zhuxing[i][1] = object.optInt("grade2");
	 * grade_zhuxing[i][2] = object.optInt("grade3"); } Intent intent=new
	 * BarChartActivity().execute(context, grade_zhuxing, year_zhuxing);
	 * startActivity(intent); super.onSuccess(arg0, arg1); }
	 * 
	 * });
	 * 
	 * } else if(id==1) { RequestParams params = new RequestParams();
	 * params.put("courseid", courseid+""); params.put("action",
	 * "course_teacher");/// client.post(HttpUtil.server_evaluate_get, params,
	 * new JsonHttpResponseHandler() {
	 * 
	 * @Override public void onSuccess(int arg0, JSONObject arg1) { // TODO
	 * Auto-generated method stub Toast.makeText(context,"onsuccess",
	 * Toast.LENGTH_SHORT).show(); Toast.makeText(context,arg1.toString(),
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * years = new double[arg1.optJSONArray("result").length()]; points = new
	 * double[arg1.optJSONArray("result").length()]; for(int
	 * i=0;i<arg1.optJSONArray("result").length();i++){ JSONObject object =
	 * arg1.optJSONArray("result").optJSONObject(i);
	 * 
	 * years[i] = object.optDouble("year"); points[i] =
	 * object.optDouble("point"); } Intent intent = new
	 * LineChartActivity().execute(context,years,points); startActivity(intent);
	 * super.onSuccess(arg0, arg1); }
	 * 
	 * });
	 * 
	 * } else if(id==2){ RequestParams params = new RequestParams();
	 * params.put("courseid", courseid+""); params.put("action",
	 * "course_teacher");/// client.post(HttpUtil.server_evaluate_Sms_get,
	 * params, new JsonHttpResponseHandler() {
	 * 
	 * @Override public void onSuccess(int arg0, JSONObject arg1) { // TODO
	 * Auto-generated method stub
	 * 
	 * personnum = new int[arg1.optJSONArray("result").length()]; for(int
	 * i=0;i<arg1.optJSONArray("result").length();i++){ JSONObject object =
	 * arg1.optJSONArray("result").optJSONObject(i);
	 * 
	 * personnum[i] = object.optInt("personnum"); } Intent achartIntent = new
	 * PieChartActivity().execute(context,personnum); startActivity(achartIntent);
	 * super.onSuccess(arg0, arg1); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * 
	 * else if(id==3){}
	 * 
	 * 
	 * }
	 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evaluate_get, menu);
        return true;
    }

}
