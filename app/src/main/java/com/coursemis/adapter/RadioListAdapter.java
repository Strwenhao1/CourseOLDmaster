package com.coursemis.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursemis.R;

import java.io.File;
import java.util.List;

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
 * Created by zhxchao on 2018/3/10.
 */

public class RadioListAdapter extends RecyclerView.Adapter{
    private Context mContext ;
    private List<String> mMediainfolist ;
    public RadioListAdapter(Context context, List<String> mediainfolist) {
        mContext = context ;
        mMediainfolist = mediainfolist ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_radia_list, null) ;
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String radioName = mMediainfolist.get(position) ;
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.radioName.setText(radioName);
        //判断当前文件是否存在
        File file = new File(mContext.getExternalFilesDir(null).getAbsolutePath()+"/"+radioName) ;
        if (file.exists()){
            //文件存在
            viewHolder.download.setText("已下载");
        }else {
            viewHolder.download.setText("未下载");
        }
    }

    @Override
    public int getItemCount() {
        return mMediainfolist.size();
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView radioName;
        public View itemView ;
        public TextView download ;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView ;
            radioName = (TextView) itemView.findViewById(R.id.radio_name);
            download = (TextView) itemView.findViewById(R.id.download);
        }
    }
}
