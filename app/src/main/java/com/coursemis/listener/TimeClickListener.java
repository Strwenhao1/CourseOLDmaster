package com.coursemis.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.coursemis.view.activity.BarChartActivity;
import com.coursemis.view.activity.TChartActivity;
import com.coursemis.view.myView.TitleView;

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
 * Created by zhxchao on 2018/2/28.
 */

public class TimeClickListener implements TitleView.OnTitleClickListener {
    private int [] mYear ;
    private Context mContext ;
    public TimeClickListener(Context context ,int [] year) {
        mYear = year ;
        mContext = context ;
    }
    @Override
    public void onClick(View button) {
        //弹出对话框
        final String [] item = new String[mYear.length];
        for (int i = 0;i<mYear.length;i++){
            item[i] = mYear[i]+"" ;
        }
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("选择时间")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //更改标题
                        TChartActivity chartActivity = (TChartActivity) mContext;
                        chartActivity.mTitleView.setTitle(item[which]+"");
                        chartActivity.mShowCount = which ;
                        chartActivity.updateBar();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
