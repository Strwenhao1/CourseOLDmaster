package com.coursemis.view.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.coursemis.R;

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

public class SelectItemView extends FrameLayout {

    private final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private Context mContext ;
    private AttributeSet mAttributeSet ;
    private int mDefStyleAttr ;
    private TextView mSignInClass;
    private TextView mSignInResult;

    public SelectItemView(@NonNull Context context) {
        this(context,null) ;
    }

    public SelectItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParameter(context, attrs, defStyleAttr) ;
        initView() ;
        initData() ;
    }

    private void initData() {
        TypedArray s = mContext.obtainStyledAttributes(mDefStyleAttr, R.styleable.SelectItemView);
        /*String signInClassName = s.getString(R.styleable.SignInItemView_signInClassName) ;*/
        String signInClassName = mAttributeSet.getAttributeValue(NAMESPACE,"signInClassName") ;
        String signInResultName = mAttributeSet.getAttributeValue(NAMESPACE,"signInResultName") ;
        int signInClassColor = mAttributeSet.getAttributeIntValue(NAMESPACE,"signInClassColor",Color.BLACK) ;
        int signInResultColor = mAttributeSet.getAttributeIntValue(NAMESPACE,"signInResultColor",Color.GRAY) ;
        mSignInResult.setTextColor(signInResultColor);
        mSignInClass.setTextColor(signInClassColor);
        Log.e("测试数据",signInClassName+"..."+signInResultName) ;
        if (signInClassName!=null&&!signInClassName.equals("")
                &&signInResultName!=null&&!signInResultName.equals("")){
            mSignInClass.setText(signInClassName) ;
            mSignInResult.setText(signInResultName);
        }else if (signInResultName!=null&&!signInResultName.equals("")){
            mSignInResult.setText(signInResultName);
        }else if (signInClassName!=null&&!signInClassName.equals("")){
            mSignInClass.setText(signInClassName);
        }
    }

    private void initParameter(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context ;
        mAttributeSet = attrs ;
        mDefStyleAttr = defStyleAttr ;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_select_item, this, true);
        mSignInClass = (TextView) findViewById(R.id.signin_class);
        mSignInResult = (TextView) findViewById(R.id.signin_result);
    }

    public void setSignInClassName(@Nullable String signInClassName){
        if (signInClassName==null||signInClassName.equals("")){
            mSignInClass.setText("");
        }else if (signInClassName!=null&&!signInClassName.equals("")){
            mSignInClass.setText(signInClassName);
        }
    }
    public void setSignInResultName(@Nullable String signInResultName){
        if (signInResultName==null||signInResultName.equals("")){
            mSignInResult.setText("");
        }else if (signInResultName!=null&&!signInResultName.equals("")){
            mSignInResult.setText(signInResultName);
        }
    }
    public void setSignInClassColor(int color){
        mSignInClass.setTextColor(color);
    }
    public void setSignInResultColor(int color){
        mSignInResult.setTextColor(color);
    }
}
