package com.coursemis.view.Fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Coursetime;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74000 on 2018/3/25.
 */

public class CourseValuate_s extends Fragment implements View.OnClickListener {


    public Context context;									// /
    private AsyncHttpClient client;

    // SharedPreferences preferences;
    // SharedPreferences.Editor editor;

    private Button back;
    private Button					button_continue;
    private ListView listview_courseOfStu;
//    FragmentManager manager = getFragmentManager();
    private int						studentid;

    private int						courseid_selected;
    private List<String> courseNames		= new ArrayList<String>();
    private List<Integer>			courseid_temp	= new ArrayList<Integer>(); // 用于传递course对应的id
    private ArrayAdapter<String> arrayAdapter;
    private View mRootView;

    @Override
    public void setArguments(Bundle args) {
        studentid = args.getInt("studentid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_evaluate,container,false);


        this.context = getActivity();
        client = new AsyncHttpClient();

        // preferences = getSharedPreferences("courseMis", 0);
        // editor = preferences.edit();
        //
        // studentid = preferences.getInt("studentid", 0);//0为默认值


        back = (Button) mRootView.findViewById(R.id.reback_btn);

        button_continue = (Button) mRootView.findViewById(R.id.continue_btn);

        listview_courseOfStu = (ListView)mRootView. findViewById(R.id.courseOfStudentList);

        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_single_choice, courseNames);

        listview_courseOfStu.setAdapter(arrayAdapter);
        listview_courseOfStu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview_courseOfStu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                listview_courseOfStu.setItemChecked(arg2, true);
                courseid_selected = courseid_temp.get(arg2);
            }
        });

        RequestParams params = new RequestParams();
        params.put("studentid", studentid + "");
        client.post(HttpUtil.server_course_student, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {

                        List<Course> courseList = new ArrayList<Course>();
                        List<Coursetime> coursetimeList = new ArrayList<Coursetime>();

                        for (int i = 0; i < arg1.optJSONArray("result")
                                .length(); i++) {
                            JSONObject object = arg1.optJSONArray("result")
                                    .optJSONObject(i);

                            Course course = new Course();
                            course.setCId(object.optInt("CId"));
                            course.setCName(object.optString("CName"));
                            courseNames.add(course.getCName());
                            courseid_temp.add(course.getCId());

                            Coursetime coursetime = new Coursetime();
                            coursetime.setCtWeekChoose(object
                                    .optInt("ctWeekChoose"));
                            coursetime.setCtWeekDay(object.optInt("ctWeekDay"));
                            coursetime.setCtStartClass(object
                                    .optInt("ctStartClass"));
                            coursetime.setCtEndClass(object
                                    .optInt("ctEndClass"));
                            coursetime.setCtAddress(object
                                    .optString("ctAddress"));

                            courseList.add(course);
                            coursetimeList.add(coursetime);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }

                });

//        // 返回按钮
//        back.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                EvaluateActivity.this.finish();
//            }
//        });
        button_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                Intent i = new Intent(EvaluateActivity.this,
//                        EvaluateAddActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("studentid", studentid);
//                bundle.putInt("courseid", courseid_selected);
//                Toast.makeText(context, "courseid:" + courseid_selected,
//                        Toast.LENGTH_SHORT).show();
//                i.putExtras(bundle);
//                EvaluateActivity.this.startActivity(i);


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                EvaluateFragement fragment = new EvaluateFragement();
                Bundle bundle = new Bundle();
                bundle.putInt("studentid", studentid);// ("fragData",fragData);
                bundle.putInt("courseid", courseid_selected);
                fragment.setArguments(bundle);
                transaction.replace(R.id.v4_drawerlayout_frame,fragment);
                transaction.commit();

            }
        });


        return mRootView;
    }


    @Override
    public void onClick(View view) {

    }
}
