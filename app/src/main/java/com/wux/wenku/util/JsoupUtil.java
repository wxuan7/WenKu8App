package com.wux.wenku.util;

import android.util.Log;

import com.wux.wenku.app.AppConfig;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WuX on 2017/5/11.
 */

public class JsoupUtil {

    private static final Object _LockJsoup = new Object();
    public static JsoupUtil helper;

    public JsoupUtil() {

    }

    public static JsoupUtil getInstance() {
        if (helper == null) {
            helper = new JsoupUtil();
        }
        return helper;
    }

    public void setCookies() throws Exception {
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
                e1.printStackTrace();
                throw e1;
            }
            AppConfig._Cookie = resp.cookies();
        }
    }

    /**
     * 解析url获取Document对象
     *
     * @param url
     * @return
     */
    public Document getDocument(String url) throws Exception {
        setCookies();
        Document document = null;
        synchronized (JsoupUtil.class) {
            Thread.sleep(200);
            try {
                document = Jsoup.connect(url).cookies(AppConfig._Cookie).timeout(10000).get();
            } catch (IOException e) {
                Log.e("链接", url);
                Log.e("解析链接异常", e.getMessage());
                e.printStackTrace();
                throw new Exception("链接断开了，稍后再试吧 (。・`ω´・)");
            }
        }
        return document;
    }

    /**
     * 直接解析html为doc对象
     *
     * @param html
     * @return
     * @throws Exception
     */
    public Document parseDocument(String html) throws Exception {
        Document document = null;
        synchronized (JsoupUtil.class) {
            try {
                document = Jsoup.parse(html);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return document;
    }

    public Document getDocument(String url, String code) throws Exception {
        setCookies();
        Document document = null;
        synchronized (JsoupUtil.class) {
            try {
                document = Jsoup.parse(getSearch(url), code);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return document;
    }

    private String readHtml(String myurl) {
        StringBuffer sb = new StringBuffer("");
        URL url;
        try {
            url = new URL(myurl);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "gbk"));
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String readHtmlWithCookies(String href) {
        StringBuffer sb = new StringBuffer("");
        try {
            setCookies();
            ;
            URL url = new URL(href);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
//            connection.addRequestProperty("Content-Type","application");
            if (AppConfig._Cookie != null) {
                for (Map.Entry<String, String> entry : AppConfig._Cookie.entrySet()) {
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gbk"));
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getSearch(String href) {
        String doc = "";
        HttpURLConnection connection = null;
        try {
            /**
             * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using
             * java.net.URL and //java.net.URLConnection
             */
            URL url = new URL(AppConfig._LoginURL);
            connection = (HttpURLConnection) url.openConnection();

/**
 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
 */
            connection.setDoOutput(true);
/**
 * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...
 */
            OutputStreamWriter out = new OutputStreamWriter(connection
                    .getOutputStream(), "GBK");
            //其中的memberName和password也是阅读html代码得知的，即为表单中对应的参数名称
            out.write("username=kuien&password=kuien&usecookie=0&action=login"); // post的关键所在！
// remember to clean up
            out.flush();
            out.close();
// 取得cookie，相当于记录了身份，供下次访问时使用
            connection.connect();
            String cookieVal = connection.getHeaderField("Set-Cookie");
            url = new URL(href);
            HttpURLConnection resumeConnection = (HttpURLConnection) url
                    .openConnection();
            if (cookieVal != null) {
                //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
                resumeConnection.setRequestProperty("Cookie", cookieVal);
            }
            resumeConnection.connect();
            InputStream urlStream = resumeConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlStream, "gbk"));
            String ss = null;
            String total = "";
            while ((ss = bufferedReader.readLine()) != null) {
                total += ss;
            }
            doc = total;
//            IOUtils.write(total, new FileOutputStream("d:/index.html"));
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return doc;
    }
}