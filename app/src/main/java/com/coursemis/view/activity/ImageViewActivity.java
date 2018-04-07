package com.coursemis.view.activity;

import com.coursemis.R;
import com.coursemis.R.layout;
import com.coursemis.R.menu;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageViewActivity extends Activity {

	ImageView im = null;
	String bitmap=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  // 无title
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_image_view);
		
		im=(ImageView)findViewById(R.id.imageShow);
        Intent intent=getIntent();
        if(intent!=null)
        {
            bitmap=intent.getStringExtra("bitmap");

            Toast.makeText(this, bitmap, 1000);
            BitmapFactory.Options op = new BitmapFactory.Options();
			op.inSampleSize = 10;
			 Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"//"+bitmap,op);
			 im.setImageBitmap(bmp);
            
        }
		
		
	}

}
