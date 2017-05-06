package com.wux.wenku.parse;

import com.wux.wenku.app.AppConfig;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WuX on 2017/4/28.
 */

public class ParseHTML {

    /**
     * 设置cookies
     */
    public static void setCookies(){
        if (null == AppConfig._Cookie) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("username", AppConfig._UserName);
            data.put("password", AppConfig._Pwd);
            data.put("usecookie", "0");
            data.put("action", "login");
            Connection.Response resp = null;
            try {
                resp = Jsoup.connect(AppConfig._LoginURL).data(data).method(Connection.Method.POST).execute();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            AppConfig._Cookie = resp.cookies();
        }
    }

    public static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0)
            url.append('?');
        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            // 不做URLEncoder处理
            // url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }
        return url.toString().replace("?&", "?");
    }
}
