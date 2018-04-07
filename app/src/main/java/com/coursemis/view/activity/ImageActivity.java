package com.coursemis.view.activity;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coursemis.R;
import com.coursemis.adapter.HomeworkAdapter;
import com.coursemis.manage.WebService;
import com.coursemis.util.HttpUtil;
import com.coursemis.thread.FormFile;
import com.coursemis.thread.UploadThread;
import com.coursemis.util.UploadImageUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageActivity extends Activity implements OnClickListener {


    private List<Uri> mUriList;
    String imagePath = null;
    private Button selectImage, uploadImage, takeImage;
    //private ImageView imageView;
    private String TAG = "ImageActivity";
    String request;
    private String picPath = null;
    Bitmap bitmap = null;

    private String cid = null;
    private String tid = null;
    Intent intent = null;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WebService.CON_STATE:
                    Toast.makeText(ImageActivity.this, "" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private RecyclerView mBookList;
    private HomeworkAdapter mAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView() ;
        initData() ;
        initListener() ;
        initList() ;
    }

    private void initList() {
        mBookList.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter = new HomeworkAdapter(this);
        mBookList.setAdapter(mAdapter);
    }

    private void initListener() {
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        takeImage.setOnClickListener(this);
    }

    private void initData() {
        intent = getIntent();
        cid = intent.getStringExtra("cid");
        tid = intent.getStringExtra("tid");
        mUriList = new ArrayList<>() ;
    }

    private void initView() {
        setContentView(R.layout.activity_image);

        selectImage = (Button) this.findViewById(R.id.selectImage);
        uploadImage = (Button) this.findViewById(R.id.uploadImage);
        takeImage = (Button) this.findViewById(R.id.takeImage);
        //imageView = (ImageView) this.findViewById(R.id.imageView);
        mBookList = (RecyclerView) findViewById(R.id.iv_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectImage:
                Intent intent = new Intent();// intent????????????????????
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 22);
                break;
            case R.id.takeImage:
                Intent intent2 = new Intent(getApplicationContext(),
                        TakePhotoActivity.class);
                startActivityForResult(intent2, 23);
                // ???????????????????????????
                break;
            case R.id.uploadImage:
                UploadImageUtil.uploadImage(this,mUriList,cid,tid,"作业名称",30);
                break;
            default:
                break;
        }// onclick????

    }// oncreate????

    // ??????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 22:
                    Uri uri = data.getData();
                    //判断Uri
                    checkUri(uri) ;
                    mAdapter.refreshData(mUriList);
                    break;
                case 23:
                    picPath = data.getStringExtra("photo_path");// ??SelectPicActivity.java?��?????��??
                    imagePath = picPath;
                    Log.i("uploadImage", "??????????��??=" + picPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath);// ???????��???????��?
                    //imageView.setImageBitmap(bitmap);// ??��????????
                    break;

            }// switch????
        }// if????
    }// onActivityResult????

    private void checkUri(Uri uri) {
        for (Uri uri1 : mUriList){
            if (uri.equals(uri1)){
                Toast.makeText(this,"这张图片你已经选择了",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mUriList.add(uri);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("???")
                .setMessage("???????????��????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }


    public void uploadFile(File imageFile) {
        Log.i(TAG, "upload start");
        try {
            String requestUrl = HttpUtil.server_teacher_tupLoadHomework;
            //??????????
            Map<String, String> params = new HashMap<String, String>();
            params.put("cid", cid);
            params.put("tid", tid);
            params.put("age", "23");
            //??????,????????????struts2????????
            FormFile formfile = new FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");
            UploadThread uploadThread = new UploadThread(requestUrl, params, formfile);

            Thread t1 = new Thread(uploadThread);
            t1.start();


            Log.i(TAG, "upload success");
            Toast.makeText(ImageActivity.this, "?????????...", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.i(TAG, "upload error");
            e.printStackTrace();
        }
        Log.i(TAG, "upload end");
    }


}
