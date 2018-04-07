package com.coursemis.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Media;
import com.coursemis.thread.FormFile;
import com.coursemis.thread.UploadThread;
import com.coursemis.util.FileUtil;
import com.coursemis.util.HttpDownloader;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.view.activity.AudioPlayerActivity;
import com.coursemis.view.activity.AudioUploadOperationActivity;
import com.coursemis.view.activity.TUploadAudioActivity;
import com.coursemis.view.activity.WelcomeActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * _oo0oo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * 0\  =  /0
 * ___/`---'\___
 * .' \\|     |// '.
 * / \\|||  :  |||// \
 * / _||||| -:- |||||- \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |_/ |
 * \  .-\__  '-'  ___/-. /
 * ___'. .'  /--.--\  `. .'___
 * ."" '<  `.___\_<|>_/___.' >' "".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `_.   \_ __\ /__ _/   .-` /  /
 * =====`-.____`.___ \_____/___.-`___.-'=====
 * `=---='
 * <p>
 * <p>
 * 资源共享的界面
 * Created by zhxchao on 2018/3/18.
 */

public class FileResourceFragment extends BaseFragment
        implements View.OnClickListener {

    private ArrayList<String> mResourceList = new ArrayList<String>();
    private ListView mListView;
    private static final String TAG = "FileResourceFragment";
    int id = 0;
    private String soundPath = null;
    private String sminfo = null;
    List<String> mediainfolist = null;
    private MediaRecorder mr;
    private Button mDownload;
    private ArrayAdapter<String> mRadioButtonAdapter;
    private Button mUploadAudio;
    private Button mBroadcast;
    private Button mUploadRecord;
    private Button mVideo;

    private static final int RECORD_VIDEO = 1;
    private static final int PLAY_VIDEO = 2;
    private String path = null;
    private ImageView iv = null;

    //缩略图
    private Bitmap bitmap = null;

    @Override
    public void refresh(Course course) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_file_resource, null);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(HttpUtil.server_getMedia, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        JSONArray object = arg1.optJSONArray("result");

                        if (object.length() == 0) {
                            Toast.makeText(getActivity(), "当前没有任何资源共享!", Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < arg1.optJSONArray("result").length(); i++) {
                                JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
                                P.p(object_temp.toString() + 2222);
                                mResourceList.add(i, object_temp.optString("smname"));
                            }
                            mRadioButtonAdapter.notifyDataSetChanged();
                        }


                        super.onSuccess(arg0, arg1);
                    }
                });
        mRadioButtonAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_checked, mResourceList);
        mListView.setAdapter(mRadioButtonAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                sminfo = mResourceList.get(arg2);
                //判断本地是否存在文件
                File file = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/" + sminfo);
                if (file.exists()) {
                    //已经存在
                    //修改按钮
                    mDownload.setText("已下载");
                    mDownload.setTextColor(Color.GRAY);
                    mDownload.setEnabled(false);
                } else {
                    //已经存在
                    //修改按钮
                    mDownload.setText("下载");
                    mDownload.setTextColor(Color.BLACK);
                    mDownload.setEnabled(true);
                }
            }

        });
        mDownload.setOnClickListener(this);
        mUploadAudio.setOnClickListener(this);
        mBroadcast.setOnClickListener(this);
        mUploadRecord.setOnClickListener(this);
        mVideo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadaudio__down:
                ButtonOnclick_uploadaudio__down(v);
                break;
            case R.id.uploadaudio__upload:
                ButtonOnclick_uploadaudio__upload(v);
                break;
            case R.id.uploadaudio__broadcast:
                ButtonOnclick_uploadaudio_broadcast(v);
                break;
            case R.id.recordUpload:
                ButtonOnclick_recordUpload(v);
                break;
            case R.id.Luzhishipin:
                ButtonOnclick_Luzhishipin(v);
                break;
        }
    }

    private void initView() {
        mListView = (ListView) mView.findViewById(R.id.uploadaudio_listview);
        mDownload = (Button) mView.findViewById(R.id.uploadaudio__down);
        mUploadAudio = (Button) mView.findViewById(R.id.uploadaudio__upload);
        mBroadcast = (Button) mView.findViewById(R.id.uploadaudio__broadcast);
        mUploadRecord = (Button) mView.findViewById(R.id.recordUpload);
        mVideo = (Button) mView.findViewById(R.id.Luzhishipin);
    }

    public void ButtonOnclick_uploadaudio__down(View view) {

        if (sminfo == null) {
            Toast.makeText(getActivity(), "您没有选择哪份作业", Toast.LENGTH_SHORT).show();
        } else {
            String url = HttpUtil.server + "/mediaShared/" + sminfo;
            HttpDownloader.download(getActivity(), url, null, new HttpDownloader.DownloadListener() {
                @Override
                public void done() {
                    Toast.makeText(getActivity(), "下载成功", Toast.LENGTH_SHORT).show();
                    getActivity().runOnUiThread(new Runnable() {
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
                    Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 22:
                if (data != null) {

                    Uri uri = data.getData();
                    soundPath = uri.toString();
                    Log.e("获得音频文件！", "uri = " + uri);
                    try {
                        if (uri != null) {
                            String path = FileUtil.getRealFilePath(getActivity(), uri);
                            if (path.endsWith(".mp3") || path.endsWith(".mp4") || path.endsWith("amr")) {
                                soundPath = path;
                                SharedPreferences sharedata = getActivity().getSharedPreferences("courseMis", 0);
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
                    Toast.makeText(getActivity(), "您没有进行任何操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case 24:
                try {
                    Uri uri = data.getData();
                    String[] pojo = {MediaStore.Audio.Media.DATA};
                    Cursor cursor = getActivity().managedQuery(uri, pojo, null, null, null);
                    if (cursor != null) {
                        int colunm_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        cursor.moveToFirst();
                        Media media = new Media();
                        media.setPath(cursor.getString(colunm_index));
//                        String path = cursor.getString(colunm_index);
                        Intent intent1 = new Intent(getActivity(), AudioPlayerActivity.class);
                        intent1.putExtra("videoPath", media);
                        startActivity(intent1);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "资源没有选中...", Toast.LENGTH_SHORT).show();
                }

                break;
            case RECORD_VIDEO:
                // 录制视频完成
                try {
                    AssetFileDescriptor videoAsset = getActivity().getContentResolver()
                            .openAssetFileDescriptor(getActivity().getIntent().getData(), "r");
                    FileInputStream fis = videoAsset.createInputStream();
                    SharedPreferences sharedata = getActivity().getSharedPreferences("courseMis", 0);
                    id = Integer.parseInt(sharedata.getString("userID", null));
                    File tmpFile = new File(
                            "/sdcard/CourseMisRecord/" + id + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) +
                                    ".mp4");
                    if (!tmpFile.exists()) {
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
                    deleteDefaultFile(getActivity().getIntent().getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PLAY_VIDEO:
                // 播放视频完成
                Log.d("play", "--over---");
                break;
        }
    }

    public void ButtonOnclick_uploadaudio_broadcast(View view) {
        /*Intent intent = new Intent();// intent可以过滤音频文件
        intent.setType("audio*//*");//获取音频文件
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 24);*/
        //判断是否选中资源
        if (sminfo == null) {
            Toast.makeText(getActivity(), "您没有选择哪份作业", Toast.LENGTH_SHORT).show();
        } else if (new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/" + sminfo).exists()) {
            //文件已经存在，直接播放
            Intent intent1 = new Intent(getActivity(), AudioPlayerActivity.class);
            Media media = new Media();
            media.setPath(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/" + sminfo);
            intent1.putExtra("videoPath",media );
            startActivity(intent1);
        } else {
            //文件没有下载，弹出对话框，判断是否要下载
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("是否要下载该资源并播放")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //下载资源并播放
                            String urlStr = HttpUtil.server + "/mediaShared/" + sminfo;
                            HttpDownloader.download(getActivity(), urlStr, null, new HttpDownloader.DownloadListener() {
                                @Override
                                public void done() {
                                    //修改UI
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDownload.setText("已下载");
                                            mDownload.setTextColor(Color.GRAY);
                                            mDownload.setEnabled(false);
                                        }
                                    });
                                    //播放
                                    Intent intent1 = new Intent(getActivity(), AudioPlayerActivity.class);
                                    Media media = new Media();
                                    media.setPath(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/" + sminfo);
                                    intent1.putExtra("videoPath", media);
                                    startActivity(intent1);
                                }

                                @Override
                                public void fail() {
                                    Toast.makeText(getActivity(), "出现了点问题", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            dialog.show();
        }
    }


    public void ButtonOnclick_Luzhishipin(View view) {
        String[] items = new String[]{"开始录制", "上传视频"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                items);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                        switch (which) {
                            case 0:
                                startrecord();
                                break;
                            case 1:
                                ButtonOnclick_audiouploadoperationactivity_upload();
                                break;

                        }

                    }
                }).create();
        dialog.show();
    }


    public void ButtonOnclick_recordUpload(View view) {
        final TextView textView = new TextView(getActivity());
        textView.setText("录音并上传");
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog dialog = builder.setTitle("录音")
                .setView(textView)
                .setPositiveButton("开始录音", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setMShowing(dialog, false);
                        textView.setText("正在录音...");
                        SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);
                        //Toast.makeText(UploadAudioActivity.this, ""+sharedata.getString("userID",null), 1000);
                        String temp = sharedata.getString("userID", null);
                        Log.v("2121", temp);
                        id = Integer.parseInt(sharedata.getString("userID", null));
                        File file = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/id" + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".mp3");
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

        final EditText editText = new EditText(getActivity());
        editText.setHint("请输入文件名");
        Dialog builder = new AlertDialog.Builder(getActivity()).setView(editText).setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //判断文件名是否为空
                if (editText.getText().toString() == null || editText.getText().toString().equals("")) {
                    //文件名为空
                    Toast.makeText(getActivity(), "请输入文件名", Toast.LENGTH_SHORT).show();
                    setMShowing(dialog, false);
                } else {
                    //修改文件名
                    //判断文件是否存在
                    File newFile = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/" + editText.getText().toString() + ".mp3");
                    if (newFile.exists()) {
                        //文件已经存在
                        Toast.makeText(getActivity(), "文件名已经存在", Toast.LENGTH_SHORT).show();
                        setMShowing(dialog, false);
                    } else {
                        File file = new File(soundPath);

                        file.renameTo(newFile);
                        file.delete();
                        SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);
                        id = Integer.parseInt(sharedata.getString("userID", null));
                        Toast.makeText(getActivity(), "录音完毕,请等待上传...", Toast.LENGTH_LONG).show();
                        uploadVideoFile(newFile);
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

    public void ButtonOnclick_uploadaudio__upload(View view) {
        Intent intent = new Intent();// intent可以过滤音频文件
        intent.setType("audio/*");//获取音频文件
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 22);
    }

    public void uploadVideoFile(File soundFile) {
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
            Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.i(TAG, "upload error");
            e.printStackTrace();
        }
        Log.i(TAG, "upload end");
    }


    private void alert() {
        Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示")
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

    /**
     * 录制视频
     **/
    public void startrecord() {
        Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);
        startActivityForResult(mIntent, RECORD_VIDEO);
    }

    /**
     * 播放视频
     **/
    public void playvideo(View view) {
        if (path == null) {
            Toast.makeText(getActivity(), "您还没有拍摄视频", Toast.LENGTH_SHORT).show();
        } else

        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path),
                    "video/mp4");
            startActivityForResult(intent, PLAY_VIDEO);
        }
    }


    public void ButtonOnclick_audiouploadoperationactivity_upload() {
        if (path == null) {
            Toast.makeText(getActivity(), "您还没有拍摄视频", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "上传中请等待...", Toast.LENGTH_SHORT).show();
            File file = new File(path);
            uploadFile(file);
        }
    }

    /*@Override
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

    }*/
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
            Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
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
                Cursor cursor = getActivity().getContentResolver().query(uri, null,
                        null, null, null);
                if (cursor.moveToNext()) {
                    int columnIndex = cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    fileName = cursor.getString(columnIndex);
                    //获取缩略图id
                    int id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Video.VideoColumns._ID));
                    //获取缩略图
                    bitmap = MediaStore.Video.Thumbnails.getThumbnail(
                            getActivity().getContentResolver(), id, MediaStore.Video.Thumbnails.MICRO_KIND,
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
