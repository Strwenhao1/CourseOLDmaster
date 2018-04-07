package com.coursemis.view.activity;

import java.util.ArrayList;


import org.json.JSONObject;

import com.coursemis.view.myView.TitleView;
import com.coursemis.R;
import com.coursemis.model.AskStudent;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: TRandomAskActivity
 * @Description: 显示课堂提问页面
 * 老师提问之后评分。点击继续。
 * @author: ningbo
 * @date: 2017年5月7日 下午5:37:05
 */
public class TAskQuestionActivity extends Activity implements View.OnClickListener{
    private TitleView mTitle=null;
	
	Intent intent =null;
	ArrayList<String> studentName=null;
	String TMN_C_INFO=null;
	TextView tv_showClass,tv_showNumber,tv_showName;
	EditText et_setGrade;
	ListView studentList=null;
	AskStudent askStudent=null;
	Button submit;
	 AsyncHttpClient client = new AsyncHttpClient();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_askquestionactivity);
		tv_showClass=(TextView)this.findViewById(R.id.showClass);

		tv_showName=(TextView)this.findViewById(R.id.showName);
		mTitle=(TitleView)findViewById(R.id.title_dianming1);
		submit=(Button)findViewById(R.id.submit);
		et_setGrade=(EditText)findViewById(R.id.setGrade);
		
		intent=getIntent();
		
		askStudent=(AskStudent)intent.getSerializableExtra("askStudent");
		
		TMN_C_INFO=intent.getStringExtra("tmn_c_info");
		
		
	
		mTitle.setTitle(TMN_C_INFO);
		
		initView();
		setListener();
		
		
	}

	/** 
	 * @Title: setListener 
	 * @Description: TODO
	 * @return: void
	 */
	private void setListener() {
        submit.setOnClickListener(this);
		
	}

	/** 
	 * @Title: initView 
	 * @Description: TODO
	 * @return: void
	 */
	private void initView() {
		
//		tv_showClass.setText(askStudent.getsNumber());
		tv_showClass.setText(askStudent.getcName());
		
		tv_showName.setText(askStudent.getsName());
       		
		
		
	
	   
	  
	
	}

	
	
	@Override
	public void onClick(View v) {
         switch(v.getId()){
    	    case R.id.submit :
    	    	P.p("=====================");
    		  Toast.makeText(this, "点击", Toast.LENGTH_SHORT);
    		  String grade= et_setGrade.getText().toString();
		      RequestParams params = new RequestParams();
		      params.put("scId", askStudent.getScId()+"");
		      params.put("cName", askStudent.getcName());
		      params.put("SNumber", askStudent.getsNumber()+"");
		      params.put("SName", askStudent.getsName());
              params.put("twxx", TMN_C_INFO);
              params.put("grade", grade+"");
			  client.post(HttpUtil.server_teacher_course_randomAsk_submit, params,
						new JsonHttpResponseHandler() {
				 			
							@Override
							public void onSuccess(int arg0, JSONObject arg1) {
							
								super.onSuccess(arg0, arg1);
								
								TAskQuestionActivity.this.finish();

							}
							
							@Override
							public void onFailure(Throwable arg0) {
								Toast.makeText(TAskQuestionActivity.this, "提交异常请稍后重试", Toast.LENGTH_SHORT);
							}
						});
    		 break;
      }
		
	}
}
