package com.coursemis.view.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.coursemis.thread.FormFile;
import com.coursemis.thread.UploadThread;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentHMUploadActivity extends Activity implements OnClickListener{


	String imagePath=null;
	private Button selectImage, uploadImage, takeImage;
	private ImageView imageView;
	private String TAG = "ImageActivity";
	String request;
	private String picPath = null;
	Bitmap bitmap = null;
	TextView textView=null;
	private String smid=null;
	private String sid = null;
	Intent intent=null;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_hmupload);
		 intent = getIntent();
		smid = intent.getStringExtra("smid");
		sid = intent.getStringExtra("sid");
		
		selectImage = (Button) this.findViewById(R.id.selectImage2);
		uploadImage = (Button) this.findViewById(R.id.uploadImage2);
		takeImage = (Button) this.findViewById(R.id.takeImage2);
		
		imageView = (ImageView) this.findViewById(R.id.imageView2);
		selectImage.setOnClickListener(this);
		uploadImage.setOnClickListener(this);
		takeImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage2:
			Intent intent = new Intent();// intent可以过滤图片文件或其他的
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 22);
			break;
		case R.id.takeImage2:
			Intent intent2 = new Intent(getApplicationContext(),
					TakePhotoActivity.class);
			startActivityForResult(intent2, 23);
			// 这里是上传图片的点击，上传不了。。
			break;
		case R.id.uploadImage2:
			if(imagePath!=null)
			{
			File file = new File(imagePath);
			uploadFile(file);
			finish();
			}else
			{
				Toast.makeText(StudentHMUploadActivity.this, "你没有选择任何图片！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}// onclick结束

	}// oncreate结束

	// 返回查找图片和拍照的结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 22:
				Uri uri = data.getData();
				imagePath=uri.toString();
				Log.e("Upload主页---L92--获得相册图片---------", "uri = " + uri);
				try {
					String[] pojo = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(uri, pojo, null, null, null);
					if (cursor != null) {
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String path = cursor.getString(colunm_index);
						if (path.endsWith("jpg") || path.endsWith("png")) {
							imagePath = path;
							ContentResolver cr = this.getContentResolver();
							Bitmap bitmap = BitmapFactory.decodeStream(cr
									.openInputStream(uri));
							imageView.setImageBitmap(bitmap);	
						} else {
							alert();
						}
					} else {
						alert();
					}
				} catch (Exception e) {
				}
				break;
			case 23:
				imageView.setImageBitmap(null);// 设置图片位图不设null的话,
												// 第一次上传照片成功后，第二次上传照片会报错。
				picPath = data.getStringExtra("photo_path");// 从SelectPicActivity.java中传回图片路径
				imagePath=picPath;
				Log.i("uploadImage", "最终选择的图片路径=" + picPath);
				Bitmap bitmap = BitmapFactory.decodeFile(picPath);// 把获得的图片路径译码成位图
				imageView.setImageBitmap(bitmap);// 把位图显示出来
				break;

			}// switch结束
		}// if结束
	}// onActivityResult结束

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

	
	 public void uploadFile(File imageFile ) {
	        Log.i(TAG, "upload start");
	        try {
	            String requestUrl = HttpUtil.server_student_supLoadHomework;
	            //请求普通信息
	            Map<String, String> params = new HashMap<String, String>();
	            params.put("smid", smid+"");
	            params.put("sid", sid+"");
	            params.put("age", "23");
	            //上传文件,第三个参数是struts2接收的参数
	            FormFile formfile = new FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");
	            UploadThread uploadThread = new UploadThread(requestUrl,params,formfile);
	            
	            Thread t1 = new Thread(uploadThread);
	            t1.start();
	           
	            
	            Log.i(TAG, "upload success");
	            Toast.makeText(StudentHMUploadActivity.this, "作业上传成功！", Toast.LENGTH_SHORT).show();
	        } catch (Exception e) {
	            Log.i(TAG, "upload error");
	            e.printStackTrace();
	        }
	        Log.i(TAG, "upload end");
	    }

}
