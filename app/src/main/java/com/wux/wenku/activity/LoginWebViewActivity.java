package com.wux.wenku.activity;

import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wux.wenku.R;

import java.util.HashMap;
import java.util.Map;

public class LoginWebViewActivity extends AppCompatActivity {
    private WebView web_login;
    private SimpleDraweeView sdvCollapsedTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_web);
        initView();
        web_login = (WebView) findViewById(R.id.web_login);
        String url = "http://www.wenku8.com/login.php?do=submit";
//        String url = "http://www.wenku8.com/index.php";
//        String url ="http://www.dilidili.wang/watch3/55596/";
        Map<String,String> data = new HashMap<String ,String>();
        data.put("username","kuien");
        data.put("password","kuien");
        data.put("usecookie","0");
        data.put("action","login");
        web_login.loadUrl(url,data);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_get_cookied:

                break;
            case R.id.action_refulsh:
                web_login.reload();
                break;
        }
        return super.onOptionsItemSelected(item);
    } private void initView() {
        sdvCollapsedTop = (SimpleDraweeView) findViewById(R.id.sdvCollapsedTop);
        sdvCollapsedTop.setImageURI(Uri.parse("http://img0.imgtn.bdimg.com/it/u=4136016998,4074341228&fm=21&gp=0.jpg"));
    }
}
