package com.wux.wenku.parse;

/**
 * Created by WuX on 2017/4/18.
 */

import android.util.Log;

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Chapters;
import com.wux.wenku.model.Novels;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 如果html的标签的class包含空格，例如：<li class="archive-item clearfix"></>需要连续调用select  select("li.archive-item").select("li.clearfix");
 */
public class ParseNovelDetail extends ParseHTML {

    public static void parseDetail(Novels novels) {
        setCookies();//设置cookie
        ArrayList<Chapters> list = new ArrayList<Chapters>();
        try {
            Document doc = Jsoup.connect(novels.getnDetailsUrl()).cookies(AppConfig._Cookie).timeout(10000).get();
//            Elements masthead = doc.select("div#content div tabel tbody tr td span");
            Elements masthead1 = doc.select("div#content div");
            Elements masthead = masthead1.first().select("table").get(2).select("tbody tr td span");
            Element lastElement;
            Element contentElement;
            if (masthead.size() == 5) {
                lastElement = masthead.get(2);
                contentElement = masthead.get(4);
            } else {
                lastElement = masthead.get(1);
                contentElement = masthead.get(3);
            }
            //  最近更新的章节信息
            novels.setnLastUpdChapter(lastElement.text());// 最新一章节名
            Log.e("解析作品详情-最后一章节", lastElement.text());
            novels.setnLastUpdChapterUrl(lastElement.attr("href"));// 最新章节名称

            novels.setnContent(contentElement.text());
            Log.e("解析作品详情-简介", contentElement.text());


            Element catalogElement = doc.select("div#content div div div span fieldset div a").first();
            catalogElement.attr("href");
//            Elements NovelListElements = masthead.select("td div");
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("解析作品详情", msg);
            e.printStackTrace();
        }
//        return novels;
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
