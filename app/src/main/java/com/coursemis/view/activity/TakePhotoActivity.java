package com.coursemis.view.activity;

import com.coursemis.R;
import com.coursemis.R.layout;
import com.coursemis.R.menu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TakePhotoActivity extends Activity implements OnClickListener {
	private LinearLayout dialogLayout;//取消拍照
	private Button takePhotoBtn;//点击拍照
	private Button cancelBtn;
	private Uri photoUri;//自动获取照片uri
	private String picPath;//图片路径
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pic_layout);
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
		takePhotoBtn.setOnClickListener(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			break;
		case R.id.btn_take_photo:
			takePhoto();
			break;
		//采到了case中没列出来的状态,如cancelBtn
		default:
			finish();
			break;
		}
	}
	// 点击拍照
	private void takePhoto() {
		//执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))//如果有媒体安装的环境
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//传拍的照
			/***
			 * 需要说明一下，以下操作使用+拍照，拍照后的图片会存放在相册中的
			 * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();//contentProvider存储？
			photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);//自动获得uri
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/**-----------------*/
			startActivityForResult(intent, 1);//跳到拍照页面，这里1没用到，可以在一个onActivityResult里设置requestCode为0来接收新页面的数据。
		}else{
			Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//这里的三个参数都没用，没有跳再新的页面传值回来
		if(resultCode == Activity.RESULT_OK&&requestCode==1)//&&的后面可有可无，因为只有一个返回结果，若多个就可用switch case判断一下。
		{
			String[] pojo = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(photoUri, pojo, null, null,null);//Uri 和 图片数据   得到  picPath
			if(cursor != null ){
				int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
				cursor.moveToFirst();
				picPath = cursor.getString(columnIndex);
				cursor.close();
			}
			Log.i("SelectPicActivity---------------", "imagePath = "+picPath);
			/*if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))*/
			if(picPath !=null){
				Intent lastIntent = getIntent();//上面putExtra 这里getIntent
				lastIntent.putExtra("photo_path", picPath);//这是新页面，，再传送路径  返送回去EmployeeAttendanceActivity
				setResult(Activity.RESULT_OK, lastIntent);
				finish();
			}else{
				Toast.makeText(this, "选择文件不正确!", Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
