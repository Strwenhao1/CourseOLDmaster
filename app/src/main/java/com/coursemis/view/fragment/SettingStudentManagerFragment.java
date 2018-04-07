package com.coursemis.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Student;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.activity.StudentCourseActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class SettingStudentManagerFragment extends BaseFragment {
    //public Context context;
    private ListView listView_studentList;
    private List<String> studentNames = new ArrayList<String>();
    private List<String> studentNums = new ArrayList<String>();
    //private int courseid;
    private List<Integer> studentid_temp = new ArrayList<Integer>(); //用于传递course对应的id

    private List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();

    private AsyncHttpClient client = new AsyncHttpClient();
    private TitleView mTitleView;
    private MyAdapter mAdapter;
    @Override
    public void refresh(Course course) {
        mCourse = course ;
        initInternetData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting_student_manager,null) ;
        initView() ;
        initData() ;
        initInternetData() ;
        return mView;
    }

    private void initData() {

    }

    private void initView() {
        listView_studentList = (ListView) mView.findViewById(R.id.studentList);
    }
    private void initInternetData() {
        RequestParams params = new RequestParams();
        params.put("courseid", mCourse.getCId()+"");
        params.put("action", "student_course");///

        client.post(HttpUtil.server_student_course, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        // TODO Auto-generated method stub
                        List<Student> studentList = new ArrayList<Student>();
                        studentid_temp.clear();
                        studentNames.clear();
                        studentNums.clear();
                        listItems.clear();
                        for(int i=0;i<arg1.optJSONArray("result").length();i++){
                            JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
                            Student student = new Student();
                            student.setSId(object.optInt("SId"));
                            studentid_temp.add(student.getSId());
                            student.setSNum(object.optString("SNum"));
                            student.setSName(object.optString("SName"));
                            studentNames.add(student.getSName());
                            studentNums.add(student.getSNum());
                            Map<String,Object> listItem = new HashMap<String,Object>();
                            listItem.put("name", student.getSName());
                            listItem.put("num", student.getSNum());
                            listItems.add(listItem);

                            studentList.add(student);
                        }

                        if (mAdapter!=null){
                            mAdapter.notifyDataSetChanged();
                        }else {
                            mAdapter = new MyAdapter(getActivity());
                            listView_studentList.setAdapter(mAdapter);
                        }

                        super.onSuccess(arg0, arg1);
                    }

                });
    }
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //****************************************第二种方法，高手一般都用此种方法,具体原因，我还不清楚,有待研究
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            StudentDelListener myListener=null;

            if (convertView == null) {

                holder=new ViewHolder();

                //可以理解为从vlist获取view  之后把view返回给ListView
                myListener=new StudentDelListener(position);

                convertView = mInflater.inflate(R.layout.adapter_student, null);
                holder.name = (TextView)convertView.findViewById(R.id.adapter_student_name);
                holder.num = (TextView)convertView.findViewById(R.id.adapter_student_num);
                holder.viewBtn = (Button)convertView.findViewById(R.id.adapter_student_del);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }


            holder.name.setText((String)listItems.get(position).get("name"));
            holder.num.setText((String)listItems.get(position).get("num"));
            holder.viewBtn.setTag(position);
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
            holder.viewBtn.setOnClickListener(myListener);


            return convertView;
        }
    }

    private class StudentDelListener implements View.OnClickListener {
        int mPosition;
        public StudentDelListener(int inPosition){
            mPosition= inPosition;
        }
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            RequestParams params = new RequestParams();
            params.put("studentid", studentid_temp.get(mPosition)+"");
            params.put("courseid", mCourse.getCId()+"");

            client.post(HttpUtil.server_student_del, params,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, JSONObject arg1) {
                            // TODO Auto-generated method stub
                            String delStudent_msg = arg1.optString("result");
                            //DialogUtil.showDialog(context, delStudent_msg, true);
                            Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                            initInternetData();
                            super.onSuccess(arg0, arg1);
                        }

                    });
	          /*Toast.makeText(
	        		  CourseActivity.this, "courseid_temp:"+courseid_temp.get(mPosition), Toast.LENGTH_SHORT).show();  */
        }
    }

    //提取出来方便点
    public final class ViewHolder {
        public TextView name;
        public TextView num;
        public Button viewBtn;
    }
}
