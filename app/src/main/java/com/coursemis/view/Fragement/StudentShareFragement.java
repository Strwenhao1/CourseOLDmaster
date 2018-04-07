package com.coursemis.view.Fragement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.coursemis.R;
import com.coursemis.model.Media;
import com.coursemis.util.HttpDownloader;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.coursemis.view.activity.AudioPlayerActivity;
import com.coursemis.view.activity.StudentManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;


public class StudentShareFragement extends Fragment
        implements View.OnClickListener{
    protected View mRootView;
    private ArrayList<String> mResourceList = new ArrayList<String>();
    private ListView mListView;
    private static final String TAG = "StudentShareFragement";
    private String sminfo = null;
    private Button mDownload;
    private ArrayAdapter<String> mRadioButtonAdapter;
    private Button mBroadcast;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_upload_audio, null);
        initView();
        initData();
        return mRootView;

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
                          mRadioButtonAdapter.notifyDataSetInvalidated();
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
        mBroadcast.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadaudio__down:
                ButtonOnclick_uploadaudio__down(v);
                break;
            case R.id.uploadaudio__broadcast:
                ButtonOnclick_uploadaudio_broadcast(v);
                break;
        }
    }

    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.uploadaudio_listview);
        mDownload = (Button) mRootView.findViewById(R.id.uploadaudio__down);
        mBroadcast = (Button) mRootView.findViewById(R.id.uploadaudio__broadcast);
        mSwipeRefreshLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.refresh);
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
            intent1.putExtra("videoPath", media);
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(),"正在刷新，请稍后",Toast.LENGTH_SHORT).show();
                StudentManager student = (StudentManager) getActivity();
                student.share();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                    }
                },6000);
            }
        });

    }
}
