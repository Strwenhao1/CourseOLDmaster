package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursemis.R;
import com.coursemis.view.myView.TitleView;

import java.util.ArrayList;


public class TCourseSignInActivity extends Activity {

    private Intent intent = null;
    ArrayList<String> list = null;
    private RecyclerView lv = null;
    private TitleView mTitleView;

    public void ButtonOnclick_tcoursesignin__back(View view) {
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView() ;
        initData() ;
    }

    private void initData() {
        intent = getIntent();
        list = intent.getStringArrayListExtra("studentCourseSignInInfo");
        lv.setLayoutManager(new LinearLayoutManager(TCourseSignInActivity.this)) ;
        lv.setAdapter(new MyAdapter());
        mTitleView.setTitle("课堂签到");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                TCourseSignInActivity.this.finish();
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_tcoursesigninactivity);
        lv = (RecyclerView) findViewById(R.id.t_coursesignListview);
//        mTitleView = (TitleView) findViewById(R.id.course_signin_title);
    }

    public class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(TCourseSignInActivity.this).inflate(R.layout.item_course_signin, parent, false);
            return new MyViewHolder(inflate );
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String temp = list.get(position);

            String[]temps = temp.split("\\s+") ;
//            Log.e("temp",temp.split("\\s+").length+"" ) ;
            String number = temps[0] ;
            Log.e("number",number) ;
            String name = temps[1] ;
            Log.e("name",name) ;
            String time = temps[2] ;
            Log.e("time",time) ;
            String totaltime = temps[3] ;
            Log.e("totaltime",totaltime) ;
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.name.setText(name);

            myViewHolder.number.setText(number);
            myViewHolder.time.setText(time);
            myViewHolder.totalTime.setText(totaltime);
            myViewHolder.name.setTextColor(0xFF00FFFF);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView number ;
            public TextView name ;
            public TextView time ;
            public TextView totalTime ;
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
