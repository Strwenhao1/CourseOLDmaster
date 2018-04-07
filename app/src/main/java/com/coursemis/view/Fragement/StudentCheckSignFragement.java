package com.coursemis.view.Fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.view.activity.StudentManager;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 74000 on 2018/3/26.
 */


public class StudentCheckSignFragement extends Fragment {


    ArrayList<String> list = new ArrayList<String>();
    private RecyclerView lv = null;
    private TitleView mTitleView;
    private int sid;
    private View mRootView;
    private AsyncHttpClient client;
    MyAdapter myadapter =null;
    @Override
    public void setArguments(Bundle args) {
        sid = args.getInt("sid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_tcoursesigninactivity, container, false);
        client = new AsyncHttpClient();


        initView();
        initData();

        return mRootView;
    }

    private void initData() {


        RequestParams params = new RequestParams();
        params.put("sid", sid+"");
        client.post(HttpUtil.server_student_StudentCourse, params,
                new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONArray object = arg1.optJSONArray("result");

                        if(object.length()==0){
                            Toast.makeText(getActivity(),"您没有选修任何课程!", Toast.LENGTH_SHORT).show();
                        }else{

                            list.add(0, "课程编号"+"    "+"课程名"+"    "+" 已到次数"+"    "+"总点到次数");
                            for(int i=1;i<=arg1.optJSONArray("result").length();i++){
                                JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i-1);
                                P.p(object_temp.toString()+2222);
                                list.add(i, (object_temp.optInt("CNumber")+"    					"+object_temp.optString("CName")+"    			"+object_temp.optString("SCPointNum")+"   				 "+object_temp.optString("ScPointTotalNum")));

                            }
                        myadapter.notifyDataSetChanged();

                        }
                        super.onSuccess(arg0, arg1);
                    }
                });
        myadapter = new MyAdapter();
        lv.setLayoutManager(new LinearLayoutManager(getActivity()));
        lv.setAdapter(myadapter);

    }

    private void initView() {

        lv = (RecyclerView) mRootView.findViewById(R.id.t_coursesignListview);
//        mTitleView = (TitleView) mRootView.findViewById(R.id.course_signin_title);
    }

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.item_course_signin, parent, false);
            return new MyAdapter.MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String temp = list.get(position);

            String[] temps = temp.split("\\s+");
//            Log.e("temp",temp.split("\\s+").length+"" ) ;
            String number = temps[0];
            Log.e("number", number);
            String name = temps[1];
            Log.e("name", name);
            String time = temps[2];
            Log.e("time", time);
            String totaltime = temps[3];
            Log.e("totaltime", totaltime);
            MyAdapter.MyViewHolder myViewHolder = (MyAdapter.MyViewHolder) holder;
            myViewHolder.name.setText(name);

            myViewHolder.number.setText(number);
            myViewHolder.time.setText(time);
            myViewHolder.totalTime.setText(totaltime);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView number;
            public TextView name;
            public TextView time;
            public TextView totalTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                //itemView = View.inflate(TCourseSignInActivity.this,R.layout.item_course_signin,null) ;
                number = (TextView) itemView.findViewById(R.id.student_number);
                name = (TextView) itemView.findViewById(R.id.student_name);
                time = (TextView) itemView.findViewById(R.id.signin_time);
                totalTime = (TextView) itemView.findViewById(R.id.signin_total_time);
            }
        }
    }


}
