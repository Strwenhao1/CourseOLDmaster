package com.coursemis.view.activity;

import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessageInfoActivity extends Activity {
	public Context context;////
	private AsyncHttpClient client;
	
	private TextView et_phone;
    private TextView msg_table;
    private EditText et_content;
    private Button bt_send;
    
    String name =new String();
    Vector<String>numbers = new Vector<String>();
    
    private int teacherid;
    private int courseid;
    private String CName;
    String all_msg= new String();//用于存放从数据库中读取的以往发送过的消息
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);
        
        this.context = this;
        client  = new AsyncHttpClient();
        
        et_phone = (TextView) findViewById(R.id.msg_phone);
        msg_table = (TextView) findViewById(R.id.msg_table);
        et_content = (EditText)findViewById(R.id.msg_content);
        bt_send = (Button)findViewById(R.id.msg_send);

        Intent intent=getIntent();  
   	 	Bundle bundle=intent.getExtras(); 
   	 	name = bundle.getString("CName"); //获取课程名称
   	 	courseid = intent.getExtras().getInt("courseid"); //获取课程id
        teacherid = intent.getExtras().getInt("teacherid");
        CName = intent.getExtras().getString("CName");
        
        Toast.makeText(context,"tid:"+teacherid+"  cid:"+courseid, Toast.LENGTH_SHORT).show();
        
       // load();
       
		//loadMessage();
        /**获取已发过的信息和要发的号码**/
		RequestParams params = new RequestParams();
		params.put("teacherid", teacherid+"");
		params.put("courseid", courseid+"");
		params.put("CName", CName);
		params.put("action", "server_message_get");
		
	  	client.post(HttpUtil.server_note_get, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
					
						for(int i=0;i<arg1.optJSONArray("result").length();i++){
							JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
							
							String content_temp = object.optString("NContent");
							String datetime_temp = object.optString("NDatetime");
							
							
							all_msg = all_msg+content_temp+'\n'+datetime_temp+'\n';
						}
						msg_table.setText(all_msg);
						
						for(int i=0;i<arg1.optJSONArray("result2").length();i++){
							JSONObject object = arg1.optJSONArray("result2").optJSONObject(i);
							
							String number = new String();
							number = object.optString("STel");
							numbers.add(number);
						}
						
						super.onSuccess(arg0, arg1);
					}
		});
	  	
		
        et_phone.setText(name);
        
   	 	et_phone.setBackgroundColor(Color.GRAY);
   	 	
   	 bt_send.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
            	 String content  = new String();
            	 String mobile = new String();
            	 content = et_content.getText().toString();
            	 Object []objects = new String [numbers.size()];
            	 objects = numbers.toArray();
            	 
            	for(int i=0;i<numbers.size() ;i++)
            	{
            		mobile = (String) objects[i];
            		//发送短信，功能已经注释掉了
                ///  
                if(mobile.length()<1)return ;
                if(content.length()<1){
                	Toast.makeText(getApplicationContext(), "请输入内容",
                		     Toast.LENGTH_SHORT).show();
                	return ;
                }
                
                SmsManager smsManager = SmsManager.getDefault();
                PendingIntent sentIntent = PendingIntent.getBroadcast(MessageInfoActivity.this, 0, new Intent(), 0);
                
                if(content.length() >= 70)
                {
                    //短信字数大于70，自动分条
                    List<String> ms = smsManager.divideMessage(content);
                    
                    for(String str : ms )
                    {
                        //短信发送
                        smsManager.sendTextMessage(mobile, null, str, sentIntent, null);
                    }
                }
                else
                {
                    smsManager.sendTextMessage(mobile, null, content, sentIntent, null);
                }      
                 ///

                //下面的功能还没有实现
            	//  Save_Message("Teacher_Contact"+mobile," "+content);
            	//    Save_Message("ss"," "+content);
            	//      String All_Message = Read_Message("Teacher_Contact"+mobile);
            	//    save(content);
                
            	}
            	Toast.makeText(context,"addMessage", Toast.LENGTH_SHORT).show();
            	//addMessage(content);  /**存储已发过的信息**/
            	Toast.makeText(context,"begin...addMessage", Toast.LENGTH_SHORT).show();
            	RequestParams params = new RequestParams();
        		params.put("teacherid", teacherid+"");
        		params.put("name_course", name);
        		params.put("content_message", content+"");
        		
        		client.post(HttpUtil.server_note_add, params,
        				new JsonHttpResponseHandler() {
        					@Override
        					public void onSuccess(int arg0, JSONObject arg1) {
        						Toast.makeText(context,"onSuccess", Toast.LENGTH_SHORT).show();
        						String addMessage_msg = arg1.optString("result");
        						DialogUtil.showDialog(context, addMessage_msg, true);
        						
        						super.onSuccess(arg0, arg1);
        					}
        					
        					@Override
        					public void onFailure(Throwable arg0, JSONObject arg1) {
        						// TODO Auto-generated method stub
        						Toast.makeText(context,"onFailure...", Toast.LENGTH_SHORT).show();
        						System.out.println(arg0.toString());
        						super.onFailure(arg0, arg1);
        					}
        				});
        	  	et_content.setText("");
            	
            	
            	//loadMessage();  /**重新获取已发过的信息**/
            	RequestParams params1 = new RequestParams();
        		params1.put("teacherid", teacherid+"");
        		params1.put("courseid", courseid+"");
        		params1.put("CName", CName);
        		params1.put("action", "server_message_get");
        		
        	  	client.post(HttpUtil.server_note_get, params1,
        				new JsonHttpResponseHandler() {
        					@Override
        					public void onSuccess(int arg0, JSONObject arg1) {
        						Toast.makeText(context,"onsuccess...loadMessage", Toast.LENGTH_SHORT).show();
        					//List<String> messageList = new ArrayList<String>();
        					for(int i=0;i<arg1.optJSONArray("result").length();i++){
        						JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
        						
        						String content_temp = object.optString("MContent");
        						String datetime_temp = object.optString("MDatetime");
        						
        						
        						all_msg = all_msg+content_temp+'\n'+datetime_temp+'\n';
        						
        						//messageList.add(course);
        						
        					}
        					msg_table.setText(all_msg);
        					super.onSuccess(arg0, arg1);
        					}
        				});
                //et_content.setText("");
                
                //Show_View(content);

            }
        });       
    }
    
    /*void load( )
    {	
		 RequestParams params = new RequestParams();
			params.put("teacherid", teacherid+"");
			params.put("courseid", courseid+"");
			
		  	client.post(HttpUtil.server_telNum_get, params,new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, JSONObject arg1) {
							Toast.makeText(context,arg1.toString(), Toast.LENGTH_SHORT).show();
							for(int i=0;i<arg1.optJSONArray("result").length();i++){
								JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
								
								String number = new String();
								number = object.optString("STel");
								numbers.add(number);
							}
							Toast.makeText(context,numbers.toString(), Toast.LENGTH_SHORT).show();
							super.onSuccess(arg0, arg1);
						}
					});
			
    		String ss = new String();
			for(int i=0;i<7;i++)
			{
				numbers.add(new String(i+"    "));
			}
    }*/
    
    void loadMessage(){ //20131219
    	/*RequestParams params1 = new RequestParams();
		params1.put("teacherid", teacherid+"");
		params1.put("action", "server_message_get");
		
	  	client.post(HttpUtil.server_message_get, params1,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						Toast.makeText(context,"onsuccess...loadMessage", Toast.LENGTH_SHORT).show();
					//List<String> messageList = new ArrayList<String>();
					for(int i=0;i<arg1.optJSONArray("result").length();i++){
						JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
						
						String content_temp = object.optString("MContent");
						String datetime_temp = object.optString("MDatetime");
						
						
						all_msg = all_msg+content_temp+'\n'+datetime_temp+'\n';
						
						//messageList.add(course);
						
					}
					msg_table.setText(all_msg);
					super.onSuccess(arg0, arg1);
					}
				});*/
    }
    
    
    //@SuppressLint("SimpleDateFormat")
	/*public void Show_View(String content)
    {

	     SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("          yyyy年MM月dd日    HH:mm:ss     ");       
	     Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间        
	     String    str    =    formatter.format(curDate);   
  
    	all_msg = all_msg+content+'\n'+str+'\n';
    	msg_table.setText(all_msg);
    }
*/
    
    /*void addMessage(String messageContent){
    	Toast.makeText(context,"begin...addMessage", Toast.LENGTH_SHORT).show();
    	RequestParams params = new RequestParams();
		params.put("teacherid", teacherid+"");
		params.put("name_course", name);
		params.put("content_message", messageContent+"");
		
	  	client.post(HttpUtil.server_message_add, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						String addMessage_msg = arg1.optString("result");
						DialogUtil.showDialog(context, addMessage_msg, true);
						
						super.onSuccess(arg0, arg1);
					}
				});
	  	et_content.setText("");
    }*/
    
}







