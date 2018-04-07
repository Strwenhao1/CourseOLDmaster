package com.coursemis.view.activity;

import java.io.File;


import com.coursemis.R;
import com.coursemis.model.Media;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class AudioPlayerActivity extends Activity {

	Intent intent = null;
	String videoPath=null;
	Media media;
	 private VideoView vv_video;
	    private MediaController mController;
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_audio_player);
	        intent = getIntent();
	        media = (Media) intent.getSerializableExtra("videoPath");
	        vv_video=(VideoView) findViewById(R.id.vv_video);
			videoPath = media.getPath();
	        // 实例化MediaController
	        mController=new MediaController(this);
	        File file=new File(videoPath);
	        if(file.exists()){
	            // 设置播放视频源的路径
	        	String path = file.getAbsolutePath();
				Log.e("e", path,null );
				vv_video.setVideoPath(videoPath);
	            // 为VideoView指定MediaController
	            vv_video.setMediaController(mController);
	            // 为MediaController指定控制的VideoView
	            mController.setMediaPlayer(vv_video);
	            // 增加监听上一个和下一个的切换事件，默认这两个按钮是不显示的
	            mController.setPrevNextListeners(new OnClickListener() {
	                
	                @Override
	                public void onClick(View v) {                    
	                    Toast.makeText(AudioPlayerActivity.this, "下一个",Toast.LENGTH_SHORT).show();
	                }
	            }, new OnClickListener() {
	                
	                @Override
	                public void onClick(View v) {
	                    Toast.makeText(AudioPlayerActivity.this, "上一个",Toast.LENGTH_SHORT).show();
	                }
	            });
	            vv_video.start();
	            if(videoPath.endsWith("mp3"))
	            {
	            	Toast.makeText(AudioPlayerActivity.this, "您播放的是音频文件，所以无法查看视频",Toast.LENGTH_LONG).show();
	            }
	        }
	    }

}
