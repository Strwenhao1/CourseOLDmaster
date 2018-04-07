package com.coursemis.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.coursemis.thread.*;
import com.coursemis.thread.FormFile;
import com.coursemis.view.activity.ImageActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Created by zhxchao on 2018/3/11.
 */

public class UploadImageUtil {
    public static void uploadImage(Context context, List<Uri> uris, String cid, String tid, String homeworkName, int homeworkScore) {
        try {
            String requestUrl = HttpUtil.server_teacher_tupLoadHomework;
            Map<String, String> params = new HashMap<String, String>();
            params.put("cid", cid);
            params.put("tid", tid);
            params.put("score",homeworkScore+"") ;
            params.put("name",homeworkName) ;
            params.put("age", "23");
            for (Uri uri : uris) {
                String realFilePath = FileUtil.getRealFilePath(context, uri);
                Log.e("测试获取到的文件地址",(realFilePath==null)+"") ;
                File file = new File(realFilePath);
                String name = file.getName();
                FormFile formFile = new FormFile(name, file, "image", "application/octet-stream");
                UploadThread uploadThread = new UploadThread(requestUrl,params,formFile);
                Thread t1 = new Thread(uploadThread);
                t1.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
