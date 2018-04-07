package com.coursemis.view.Fragement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.view.activity.HomeworkCourseCSubmitInfoActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74000 on 2018/3/26.
 */

public class HomeworkManagerFragement extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ListView sch=null;

    Button back = null;
    private AsyncHttpClient client = new AsyncHttpClient();

    int sid;
    List<String> list;
    @Override
    public void setArguments(Bundle args) {
       list = args.getStringArrayList("studentCourseInfo1") ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_student_check_homework,container,false);
        SharedPreferences sharedata=getActivity().getSharedPreferences("courseMis", 0);
        sid = Integer.parseInt(sharedata.getString("userID",null));
        sch=(ListView)mRootView.findViewById(R.id.studentcheckhomework_listview);
        final List<String> courseinfol= list;
        ArrayList<String> courseinfol_temp=new ArrayList<String>();
        for(int i=0;i<courseinfol.size();i++)
        {
            String temp = courseinfol.get(i);
            String cname = temp.substring(temp.indexOf(" ")+1,temp.indexOf("_"));
            String tname = temp.substring(temp.indexOf("_")+1,temp.length());
            courseinfol_temp.add(cname+"       "+tname+" 老师");
        }
        ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, courseinfol_temp);
        sch.setAdapter(aaRadioButtonAdapter);
        sch.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sch.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                RequestParams params = new RequestParams();
                params.put("courseinfo", courseinfol.get(arg2)+"");
                params.put("sid", sid+"");
                Toast.makeText(getActivity(),"here",Toast.LENGTH_SHORT).show();

                client.post(HttpUtil.server_student_StudentCourseCheckhomework, params,
                        new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {

                                Log.e("here", "onItemClick: ",null );
                                JSONArray object = arg1.optJSONArray("result");

                                if(object.length()==0){
                                    Toast.makeText(getActivity(),"这门课程没有任何作业", Toast.LENGTH_SHORT).show();
                                }else{
                                    ArrayList<String> list=new ArrayList<String>();

                                    for(int i=0;i<arg1.optJSONArray("result").length();i++){
                                        JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                        P.p(object_temp.toString()+2222);
                                        list.add(i, (object_temp.optInt("smid")+" "+object_temp.optString("smname")+"_"+object_temp.optString("flag")));
                                    }

                                    Intent intent = new Intent(getActivity(),HomeworkCourseCSubmitInfoActivity.class);
                                    intent.putExtra("courseinfo", courseinfol.get(arg2)+"");
                                    intent.putStringArrayListExtra("studentCourseHomeworkInfo", list);
                                    P.p("!@#$%^&*@#$%^&*#$%^&");
                                    startActivity(intent);
                                }


                                super.onSuccess(arg0, arg1);
                            }
                        });

            }

        });




        for(int i=0;i<courseinfol.size();i++)
        {
            final Button btn = new Button(getActivity());
            btn.setText(courseinfol.get(i));
            P.p(courseinfol.get(i));
            btn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                }

            });

        }



        return mRootView;
    }


    @Override
    public void onClick(View view) {

    }
}
