package com.wux.wenku.parse;

/**
 * Created by WuX on 2017/4/18.
 */

import android.util.Log;

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Chapters;
import com.wux.wenku.model.Novels;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 如果html的标签的class包含空格，例如：<li class="archive-item clearfix"></>需要连续调用select  select("li.archive-item").select("li.clearfix");
 */
public class ParseChapteresList extends ParseHTML {
    static int num = 10;

    public static ArrayList<Chapters> parseChapteresList(String href) {
        setCookies();//设置cookie
        ArrayList<Chapters> list = new ArrayList<Chapters>();
        try {
            String chapteresUrl = getChapteres(href);
            String baseUrl = chapteresUrl.substring(0, chapteresUrl.lastIndexOf("/")) + "/";
            Document doc = Jsoup.connect(chapteresUrl).cookies(AppConfig._Cookie).timeout(10000).get();
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
                    if (!"&nbsp;".equals(chaptereElement.text())) {
                        chapters.setChapterName(chaptereElement.text());
                    }
                    chapters.setIndex(index);
                    index++;
                    chapters.setUrl(baseUrl + chaptereElement.attr("href"));
                    if (null != chapters.getChapterName() & null != chapters.getUrl()) {
                        if (!"".equals(chapters.getChapterName().trim()) & !"baseUrl".equals(chapters.getUrl().trim())) {
                            list.add(chapters);
                        }
                    }
                }
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("解析章节目录", msg);
            e.printStackTrace();
        }
        return list;
    }

    private static String getChapteres(String url) {
        String c_url = "";
        try {
            Document doc = Jsoup.connect(url).cookies(AppConfig._Cookie).timeout(10000).get();
            Element masthead = doc.select("div div div div div div span fieldset div a").first();
            c_url = masthead.attr("href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c_url;
    }
}
