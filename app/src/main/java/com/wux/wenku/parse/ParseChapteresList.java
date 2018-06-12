package com.wux.wenku.parse;

/**
 * Created by WuX on 2017/4/18.
 */

import android.util.Log;

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Chapters;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 如果html的标签的class包含空格，例如：<li class="archive-item clearfix"></>需要连续调用select  select("li.archive-item").select("li.clearfix");
 */
public class ParseChapteresList extends ParseHTML {
    static int num = 10;

    public static ArrayList<Chapters> parseChapteresList(String href) throws Exception {
//        setCookies();//设置cookie
        ArrayList<Chapters> list = new ArrayList<Chapters>();
        try {
//            String chapteresUrl = getChapteres(href);
            String baseUrl = href.substring(0, href.lastIndexOf("/")) + "/";
            Log.e("章节目录链接：", href);
            Document doc = AppConfig.mJsoupUtil.getDocument(href);//Jsoup.connect(href).cookies(AppConfig._Cookie).timeout(10000).get();
            Log.e("章节目录元素：", doc.text());
            Elements masthead = doc.select("table.css tbody tr");
//            Elements NovelListElements = masthead.select("td div");
            int index = 0;
            for (int i = 0; i < masthead.size(); i++) {
                Element cElement = masthead.get(i);
                Elements cElements = cElement.select("td");
                for (int j = 0; j < cElements.size(); j++) {
                    Chapters chapters = new Chapters();
                    Element chaptereElement = cElements.get(j).select("a").first();
                    if (chaptereElement == null) {
                        chaptereElement = cElements.get(j);
                    }
                    // 去除空格占位部分
                    if (!" ".equals(chaptereElement.text())) {
                        chapters.setChapterName(chaptereElement.text());
                    }
                    chapters.setIndex(index);
                    index++;
                    String url = chaptereElement.attr("href");
                    if (null != url && url.length() > 0) {
                        chapters.setUrl(baseUrl + url);
                    }
                    if (null != chapters.getChapterName() && !"".equals(chapters.getChapterName().trim())) {
                        list.add(chapters);
                    }
                }
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("解析章节目录", msg);
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    private static String getChapteres(String url) throws Exception {
        String c_url = "";
        try {
            Document doc = AppConfig.mJsoupUtil.getDocument(url);// Jsoup.connect(url).cookies(AppConfig._Cookie).timeout(10000).get();
            Element masthead = doc.select("div div div div div div span fieldset div a").first();
            c_url = masthead.attr("href");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return c_url;
    }
}
