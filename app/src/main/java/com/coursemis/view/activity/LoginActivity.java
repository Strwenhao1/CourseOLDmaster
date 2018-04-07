package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Student;
import com.coursemis.model.Teacher;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * 登录界面
 */
public class LoginActivity extends Activity {
	public Context				context;																		///
	private AsyncHttpClient		client;																			// 用于客户端连接服务器端，异步网络数据
	SQLiteDatabase				db;																				// 声明本地数据库
	SharedPreferences			preferences;																	// 声明共享首选项
	Editor	editor;
	private EditText			username;
	private EditText			password;
	private Button				login;
	private RadioGroup			rg_type;
	private String				type			= "学生";
	int[]						type_string_id	= { R.string.user_type_student, R.string.user_type_teacher };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//
		this.context = this;






		client = new AsyncHttpClient();
		preferences = getSharedPreferences("courseMis", 0);
		editor = preferences.edit();
		// 打开或创建本地数据库，名为CourseUser.db3
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/CourseUser.db3", null);
		username = (EditText) findViewById(R.id.login_edit_account);
		password = (EditText) findViewById(R.id.login_edit_pwd);
		login = (Button) findViewById(R.id.login);
		rg_type = (RadioGroup) findViewById(R.id.type);
		rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int RadioButtonId = rg_type.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) LoginActivity.this.findViewById(RadioButtonId);
				type = "" + rb.getText();
			}
		});
		try {
			Cursor cursor = db.rawQuery("select * from user_info", null);
			// 填充name和password
			if (cursor.moveToLast()) {
				username.setText(cursor.getString(cursor.getColumnIndex("user_name")));
				if (cursor.getString(cursor.getColumnIndex("user_type")).equals("教师")) {
					rg_type.check(R.id.teacher);
				} else if (cursor.getString(cursor.getColumnIndex("user_type")).equals("学生")) {
					rg_type.check(R.id.student);
				}
			}
		} catch (SQLiteException se) {
		}
		/**
		 * 登录按钮的点击事件
		 */

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (validate()) {
					 //将要提交服务器端的数据包装到RequestParams中
					RequestParams params = new RequestParams();
					String x1 = username.getText().toString().trim();
					String x2 = password.getText().toString().trim();
					String x3 = type;
					params.put("name", username.getText().toString().trim());
					params.put("password", password.getText().toString().trim());
					params.put("type", type);// 用户类型
					params.put("power", ""); ///
					params.put("action", "login");///

					try {

						 /* 与服务器端进行数据交流 第一个参数指服务器端的网址和进行处理的action params包装从客户端到服务器端的数据*/
						Log.e("数据",params.toString()) ;
						client.post(HttpUtil.server_login, params, new JsonHttpResponseHandler() {
							@Override
							public void onFailure(Throwable arg0, JSONObject arg1) { // 登录失败
								// TODO Auto-generated method stub
								DialogUtil.showDialog(context, "用户名或密码错误", false);
//								Log.e("数据",arg0.toString()) ;
								super.onFailure(arg0, arg1);
							}
							@Override
							public void onSuccess(int arg0, JSONObject arg1) { // 登录成功
								// TODO Auto-generated method stub
								//DialogUtil.showDialog(context, arg1.toString(), true);
								login.setClickable(false);
//								System.out.println(arg1.toString());
								Log.e("数据",arg1.toString()) ;
								JSONObject object = arg1.optJSONArray("result").optJSONObject(0);
								if (type.equals("教师")) {
									Teacher teacher = new Teacher();
									if (object != null && !object.equals("")) {
										teacher.setTName(object.optString("TName"));
										teacher.setTPassword(object.optString("TPassword"));
										teacher.setTId(object.optInt("TId"));
										try {
											insertData(db, teacher.getTName(), "教师");
										} catch (SQLiteException se) {
											// 执行DDL创建数据表
											db.execSQL("create table user_info(_id integer primary key autoincrement,"
													+ "user_name varchar(50),"
													+ "user_type varchar(8))");
											// 执行insert语句插入数据
											insertData(db, teacher.getTName(), "教师");
										}
										// 下述代码保存用户信息在不同的activity之间分享数据,存储简单的用户信息
										editor.putString("userID", teacher.getTId() + "");
										editor.putString("userName", teacher.getTName());
										editor.putString("userPassword", teacher.getTPassword());
										editor.putInt("teacherid", teacher.getTId());
										editor.putString("type", "教师");
										editor.commit(); // 提交数据
										Editor sharedata = getSharedPreferences("data", 0).edit();
										sharedata.putString("userName", teacher.getTName());
										sharedata.putString("userID", teacher.getTId() + "");
										sharedata.putString("userPassword", teacher.getTPassword());
										sharedata.commit();
										Toast.makeText(context, "恭喜您" + teacher.getTName() + "登录成功", Toast.LENGTH_SHORT)
												.show();
										// 将一些基本信息通过Intent传递到下一个Activity
										Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("teacher", teacher);
										bundle.putInt("teacherid", teacher.getTId());
										bundle.putString("type", type);
										intent.putExtras(bundle);
										intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) ;
										LoginActivity.this.startActivity(intent);
										//LoginActivity.this.finish();
									}
								} else if (type.equals("学生")) {
									Student student = new Student();
									if (object != null && !object.equals("")) {
										student.setSName(object.optString("SName"));
										student.setSPassword(object.optString("SPassword"));
										student.setSId(object.optInt("SId"));
										try {
											insertData(db, student.getSName(), "学生");
										} catch (SQLiteException se) {
											// 执行DDL创建数据表
											db.execSQL("create table user_info(_id integer primary key autoincrement,"
													+ "user_name varchar(50),"
													+ "user_type varchar(8))");
											// 执行insert语句插入数据
											insertData(db, student.getSName(), "学生");
										}
										// 下述代码保存用户信息在不同的activity之间分享数据,存储简单的用户信息
										editor.putString("userID", student.getSId() + "");
										editor.putString("userName", student.getSName());
										editor.putString("userPassword", student.getSPassword());
										editor.putInt("studentid", student.getSId());
										editor.putString("type", "学生");
										editor.commit(); // 提交数据
										Editor sharedata = getSharedPreferences("data", 0).edit();
										sharedata.putString("userName", student.getSName());
										sharedata.putString("userID", student.getSId() + "");
										sharedata.putString("userPassword", student.getSPassword());
										sharedata.commit();
										Toast.makeText(context, "恭喜您" + student.getSName() + "登录成功", Toast.LENGTH_SHORT)
												.show();
										// 将一些基本信息通过Intent传递到下一个Activity
										Intent intent = new Intent(LoginActivity.this, StudentManager.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("student", student);
										bundle.putInt("studentid", student.getSId());
										bundle.putString("type", type);
										intent.putExtras(bundle);
										intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) ;
										LoginActivity.this.startActivity(intent);
										//LoginActivity.this.finish();
									}
								}
								super.onSuccess(arg0, arg1);
								login.setClickable(true);
							}
						});
					} catch (Exception e) {
						Log.e("数据",e.toString()) ;
						DialogUtil.showDialog(context, "服务器响应异常，请稍后再试", true);
						e.printStackTrace();
					}
				}
			}
		});
	}
	// 在本地数据库添加一条记录
	private void insertData(SQLiteDatabase db, String name, String type) {
		String sql = "insert into user_info values(null,?,?)";
		db.execSQL(sql, new String[] { name, type });
	}
	// 对用户输入的用户名、密码进行校验
	private boolean validate() {
		String name = username.getText().toString().trim();
		if (name.equals("")) // 验证用户名是否为空
		{
			DialogUtil.showDialog(this, "用户账户是必填项！", false);
			return false;
		}
		String pwd = password.getText().toString().trim();
		if (pwd.equals("")) // 验证密码是否为空
		{
			DialogUtil.showDialog(this, "用户密码是必填项！", false);
			return false;
		}
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
