package com.coursemis.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.coursemis.bak.FileUtilBak;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class HttpDownloader {

    private URL url = null;

    /**
     * 根据URL下载文件,前提是这个文件当中的内容是文本,函数的返回值就是文本当中的内容
     * 1.创建一个URL对象
     * 2.通过URL对象,创建一个HttpURLConnection对象
     * 3.得到InputStream
     * 4.从InputStream当中读取数据
     *
     * @param urlStr:网络文件地址
     * @param path:指定下载到SD卡上的文件目录
     * @return 保存到SD卡的文件路径
     */
    public String download(String urlStr, String path) {

        int start = urlStr.lastIndexOf("/");
        int end = urlStr.length();
        String fileName = urlStr.substring(start, end);//截取文件名，为下载到SD卡上的文件名

        HttpURLConnection urlConn = null;
        try {
            url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();//一定要加上，否则urlConn.getInputStream()报错
            urlConn.setConnectTimeout(6000);
            InputStream inputStream = urlConn.getInputStream();
            FileUtil fileUtils = new FileUtil();
            File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);

            if (resultFile == null) {
                return null;
            }
            return resultFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != urlConn)
                urlConn.disconnect();
        }
        return null;
    }

    /**
     * 处理回掉
     */
    private static class MyHandler extends Handler {

        public static final int DONE = 1;
        public static final int FAIL = 2;
        private DownloadListener mListener;

        public MyHandler(DownloadListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case DONE:
                    mListener.done();
                    break;
                case FAIL:
                    mListener.fail();
                    break;
            }
        }
    }

    /**
     * 异步下载文件
     *
     * @param urlStr   下载文件的uri
     * @param path     存储的地址，为空时代表getExternalFilesDir(null).getAbsolutePath()文件夹
     * @param listener 监听
     */
    public static void download(Context context, String urlStr, String path, final DownloadListener listener) {
        try {
            //截取文件名
            if (path == null) {
                path = context.getExternalFilesDir(null).getAbsolutePath();
            }
            int start = urlStr.lastIndexOf("/");
            int end = urlStr.length();
            String fileName = urlStr.substring(start+1, end);//截取文件名，为下载到SD卡上的文件名
            String finalPath = path;
            String urlNoFileName = urlStr.substring(0, start+1);
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            urlStr = urlNoFileName+encodeFileName ;
            downloadThread(urlStr, fileName, finalPath, listener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void downloadThread(final String urlStr, final String fileName, final String finalPath, final DownloadListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HttpURLConnection urlConn = null;
                MyHandler handler = new MyHandler(listener);
                Message message = new Message();
                try {
                    Log.e("编码类型", EncodingUtil.getEncoding(urlStr));
                    //URL url = new URL(URLEncoder.encode(urlStr, "Unicode"));
                    URL url = new URL(urlStr) ;
                    //String gb2312 = URLEncoder.encode(urlStr, "GB2312");
                    //URL url = new URL(gb2312) ;
                    Log.e("Url地址", fileName);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.connect();//一定要加上，否则urlConn.getInputStream()报错
                    urlConn.setConnectTimeout(6000);
                    InputStream inputStream = urlConn.getInputStream();
                    //Log.e("测试数据是否正确",finalPath+"...."+fileName);
                    //fileName = URLEncoder.encode(fileName,"UTF-8") ;
                    File resultFile = fileWrite(finalPath + "/" + fileName, inputStream);
                    if (resultFile != null) {
                        //下载成功
                        message.arg1 = MyHandler.DONE;
                        handler.handleMessage(message);
                    } else {
                        message.arg1 = MyHandler.FAIL;
                        handler.handleMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message.arg1 = MyHandler.FAIL;
                    handler.handleMessage(message);
                } finally {
                    if (null != urlConn)
                        urlConn.disconnect();
                }
                Looper.loop();
            }
        }).start();
    }

    public interface DownloadListener {
        void done();

        void fail();

    }

    /**
     * 将流的数据写入文件
     */
    private static File fileWrite(String file_name, InputStream is) {
        File file = new File(file_name);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}