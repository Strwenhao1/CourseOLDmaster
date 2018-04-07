package com.coursemis.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.coursemis.R;
import com.coursemis.service.WebService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NetActivity extends Activity {

	 private EditText picaddress;
	   private Button button;
	   private ImageView imageView;
	   
	   
	   /**
	    *保存到sd卡上的方法
	    */
	   public void saveMyBitmap(String bitName,Bitmap mBitmap){
		   File f = new File("/sdcard/" + bitName + ".JPG");
		   try {
		    f.createNewFile();
		   } catch (IOException e) {
		    // TODO Auto-generated catch block
		    Log.v("在保存图片时出错：",e.toString());
		   }
		   FileOutputStream fOut = null;
		   try {
		    fOut = new FileOutputStream(f);
		   } catch (FileNotFoundException e) {
		    e.printStackTrace();
		   }
		   mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		   try {
		    fOut.flush();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		   try {
		    fOut.close();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		  } 
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_net);
	        button=(Button)this.findViewById(R.id.button);
	        imageView=(ImageView)this.findViewById(R.id.image);
	        picaddress=(EditText)this.findViewById(R.id.imageaddress);
	        button.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					String address=picaddress.getText().toString();
					
					try {
					
					byte[] data=WebService.getImage(address); //得到图片的输入流
//						
//						//二进制数据生成位图
						Bitmap bit=BitmapFactory.decodeByteArray(data, 0, data.length);
						saveMyBitmap("zb1",bit);
						
						Intent intent = new Intent(NetActivity.this,ImageViewActivity.class);
						intent.putExtra("bitmap", "zb1.JPG");
						startActivity(intent);
						

//						BitmapFactory.Options op = new BitmapFactory.Options();
//						op.inSampleSize = 10;
//						 Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+  "/zb1.JPG",op);
//						 imageView.setImageBitmap(bmp);
						 
						 
					} catch (Exception e) {
					    Log.e("NetActivity", e.toString());
						
						Toast.makeText(NetActivity.this, "下载出错", 1).show();
					}
				}
			});
	    }

}
