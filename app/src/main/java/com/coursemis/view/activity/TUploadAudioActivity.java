package com.coursemis.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.adapter.RadioListAdapter;
import com.coursemis.thread.FormFile;
import com.coursemis.thread.UploadThread;
import com.coursemis.util.FileUtil;
import com.coursemis.util.HttpDownloader;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//教师的资源共享界面
public class TUploadAudioActivity extends Activity {
    String TAG = "UploadAudioActivity";
    private static final int RECORD_VIDEO = 1;
    private ListView upa = null;
    int id = 0;
    private String soundPath = null;
    private String sminfo = null;
    Intent intent = null;
    List<String> mediainfolist = null;
    private MediaRecorder mr;
    private AsyncHttpClient client = new AsyncHttpClient();
    private Handler handler = new Handler() {

        @Override

        //当有消息发送出来的时候就执行Handler的这个方法

        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            //处理UI
            Toast.makeText(TUploadAudioActivity.this, "资源已经下载", Toast.LENGTH_SHORT).show();

        }

    };
    private TitleView mTitleView;
    private Button mDownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_upload_audio);
        mDownload = (Button) findViewById(R.id.uploadaudio__down);
        intent = getIntent();
        mediainfolist = intent.getStringArrayListExtra("mediainfolist");
        upa = (ListView) findViewById(R.id.uploadaudio_listview);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ua_add);
        ArrayList<String> mediainfolist_temp = new ArrayList<String>();
        for (int i = 0; i < mediainfolist.size(); i++) {
            String temp = mediainfolist.get(i);
            String temp1 = temp.substring(temp.indexOf(" ") + 1, temp.length());
            mediainfolist_temp.add(temp1);
        }


        ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_checked, mediainfolist_temp);
        upa.setAdapter(aaRadioButtonAdapter);
        upa.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        upa.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                sminfo = mediainfolist.get(arg2);
                //判断本地是否存在文件
                File file = new File(getExternalFilesDir(null).getAbsolutePath()+"/"+sminfo) ;
                if (file.exists()){
                    //已经存在
                    //修改按钮
                    mDownload.setText("已下载");
                    mDownload.setTextColor(Color.GRAY);
                    mDownload.setEnabled(false);
                }else {
                    //已经存在
                    //修改按钮
                    mDownload.setText("下载");
                    mDownload.setTextColor(Color.WHITE);
                    mDownload.setEnabled(true);
                }
            }

        });

        //使用RecyclerView做的实现
        /*upa.setLayoutManager(new LinearLayoutManager(this));
        upa.setAdapter(new RadioListAdapter(this,mediainfolist));*/


        initTitle();

    }

    private void initTitle() {
        mTitleView = (TitleView) findViewById(R.id.upload_audio_title);
        mTitleView.setTitle("资源共享");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                TUploadAudioActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload_audio, menu);
        return true;
    }

    public void ButtonOnclick_uploadaudio__down(View view) {

        if (sminfo == null) {
            Toast.makeText(TUploadAudioActivity.this, "您没有选择哪份作业", Toast.LENGTH_SHORT).show();
        } else {
            String url = HttpUtil.server + "/mediaShared/" + sminfo;
            HttpDownloader.download(this, url, null, new HttpDownloader.DownloadListener() {
                @Override
                public void done() {
                    Toast.makeText(TUploadAudioActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDownload.setText("已下载");
                            mDownload.setTextColor(Color.GRAY);
                            mDownload.setEnabled(false);
                        }
                    });
                }

                @Override
                public void fail() {
                    Toast.makeText(TUploadAudioActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public void ButtonOnclick_uploadaudio_broadcast(View view) {
        /*Intent intent = new Intent();// intent可以过滤音频文件
        intent.setType("audio*//*");//获取音频文件
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 24);*/
        //判断是否选中资源
        if (sminfo==null){
            Toast.makeText(this,"您没有选择哪份作业",Toast.LENGTH_SHORT).show();
        }else if (new File(getExternalFilesDir(null).getAbsolutePath()+"/"+sminfo).exists()){
            //文件已经存在，直接播放
            Intent intent1 = new Intent(TUploadAudioActivity.this, AudioPlayerActivity.class);
            intent1.putExtra("videoPath", getExternalFilesDir(null).getAbsolutePath()+"/"+sminfo);
            startActivity(intent1);
        }else {
            //文件没有下载，弹出对话框，判断是否要下载
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("是否要下载该资源并播放")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //下载资源并播放
                            String urlStr = HttpUtil.server + "/mediaShared/" + sminfo;
                            HttpDownloader.download(TUploadAudioActivity.this, urlStr, null, new HttpDownloader.DownloadListener() {
                                @Override
                                public void done() {
                                    //修改UI
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDownload.setText("已下载");
                                            mDownload.setTextColor(Color.GRAY);
                                            mDownload.setEnabled(false);
                                        }
                                    });
                                    //播放
                                    Intent intent1 = new Intent(TUploadAudioActivity.this, AudioPlayerActivity.class);
                                    intent1.putExtra("videoPath", getExternalFilesDir(null).getAbsolutePath()+"/"+sminfo);
                                    startActivity(intent1);
                                }

                                @Override
                                public void fail() {
                                    Toast.makeText(TUploadAudioActivity.this,"出现了点问题",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create() ;
            dialog.show();
        }
    }


    public void ButtonOnclick_Luzhishipin(View view) {
        Intent intent = new Intent(TUploadAudioActivity.this, AudioUploadOperationActivity.class);// intent可以过滤音频文件

        startActivity(intent);
    }


    public void ButtonOnclick_uploadaudio__back(View view) {
        finish();
    }

    public void ButtonOnclick_recordUpload(View view) {
        final TextView textView = new TextView(this);
        textView.setText("录音并上传");
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Dialog dialog = builder.setTitle("录音")
                .setView(textView)
                .setPositiveButton("开始录音", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setMShowing(dialog, false);
                        textView.setText("正在录音...");
                        SharedPreferences sharedata = getSharedPreferences("data", 0);
                        //Toast.makeText(UploadAudioActivity.this, ""+sharedata.getString("userID",null), 1000);
                        String temp = sharedata.getString("userID", null);
                        Log.v("2121", temp);
                        id = Integer.parseInt(sharedata.getString("userID", null));
                        File file = new File(getExternalFilesDir(null).getAbsolutePath() + "/id" + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".mp3");

                        Toast.makeText(getApplicationContext(), "正在录音，录音文件在" + file.getAbsolutePath(), Toast.LENGTH_LONG)
                                .show();
                        setTitle("正在录音");
                        soundPath = file.getAbsolutePath();
                        mr = new MediaRecorder();

                        // 从麦克风源进行录音
                        mr.setAudioSource(MediaRecorder.AudioSource.DEFAULT);

                        // 设置输出格式
                        mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

                        // 设置编码格式
                        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                        // 设置输出文件
                        mr.setOutputFile(file.getAbsolutePath());

                        try {
                            // 创建文件
                            file.createNewFile();
                            // 准备录制
                            mr.prepare();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 开始录制
                        mr.start();


                    }
                }).setNegativeButton("结束录音", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // TODO Auto-generated method stub
                        if (mr != null) {
                            mr.stop();
                            mr.release();
                            mr = null;
                            //显示对话框，判断是否要上传
                            showUploadDialog();
                        }
                    }
                }).create();

        dialog.show();
    }

    private void showUploadDialog() {

        final EditText editText = new EditText(this);
        editText.setHint("请输入文件名");
        Dialog builder = new AlertDialog.Builder(this).setView(editText).setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //判断文件名是否为空
                if (editText.getText().toString() == null || editText.getText().toString().equals("")) {
                    //文件名为空
                    Toast.makeText(TUploadAudioActivity.this, "请输入文件名", Toast.LENGTH_SHORT).show();
                    setMShowing(dialog, false);
                } else {
                    //修改文件名
                    //判断文件是否存在
                    File newFile = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + editText.getText().toString() + ".mp3");
                    if (newFile.exists()) {
                        //文件已经存在
                        Toast.makeText(TUploadAudioActivity.this, "文件名已经存在", Toast.LENGTH_SHORT).show();
                        setMShowing(dialog,false);
                    } else {
                        File file = new File(soundPath);

                        file.renameTo(newFile);
                        file.delete();
                        SharedPreferences sharedata = getSharedPreferences("data", 0);
                        id = Integer.parseInt(sharedata.getString("userID", null));
                        Toast.makeText(getApplicationContext(), "录音完毕,请等待上传...", Toast.LENGTH_LONG).show();
                        uploadFile(newFile);
                        setMShowing(dialog, true);
                    }

                }

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除文件
                File file = new File(soundPath);
                if (file.exists()) {
                    file.delete();
                }
                setMShowing(dialog, true);
            }
        }).create();
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 22:
                if (data != null) {

                    Uri uri = data.getData();
                    soundPath = uri.toString();
                    Log.e("获得音频文件！", "uri = " + uri);
                    try {
                        //String[] pojo = {MediaStore.Audio.Media.DATA};
                        //Cursor cursor = managedQuery(uri, pojo, null, null, null);
                        if (uri != null) {
                            String path = FileUtil.getRealFilePath(TUploadAudioActivity.this, uri);
                            if (path.endsWith(".mp3") || path.endsWith(".mp4") || path.endsWith("amr")) {
                                soundPath = path;
                                SharedPreferences sharedata = getSharedPreferences("courseMis", 0);
                                id = Integer.parseInt(sharedata.getString("userID", null));
                                File file = new File(path);
                                uploadFile(file);


                            } else {
                                Log.e("错误", "1");
                                alert();
                            }
                        } else {
                            Log.e("错误", "2");
                            alert();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(TUploadAudioActivity.this, "您没有进行任何操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case 24:
                try {
                    Uri uri = data.getData();
                    String[] pojo = {MediaStore.Audio.Media.DATA};
                    Cursor cursor = managedQuery(uri, pojo, null, null, null);
                    if (cursor != null) {
                        int colunm_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(colunm_index);
                        Intent intent1 = new Intent(TUploadAudioActivity.this, AudioPlayerActivity.class);
                        intent1.putExtra("videoPath", path);
                        startActivity(intent1);
                    }
                } catch (Exception e) {
                    Toast.makeText(TUploadAudioActivity.this, "资源没有选中...", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }// onActivityResult结束

    public void ButtonOnclick_uploadaudio__upload(View view) {
        Intent intent = new Intent();// intent可以过滤音频文件
        intent.setType("audio/*");//获取音频文件
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 22);
    }

    public void uploadFile(File soundFile) {
        Log.i(TAG, "upload start");
        try {
            String requestUrl = HttpUtil.server_ShareMediaData;
            //请求普通信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", id + "");
            //上传文件,第三个参数是struts2接收的参数
            FormFile formfile = new FormFile(soundFile.getName(), soundFile, "video", "audio/mpeg");
            UploadThread uploadThread = new UploadThread(requestUrl, params, formfile);

            Thread t1 = new Thread(uploadThread);
            t1.start();


            Log.i(TAG, "upload success");
            Toast.makeText(TUploadAudioActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.i(TAG, "upload error");
            e.printStackTrace();
        }
        Log.i(TAG, "upload end");
    }


    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的音频文件!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        soundPath = null;
                    }
                }).create();
        dialog.show();
    }

    /**
     * 设置AlertDialog是否dismiss
     */
    private void setMShowing(DialogInterface dialog, boolean mShowing) {
        try {
            Field field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, mShowing);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
