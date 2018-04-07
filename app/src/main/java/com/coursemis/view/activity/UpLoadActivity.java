package com.coursemis.view.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.coursemis.R;
import com.coursemis.thread.FormFile;
import com.coursemis.thread.UploadThread;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpLoadActivity extends Activity {

	 private File file;
	    private static final String TAG="MainActivity";
	    private TextView textView;
	    private Button button; 
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_up_load);
	        
	        textView = (TextView)findViewById(R.id.textView);
	        button = (Button)findViewById(R.id.upload);
	        
	        file = new File(Environment.getExternalStorageDirectory(), "zb.JPG");
	        
	        button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.v("111","去死吧 还是跳不好");
					uploadFile(file);
				}
			});
	    }
	    
	    /**
	     * 上传图片到服务器
	     * 
	     * @param imageFile 路径
	     */
	    public void uploadFile(File imageFile) {
	        Log.i(TAG, "upload start");
	        try {
	            String requestUrl = "http://192.168.173.1:8080/upload_serve/execute.do";
	            //请求普通信息
	            Map<String, String> params = new HashMap<String, String>();
	            params.put("username", "张三");
	            params.put("pwd", "123456");
	            params.put("age", "23");
	            //上传文件,第三个参数是struts2接收的参数
	            FormFile formfile = new FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");
	            UploadThread uploadThread = new UploadThread(requestUrl,params,formfile);
	            
	            Thread t1 = new Thread(uploadThread);
	            t1.start();
	            /*new Thread(){
	 @Override
	 public void run(String requestUrl,)
	{
	 //这里放你的那段访问网络的代码
	 //代码执行完毕后给handler发送消息
	 handler.sendEmptyMessage(0);
	 }
	 }.start();*/
	            
	            Log.i(TAG, "upload success");
	            textView.setText("上传成功");
	        } catch (Exception e) {
	            Log.i(TAG, "upload error");
	            e.printStackTrace();
	        }
	        Log.i(TAG, "upload end");
	    }
}
