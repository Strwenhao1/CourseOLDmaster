package com.coursemis.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Student;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.activity.TSecondActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Created by zhxchao on 2018/3/24.
 */

public class AddStudentFragment extends BaseFragment {

    public Context context;
    private AsyncHttpClient client;

    EditText selected;
    ExpandableListView expandListView;
    String[] armTypes;
    String[][] arms;
    int[][] studentid_arms;
    private String[] studentNames1;
    private String[] studentNames2;
    private String[] studentNames3;
    private int[] studentId1;
    private int[] studentId2;
    private int[] studentId3;
    private List<String> studentNames_1 = new ArrayList<String>();
    private List<String> studentNames_2 = new ArrayList<String>();
    private List<String> studentNames_3 = new ArrayList<String>();

    private List<Integer> para = new ArrayList<Integer>();
    private TitleView mTitleView;
    private Button mCommit;

    @Override
    public void refresh(Course course) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_student,null) ;
        initView() ;
        initData();
        return mView;
    }
    private void initData() {
        client = new AsyncHttpClient();
        //courseid = intent.getExtras().getInt("courseid");
        selected.setText("");

        RequestParams params = new RequestParams();
        params.put("courseid", mCourse.getCId() + "");
        params.put("action", "get_all_student");///

        client.post(HttpUtil.server_get_student, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        // TODO Auto-generated method stub
                        studentId1 = new int[arg1.optJSONArray("result1").length()];
                        studentId2 = new int[arg1.optJSONArray("result2").length()];
                        studentId3 = new int[arg1.optJSONArray("result3").length()];
                        Log.e("测试","成功") ;
                        List<Student> studentList1 = new ArrayList<Student>();
                        for (int i = 0; i < arg1.optJSONArray("result1").length(); i++) {
                            JSONObject object = arg1.optJSONArray("result1").optJSONObject(i);

                            Student student = new Student();
                            student.setSId(object.optInt("SId"));
                            studentId1[i] = student.getSId();
                            student.setSNum(object.optString("SNum"));
                            student.setSName(object.optString("SName"));
                            studentNames_1.add(student.getSName());
                            studentList1.add(student);
                        }
                        List<Student> studentList2 = new ArrayList<Student>();
                        for (int i = 0; i < arg1.optJSONArray("result2").length(); i++) {
                            JSONObject object = arg1.optJSONArray("result2").optJSONObject(i);

                            Student student = new Student();
                            student.setSId(object.optInt("SId"));
                            studentId2[i] = student.getSId();
                            student.setSNum(object.optString("SNum"));
                            student.setSName(object.optString("SName"));
                            studentNames_2.add(student.getSName());
                            studentList2.add(student);
                        }
                        List<Student> studentList3 = new ArrayList<Student>();
                        for (int i = 0; i < arg1.optJSONArray("result3").length(); i++) {
                            JSONObject object = arg1.optJSONArray("result3").optJSONObject(i);

                            Student student = new Student();
                            student.setSId(object.optInt("SId"));
                            studentId3[i] = student.getSId();
                            student.setSNum(object.optString("SNum"));
                            student.setSName(object.optString("SName"));
                            studentNames_3.add(student.getSName());
                            studentList3.add(student);
                        }
                        //创建一个BaseExpandableListAdapter对象
                        armTypes = new String[]{"软工0901", "软工0902", "软工0903"};

                        studentNames1 = new String[studentNames_1.size()];
                        studentNames2 = new String[studentNames_2.size()];
                        studentNames3 = new String[studentNames_3.size()];
                        for (int i = 0; i < studentNames_1.size(); i++) {
                            studentNames1[i] = studentNames_1.get(i);
                        }
                        for (int i = 0; i < studentNames_2.size(); i++) {
                            studentNames2[i] = studentNames_2.get(i);
                        }
                        for (int i = 0; i < studentNames_3.size(); i++) {
                            studentNames3[i] = studentNames_3.get(i);
                        }

                        arms = new String[][]{
                                studentNames1,
                                studentNames2,
                                studentNames3
                        };
                        studentid_arms = new int[][]{
                                studentId1,
                                studentId2,
                                studentId3
                        };
                        ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
                            int[] logos = new int[]{R.drawable.classicon, R.drawable.classicon, R.drawable.classicon};

                            //获取指定组位置、指定子列表项处的子列表项数据
                            @Override
                            public Object getChild(int groupPosition, int childPosition) {
                                return arms[groupPosition][childPosition];
                            }

                            @Override
                            public long getChildId(int groupPosition, int childPosition) {
                                return childPosition;
                            }

                            @Override
                            public int getChildrenCount(int groupPosition) {
                                return arms[groupPosition].length;
                            }

                            private TextView getTextView() {
                                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT, 64);
                                TextView textView = new TextView(getActivity());
                                textView.setLayoutParams(lp);
                                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                                textView.setPadding(36, 0, 0, 0);
                                textView.setTextSize(20);
                                return textView;
                            }

                            //该方法决定每个子选项的外观
                            @Override
                            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                                TextView textView = getTextView();
                                textView.setText(getChild(groupPosition, childPosition).toString());

                                return textView;
                            }

                            //获取指定组位置处的组数据
                            @Override
                            public Object getGroup(int groupPosition) {
                                return armTypes[groupPosition];
                            }

                            @Override
                            public int getGroupCount() {
                                return armTypes.length;
                            }

                            @Override
                            public long getGroupId(int groupPosition) {
                                return groupPosition;
                            }

                            //该方法决定每个组选项的外观
                            @Override
                            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                                LinearLayout ll = new LinearLayout(getActivity());
                                ll.setOrientation(LinearLayout.HORIZONTAL);
                                ImageView logo = new ImageView(getActivity());
                                logo.setImageResource(logos[groupPosition]);
                                ll.addView(logo);
                                TextView textView = getTextView();
                                textView.setText(getGroup(groupPosition).toString());
                                ll.addView(textView);
                                return ll;
                            }

                            @Override
                            public boolean isChildSelectable(int groupPosition, int childPosition) {
                                return true;
                            }

                            @Override
                            public boolean hasStableIds() {
                                return true;
                            }


                        };
                        expandListView = (ExpandableListView) mView.findViewById(R.id.list);
                        expandListView.setOnChildClickListener(
                                new ExpandableListView.OnChildClickListener() {

                                    @Override
                                    public boolean onChildClick(ExpandableListView parent,
                                                                View v, int groupPosition, int childPosition,
                                                                long id) {
                                        // TODO Auto-generated method stub

                                        selected.setText(selected.getText() + arms[groupPosition][childPosition] + ";");
                                        para.add(studentid_arms[groupPosition][childPosition]);
                                        return true;
                                    }

                                });

                        expandListView.setAdapter(adapter);

                        super.onSuccess(arg0, arg1);
                    }

                });
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断数据
                if (selected.getText()==null||selected.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请选择一个学生",Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestParams params = new RequestParams();

                for (int i = 0; i < para.size(); i++) {
                    params.put("studentid" + i, para.get(i) + "");
                }
                params.put("studentsize", para.size() + "");
                params.put("courseid", mCourse.getCId() + "");
                params.put("action", "get_all_student");///

                client.post(HttpUtil.server_add_student, params,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {
                                Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                                getActivity().finishActivity(TSecondActivity.ADDSUCCESS);
                                getActivity().finish();
                                super.onSuccess(arg0, arg1);
                            }

                        });
            }
        });
    }

    private void initView() {
        selected = (EditText) mView.findViewById(R.id.selected);
        mCommit = (Button) mView.findViewById(R.id.commit);
    }


}
