package com.coursemis.view.myView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coursemis.R;

import java.util.ArrayList;
import java.util.List;

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
 * 签到列表的view
 * Created by zhxchao on 2018/3/14.
 */

public class SignInListView extends LinearLayout {
    private Context mContext;
    private AttributeSet mAttrs;
    private int mDefStyleAttr;
    private LayoutInflater mInflate;
    ArrayList<String> mList = null;
    private RecyclerView mListView;
    public Button mQuit;

    public SignInListView(Context context) {
        this(context, null);
    }

    public SignInListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs, defStyleAttr);
        initView();
        Log.e("Log", "initData(context, attrs, defStyleAttr);");
    }


    private void initView() {
        mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflate.inflate(R.layout.view_signin_list, this);
        mListView = (RecyclerView) findViewById(R.id.t_coursesignListview);
        mQuit = (Button) findViewById(R.id.quit);
    }

    private void initData(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        mAttrs = attrs;
        mDefStyleAttr = defStyleAttr;
    }

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_course_signin, parent, false);
            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.e("Log","onBindViewHolder") ;
            String temp = mList.get(position);
            String[] temps = temp.split("_");
            Log.e("temp", temp.split("_").length + "");
            String number = temps[0];
            Log.e("number", number);
            String name = temps[1];
            Log.e("name", name);
            String time = temps[2];
            Log.e("time", time);
            String totaltime = temps[3];
            Log.e("totaltime", totaltime);
            MyViewHolder myViewHolder = (MyAdapter.MyViewHolder) holder;
            myViewHolder.name.setText(name);
            myViewHolder.number.setText(number);
            myViewHolder.time.setText(time);
            myViewHolder.totalTime.setText(totaltime);
        }

        @Override
        public int getItemCount() {
            return mList.size();
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

    public SignInListView setList(List<String> list) {
        Log.e("Log", "setList");
        mList = (ArrayList<String>) list;
        Log.e("Log", (mListView == null) + "...." + (mContext == null)+"...."+mList.size());
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setAdapter(new MyAdapter());
        return this;
    }
}
