package com.coursemis.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coursemis.R;
import com.coursemis.view.activity.ImageActivity;

import java.util.List;

import static android.R.attr.width;

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

public class HomeworkAdapter extends RecyclerView.Adapter{
    private Context mContext ;
    private List<Uri> mUris ;

    public HomeworkAdapter(Context context, List<Uri> uris) {
        mContext = context ;
        mUris = uris ;
    }
    public HomeworkAdapter(Context context) {
        mContext = context ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_homework_list, null);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Glide.with(mContext)
                .load(mUris.get(position))
                .into(myViewHolder.homework) ;
        Activity activity = (Activity) mContext;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams params = myViewHolder.homework.getLayoutParams();
        //设置图片的相对于屏幕的宽高比
        params.width = width /2;
        params.height =  height/3 ;
        myViewHolder.homework.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        if (mUris==null){
            return 0 ;
        }else {
            return mUris.size();
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        private View itemView ;
        private ImageView homework ;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView ;
            homework = (ImageView) itemView.findViewById(R.id.iv_homework);
        }
    }
    public void refreshData(List<Uri> uris){
        mUris = uris ;
        notifyDataSetChanged();
    }
}