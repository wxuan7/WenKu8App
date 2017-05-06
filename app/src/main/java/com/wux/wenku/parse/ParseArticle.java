package com.wux.wenku.parse;

/**
 * 解析正文
 * Created by WuX on 2017/4/18.
 */

import android.util.Log;

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Chapters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 如果html的标签的class包含空格，例如：<li class="archive-item clearfix"></>需要连续调用select  select("li.archive-item").select("li.clearfix");
 */
public class ParseArticle extends ParseHTML {
    static int num = 10;

    public static String parseChapteresList(String href) {
        setCookies();//设置cookie
        ArrayList<Chapters> list = new ArrayList<Chapters>();
        try {
            Document doc = Jsoup.connect(href).cookies(AppConfig._Cookie).timeout(10000).get();
            Element masthead = doc.select("div#content").first();
            String content = masthead.text();
            return content;
//            int index = 0;
//            for (int i = 0; i < masthead.size(); i++) {
//                Element cElement = masthead.get(i);
//                Elements cElements = cElement.select("td");
//                for (int j = 0; j < cElements.size(); j++) {
//                    Chapters chapters = new Chapters();
//                    Element chaptereElement = cElements.get(j).select("a").first();
//                    if (chaptereElement == null) {
//                        chaptereElement = cElements.get(j);
//                    }
//                    if (!"&nbsp;".equals(chaptereElement.text())) {
//                        chapters.setChapterName(chaptereElement.text());
//                    }
//                    chapters.setIndex(index);
//                    index++;
//                    chapters.setUrl(baseUrl + chaptereElement.attr("href"));
//                    if (null != chapters.getChapterName() & null != chapters.getUrl()) {
//                        if (!"".equals(chapters.getChapterName().trim()) & !"baseUrl".equals(chapters.getUrl().trim())) {
//                            list.add(chapters);
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("解析章节目录", msg);
            e.printStackTrace();
        }
        return "";
    }

}
