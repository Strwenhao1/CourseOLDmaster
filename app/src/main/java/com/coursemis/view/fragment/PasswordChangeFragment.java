package com.coursemis.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.activity.PasswordChangeActivity;
import com.coursemis.view.activity.StudentMainActivity;
import com.coursemis.view.activity.WelcomeActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

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
 * Created by zhxchao on 2018/3/24.
 */

public class PasswordChangeFragment extends BaseFragment {

    public Context context;///
    private EditText password_old;
    private EditText password_new;
    private EditText password_conform;

    //private int teacherid;
    private int studentid;
    private String type = "教师";

    private AsyncHttpClient client;
    private Button mCommit;

    @Override
    public void refresh(Course course) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, null);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        client = new AsyncHttpClient();
        mCommit.setOnClickListener
                (new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (validate()) {
                            RequestParams params = new RequestParams();
                            params.put("password_old", password_old.getText().toString().trim());
                            params.put("password_new1", password_new.getText().toString().trim());
                            params.put("password_new2", password_conform.getText().toString().trim());

                            params.put("type", type);//用户类型
                            if (type.equals("教师")) {
                                params.put("teacherid", mTeacher.getTId() + "");//用户类型为教师
                            } else {
                                params.put("studentid", studentid + "");//用户类型为教师
                            }


                            params.put("power", ""); ///
                            params.put("action", "login");///

                            client.post(HttpUtil.server_pwd_change, params,
                                    new JsonHttpResponseHandler() {

                                        @Override
                                        public void onSuccess(int arg0, JSONObject arg1) {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                                            //DialogUtil.showDialog(getActivity(), pwdChange_msg, true);
                                            getActivity().finish();
                                            super.onSuccess(arg0, arg1);
                                        }

                                    });

                        }


                    }

                });
    }

    private void initView() {
        password_old = (EditText) mView.findViewById(R.id.password_old);
        password_new = (EditText) mView.findViewById(R.id.password_new);
        password_conform = (EditText) mView.findViewById(R.id.password_conform);
        mCommit = (Button) mView.findViewById(R.id.commit);
    }

    // 对用户输入的用户名、密码进行校验
    private boolean validate() {
        String old = password_old.getText().toString().trim();
        if (old.equals("")) {
            DialogUtil.showDialog(getActivity(), "用户原始密码是必填项！", false);
            return false;
        }
        String pwd1 = password_new.getText().toString().trim();
        if (pwd1.equals("")) {
            DialogUtil.showDialog(getActivity(), "新密码是必填项！", false);
            return false;
        }
        String pwd2 = password_conform.getText().toString().trim();
        if (pwd2.equals("")) {
            DialogUtil.showDialog(getActivity(), "确认密码是必填项！", false);
            return false;
        }
        if (!pwd2.equals(pwd1)) {
            DialogUtil.showDialog(getActivity(), "确认密码失败，必须与新密码相同！", false);
            return false;
        }
        if (pwd2.equals(old)) {
            DialogUtil.showDialog(getActivity(), "新密码不得与原密码相同！", false);
            return false;
        }
        return true;
    }

}
