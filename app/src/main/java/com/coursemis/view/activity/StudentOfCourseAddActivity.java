package com.coursemis.view.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.model.Student;
import com.coursemis.adapter.CheckboxAdapter;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StudentOfCourseAddActivity extends Activity {
    public Context context;
    private AsyncHttpClient client;

    private int courseid;
    EditText selected;
    ExpandableListView expandListView;
    //private Button back;
    //private TextView top_title;
    //private Button finise_add;

    String[] armTypes;
    String[][] arms;
    int[][] studentid_arms;
    //适配器
    CheckboxAdapter listItemAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView() ;
        initData() ;
        initTitle() ;

        //返回按钮
        /*back = (Button) findViewById(R.id.reback_btn);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StudentOfCourseAddActivity.this.finish();
            }
        });

        top_title = (TextView) findViewById(R.id.title);
        top_title.setText("         添加学生             ");
        finise_add = (Button) findViewById(R.id.continue_btn);

        finise_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                RequestParams params = new RequestParams();

                for (int i = 0; i < para.size(); i++) {
                    params.put("studentid" + i, para.get(i) + "");
                }
                params.put("studentsize", para.size() + "");
                params.put("courseid", courseid + "");
                params.put("action", "get_all_student");///

                client.post(HttpUtil.server_add_student, params,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {

                                String addStudentToCourse_msg = arg1.optString("result");
                                DialogUtil.showDialog(context, addStudentToCourse_msg, true);

                                Intent intent = new Intent(StudentOfCourseAddActivity.this, StudentCourseActivity.class);
                                Bundle bundle = new Bundle();
                                //	bundle.putSerializable("courseList", (Serializable) courseList);
                                bundle.putInt("courseid", courseid);
                                intent.putExtras(bundle);
                                StudentOfCourseAddActivity.this.startActivity(intent);
                                StudentOfCourseAddActivity.this.finish();

                                super.onSuccess(arg0, arg1);
                            }

                        });

            }
        });*/

        selected.setText("");

        RequestParams params = new RequestParams();
        params.put("courseid", courseid + "");
        params.put("action", "get_all_student");///

        client.post(HttpUtil.server_get_student, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        // TODO Auto-generated method stub
                        studentId1 = new int[arg1.optJSONArray("result1").length()];
                        studentId2 = new int[arg1.optJSONArray("result2").length()];
                        studentId3 = new int[arg1.optJSONArray("result3").length()];

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
                                TextView textView = new TextView(StudentOfCourseAddActivity.this);
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
                                LinearLayout ll = new LinearLayout(StudentOfCourseAddActivity.this);
                                ll.setOrientation(0);
                                ImageView logo = new ImageView(StudentOfCourseAddActivity.this);
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
                        expandListView = (ExpandableListView) findViewById(R.id.list);
                        expandListView.setOnChildClickListener(
                                new OnChildClickListener() {

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


    }

    private void initTitle() {
        mTitleView.setTitle("添加学生");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                StudentOfCourseAddActivity.this.finish();
            }
        });
        mTitleView.setRightButton("添加", new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {

                //判断数据
                 if (selected.getText()==null||selected.getText().toString().equals("")){
                     Toast.makeText(context,"请选择一个学生",Toast.LENGTH_SHORT).show();
                     return;
                 }

                RequestParams params = new RequestParams();

                for (int i = 0; i < para.size(); i++) {
                    params.put("studentid" + i, para.get(i) + "");
                }
                params.put("studentsize", para.size() + "");
                params.put("courseid", courseid + "");
                params.put("action", "get_all_student");///

                client.post(HttpUtil.server_add_student, params,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, JSONObject arg1) {

                                /*String addStudentToCourse_msg = arg1.optString("result");
                                DialogUtil.showDialog(context, addStudentToCourse_msg, true);

                                Intent intent = new Intent(StudentOfCourseAddActivity.this, StudentCourseActivity.class);
                                Bundle bundle = new Bundle();
                                //	bundle.putSerializable("courseList", (Serializable) courseList);
                                bundle.putInt("courseid", courseid);
                                intent.putExtras(bundle);
                                StudentOfCourseAddActivity.this.startActivity(intent);*/
                                StudentOfCourseAddActivity.this.finishActivity(SubActivity.SUCCESS);
                                StudentOfCourseAddActivity.this.finish();

                                super.onSuccess(arg0, arg1);
                            }

                        });
            }
        });
    }

    private void initData() {
        this.context = this;

        client = new AsyncHttpClient();
        Intent intent = getIntent();
        courseid = intent.getExtras().getInt("courseid");
    }

    private void initView() {
        setContentView(R.layout.activity_student_of_course_add);
        mTitleView = (TitleView) findViewById(R.id.student_of_course_add_title);
        selected = (EditText) findViewById(R.id.selected);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_of_course_add, menu);
        return true;
    }
}
