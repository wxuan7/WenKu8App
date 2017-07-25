package com.wux.wenku.parse;

/**
 * 解析正文
 * Created by WuX on 2017/4/18.
 */

import android.util.Log;

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Chapters;
import com.wux.wenku.util.FileUtil;
import com.wux.wenku.util.JsoupUtil;

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

    public static String parseArticle(String novel, String chapter, String href) throws Exception {
        if (FileUtil.isFileExist(novel, chapter)) {
            Thread.sleep(1000);
            return FileUtil.getContent(novel, chapter);
        } else {
            ArrayList<Chapters> list = new ArrayList<Chapters>();
            try {
                Document doc = AppConfig.mJsoupUtil.getDocument(href);
                Element masthead = doc.select("div#content").first();
                String content = masthead.text().replace(" ", "\r\n");
                FileUtil.putContent(novel,chapter,content);
                return content;
            } catch (Exception e) {
                String msg = e.getMessage();
                Log.e("解析章节目录", msg);
                e.printStackTrace();
                throw e;
            }
        }
    }

}
