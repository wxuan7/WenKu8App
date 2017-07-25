package com.wux.wenku.parse;

/**
 * Created by WuX on 2017/4/18.
 */

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Novels;
import com.wux.wenku.model.Tags;
import com.wux.wenku.util.JsoupUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;


public class ParseTags extends ParseHTML {
    /**
     * 解析英语标签
     *
     * @param href
     * @return
     * @throws Exception
     */
    public static ArrayList<Tags> parseEnglish(String href) throws Exception {
        ArrayList<Tags> list = new ArrayList<Tags>();
        try {
            Document doc = AppConfig.mJsoupUtil.getDocument(href);//Jsoup.connect(href).cookies(AppConfig._Cookie).timeout(10000).get();
            Elements masthead = doc.select("div#content table tbody tr").first().select("td a");
            for (int i = 1; i < masthead.size()-1; i++) {
                Tags tag = new Tags();
                Element tagElement = masthead.get(i);
                tag.setTitle(tagElement.text().replace("[","").replace("]",""));
                tag.setTagUrl(tagElement.attr("href"));
                list.add(tag);
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    /**
     * 解析分类标签
     *
     * @param href
     * @return
     * @throws Exception
     */
    public static ArrayList<Tags> parseType(String href) throws Exception {
        ArrayList<Tags> list = new ArrayList<Tags>();
        try {
            Document doc = AppConfig.mJsoupUtil.getDocument(href);//Jsoup.connect(href).cookies(AppConfig._Cookie).timeout(10000).get();
            Elements masthead = doc.select("ul.ulitem li a");
            for (int i = 1; i < masthead.size(); i++) {
                Tags tag = new Tags();
                Element tagElement = masthead.get(i);
                tag.setTitle(tagElement.text());
                tag.setTagUrl(tagElement.attr("href"));
                list.add(tag);
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            e.printStackTrace();
            throw e;
        }
        return list;
    }

}
