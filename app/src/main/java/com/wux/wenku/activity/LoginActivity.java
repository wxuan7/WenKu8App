package com.wux.wenku.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wux.wenku.BaseActivity;
import com.wux.wenku.R;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.ui.RippleView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    private TextInputLayout layoutEtEmail;
    private EditText et_username;

    private TextInputLayout layoutEtPwd;
    private EditText et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_floating_label);

        title = "登录WenKu8";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_login:
                AppConfig._UserName = et_username.getText().toString().trim();
                AppConfig._Pwd = et_pwd.getText().toString().trim();
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url1 = "http://www.wenku8.com/login.php?do=submit";
//                        Map<String, String> data = new HashMap<String, String>();
//                        data.put("username", et_username.getText().toString().trim());
//                        data.put("password", et_pwd.getText().toString().trim());
//                        data.put("usecookie", "0");
//                        data.put("action", "login");
//                        Connection.Response resp = null;
//                        try {
//                            resp = Jsoup.connect(url1).data(data).method(Connection.Method.POST).execute();
//                        } catch (IOException e1) {
//                            // TODO Auto-generated catch block
//                            e1.printStackTrace();
//                        }
//                        AppConfig._Cookie = resp.cookies();
//                        LoginHandler handler = new LoginHandler(LoginActivity.this);
//                        Message msg = new Message();
//                        msg.obj = "登录成功";
//                    }
//                }).start();
                break;
        }
    }
    static class LoginHandler extends Handler {

        WeakReference<LoginActivity> mActivity;

        public LoginHandler(LoginActivity mainActivity) {
            this.mActivity = new WeakReference<LoginActivity>(mainActivity);
        }

        public void sendMessage(int what, Object message) {
            Message msg = new Message();
            msg.what = what;
            msg.obj = message;

            this.sendMessage(msg);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity ma = mActivity.get();
            if (ma != null) {
                switch (msg.what) {
                    case 1: // 登录失败时候的提示
                        Toast.makeText(ma, (String) msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        break;
                }
            }
        }
    }
    private void initView() {
        layoutEtEmail = (TextInputLayout) findViewById(R.id.layoutEtEmail);
        et_username = (EditText) findViewById(R.id.et_username);

        layoutEtPwd = (TextInputLayout) findViewById(R.id.layoutEtPwd);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        RippleView rv_login = (RippleView) findViewById(R.id.rv_login);
        Button bt_login = (Button) findViewById(R.id.bt_login);
        rv_login.setOnClickListener(this);

    }

    private void initData() {
        et_username.setText("kuien");
        et_pwd.setText("kuien");

//        layoutEtPwd.setErrorEnabled(true);
//        layoutEtPwd.setError("密码为数字");
    }

}
