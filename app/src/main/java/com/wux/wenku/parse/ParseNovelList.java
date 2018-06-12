package com.wux.wenku.parse;

/**
 * Created by WuX on 2017/4/18.
 */

import android.util.Log;

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Novels;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 如果html的标签的class包含空格，例如：<li class="archive-item clearfix"></>需要连续调用select  select("li.archive-item").select("li.clearfix");
 */
public class ParseNovelList extends ParseHTML {
    static int num = 20;

    public static ArrayList<Novels> parseNovelListList(String href, final int page) throws Exception {
//        setCookies();
        ArrayList<Novels> list = new ArrayList<Novels>();
        try {
            href = _MakeURL(href, new HashMap<String, Object>() {
                {
                    put("page", page);
                }
            });
            Document doc = AppConfig.mJsoupUtil.getDocument(href);//Jsoup.connect(href).cookies(AppConfig._Cookie).timeout(10000).get();
            Elements head = doc.select("table.grid tbody tr");
            Element masthead = null;
            if (href.contains("anime")) {
                masthead = head.get(1);
            } else {
                masthead = head.first();
            }
            Elements NovelListElements = masthead.select("td div");
            for (int i = 0; i < NovelListElements.size(); i = i + 3) {
                Novels novels = new Novels();
                Element NovelListElement = NovelListElements.get(i);
                Element titleElement = NovelListElement.select("div b a").first();
//                Element summaryElement = NovelListElement.select("div p").first();
                Elements contentElements = NovelListElement.select("div p");
                for (int j = 0; j < contentElements.size(); j++) {
                    switch (j) {
                        case 0:
                            Element zuozhe = contentElements.select("p").get(j);
                            novels.setnAuthor(zuozhe.text());
                            break;
                        case 1:
                            Element time = contentElements.select("p").get(j);
                            novels.setnUpdTime(time.text());
                            break;
                        case 2:
                            Element jianjie = contentElements.select("p").get(j);
                            novels.setnContent(jianjie.text());
                            break;
                        case 3:
                            if ("".equals(novels.getnContent())) {
                                novels.setnContent(contentElements.select("p").get(j).text());
                            }
                            break;
                    }
                }
                Element imgElement = null;
                if (NovelListElement.select("img").size() != 0) {
                    imgElement = NovelListElement.select("img").first();
                }
//                Element timeElement = NovelListElement
//                        .select("div.archive-data span.glyphicon-class").first();
                String url = titleElement.attr("href");
                String title = titleElement.text();
//                String summary = summaryElement.text();
//                if (summary.length() > 70)
//                    summary = summary.substring(0, 70);
                String imgsrc = "";
                if (imgElement != null) {
                    imgsrc = imgElement.attr("src");
                }

//                String postTime = timeElement.text();
                novels.setnDetailsUrl(url);
                novels.setnTitle(title);
                novels.setnCoverImgUrl(imgsrc);
//                novels.setnUpdTime(postTime);
                list.add(novels);
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    public static ArrayList<Novels> parseSearchNovel(String href) throws Exception {
//        setCookies();
        ArrayList<Novels> list = new ArrayList<Novels>();
        try {
            Log.i("url",href);
            Document doc = AppConfig.mJsoupUtil.getDocument(href);//Jsoup.connect(href).cookies(AppConfig._Cookie).timeout(10000).get();
            Log.i("doc",doc.toString());
            Element masthead = doc.select("table.grid tbody tr").first();
            Elements NovelListElements = masthead.select("td div");
            for (int i = 0; i < NovelListElements.size(); i = i + 3) {
                Novels novels = new Novels();
                Element NovelListElement = NovelListElements.get(i);
                Element titleElement = NovelListElement.select("div b a").first();
//                Element summaryElement = NovelListElement.select("div p").first();
                Elements contentElements = NovelListElement.select("div p");
                for (int j = 0; j < contentElements.size(); j++) {
                    switch (j) {
                        case 0:
                            Element zuozhe = contentElements.select("p").get(j);
                            novels.setnAuthor(zuozhe.text());
                            break;
                        case 1:
                            Element time = contentElements.select("p").get(j);
                            novels.setnUpdTime(time.text());
                            break;
                        case 2:
                            Element jianjie = contentElements.select("p").get(j);
                            novels.setnContent(jianjie.text());
                            break;
                    }
                }
                Element imgElement = null;
                if (NovelListElement.select("img").size() != 0) {
                    imgElement = NovelListElement.select("img").first();
                }
//                Element timeElement = NovelListElement
//                        .select("div.archive-data span.glyphicon-class").first();
                String url = titleElement.attr("href");
                String title = titleElement.text();
//                String summary = summaryElement.text();
//                if (summary.length() > 70)
//                    summary = summary.substring(0, 70);
                String imgsrc = "";
                if (imgElement != null) {
                    imgsrc = imgElement.attr("src");
                }

//                String postTime = timeElement.text();
                novels.setnDetailsUrl(url);
                novels.setnTitle(title);
                novels.setnCoverImgUrl(imgsrc);
//                novels.setnUpdTime(postTime);
                list.add(novels);
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            e.printStackTrace();
            throw e;
        }

        return list;
    }
}
