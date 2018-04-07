package com.coursemis.view.activity;

import android.provider.MediaStore.Video.Thumbnails;  
import android.provider.MediaStore.Video.VideoColumns;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.coursemis.thread.FormFile;
import com.coursemis.thread.UploadThread;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AudioUploadOperationActivity extends Activity {  
	  
	String TAG = "AudioUploadOperation";
    private static final int RECORD_VIDEO = 1;  
    private static final int PLAY_VIDEO = 2;  
    private String path=null;
    int id ;
    private ImageView iv = null;  
      
    //缩略图  
    private Bitmap bitmap = null;  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_audio_upload_operation);  
        iv = (ImageView) findViewById(R.id.audiouploadoperationactivity_iv);  
    }  
      
    /**录制视频**/  
    public void startrecord(View view) {  
        Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);  
        mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);  
        startActivityForResult(mIntent, RECORD_VIDEO);  
    }  
      
    /**播放视频**/  
    public void playvideo(View view) {  
    	if(path==null)
    	{
    		Toast.makeText(AudioUploadOperationActivity.this,"您还没有拍摄视频", Toast.LENGTH_SHORT).show();
    	}else
    		
    	{
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.parse(path),  
                "video/mp4");  
        startActivityForResult(intent, PLAY_VIDEO); 
    	}
    }  
  
    /**返回**/
    public void ButtonOnclick_audiouploadoperationactivity_back(View view)
    {
    	finish();
    }
    
    
    public void ButtonOnclick_audiouploadoperationactivity_upload(View view)
    {
    	if(path==null)
    	{
    		Toast.makeText(AudioUploadOperationActivity.this,"您还没有拍摄视频", Toast.LENGTH_SHORT).show();
    	}else
    	{
    		Toast.makeText(AudioUploadOperationActivity.this,"上传中请等待...", Toast.LENGTH_SHORT).show();
    		File file = new File(path);
			uploadFile(file);
			finish();
    	}
    }
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode,  
            Intent intent) {  
        super.onActivityResult(requestCode, resultCode, intent);  
        if (resultCode != RESULT_OK) {  
            return;  
        }  
        switch (requestCode) {  
        case RECORD_VIDEO:  
            // 录制视频完成  
            try {  
            	 AssetFileDescriptor videoAsset = getContentResolver()  
	                        .openAssetFileDescriptor(intent.getData(), "r");  
	                FileInputStream fis = videoAsset.createInputStream();
	                SharedPreferences  sharedata=getSharedPreferences("courseMis", 0);
	                 id = Integer.parseInt(sharedata.getString("userID",null));
	                File tmpFile = new File(  
	                		"/sdcard/CourseMisRecord/"+ id+ new DateFormat().format("yyyyMMdd_HHmmss",Calendar.getInstance(Locale.CHINA))+ 
	                        ".mp4");  
	                if(!tmpFile.exists()){
	                	tmpFile.createNewFile();
	                }
                tmpFile.createNewFile();
                path = tmpFile.getAbsolutePath();
                FileOutputStream fos = new FileOutputStream(tmpFile);  
  
                byte[] buf = new byte[1024];  
                int len;  
                while ((len = fis.read(buf)) > 0) {  
                    fos.write(buf, 0, len);  
                }  
                fis.close();  
                fos.close();  
                // 文件写完之后删除/sdcard/dcim/CAMERA/XXX.MP4  
                deleteDefaultFile(intent.getData()); 
                
                
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            iv.setImageBitmap(bitmap);  
            break;  
  
        case PLAY_VIDEO:  
            // 播放视频完成  
            Log.d("play", "--over---");  
            break;  
        }  
  
    }  
    public void uploadFile(File soundFile ) {
        Log.i(TAG, "upload start");
        try {
            String requestUrl = HttpUtil.server_ShareMediaData;
            //请求普通信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", id+"");
            //上传文件,第三个参数是struts2接收的参数
            FormFile formfile = new FormFile(soundFile.getName(), soundFile,"video", "audio/mpeg");
            UploadThread uploadThread = new UploadThread(requestUrl,params,formfile);
            
            Thread t1 = new Thread(uploadThread);
            t1.start();
           
            
            Log.i(TAG, "upload success");
            Toast.makeText(AudioUploadOperationActivity.this,"上传成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.i(TAG, "upload error");
            e.printStackTrace();
        }
        Log.i(TAG, "upload end");
    }
	
    // 删除在/sdcard/dcim/Camera/默认生成的文件  
    private void deleteDefaultFile(Uri uri) {  
        String fileName = null;  
        if (uri != null) {  
            // content  
            Log.d("Scheme", uri.getScheme().toString());  
            if (uri.getScheme().toString().equals("content")) {  
                Cursor cursor = this.getContentResolver().query(uri, null,  
                        null, null, null);  
                if (cursor.moveToNext()) {  
                    int columnIndex = cursor  
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);  
                    fileName = cursor.getString(columnIndex);  
                    //获取缩略图id  
                    int id = cursor.getInt(cursor  
                            .getColumnIndex(VideoColumns._ID));  
                    //获取缩略图  
                    bitmap = Thumbnails.getThumbnail(  
                            getContentResolver(), id, Thumbnails.MICRO_KIND,  
                            null);  
  
                    if (!fileName.startsWith("/mnt")) {  
                        fileName = "/mnt/" + fileName;  
                    }  
                    Log.d("fileName", fileName);  
                }  
            }  
        }  
        // 删除文件  
        File file = new File(fileName);  
        if (file.exists()) {  
            file.delete();  
            Log.d("delete", "删除成功");  
        }  
    }  
} 
