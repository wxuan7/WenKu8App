package com.wux.wenku.parse;

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.util.JsoupUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WuX on 2017/4/28.
 */

public class ParseHTML {


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
