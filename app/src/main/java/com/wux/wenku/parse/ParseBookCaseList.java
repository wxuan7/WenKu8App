package com.wux.wenku.parse;

/**
 * Created by WuX on 2017/4/18.
 */

import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Novels;
import com.wux.wenku.util.FileUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 如果html的标签的class包含空格，例如：<li class="archive-item clearfix"></>需要连续调用select  select("li.archive-item").select("li.clearfix");
 */
public class ParseBookCaseList extends ParseHTML {
    static int num = 20;

    public static ArrayList<Novels> parseNovelList(String href, int bookcase, final int page) throws Exception {
//        setCookies();
        ArrayList<Novels> list = new ArrayList<Novels>();
        try {
            Document doc = null;
            if (page == 1) {
                doc = AppConfig.mJsoupUtil.getDocument(href + bookcase);//Jsoup.connect(href).cookies(AppConfig._Cookie).timeout(10000).get();
                FileUtil.saveFavorite(bookcase, doc.toString());
            } else {
                String html = FileUtil.getFavoriteHtml(bookcase);
                if (null != html && !"".equals(html)) {
                    doc = AppConfig.mJsoupUtil.parseDocument(html);
                }
            }
            if (null == doc) {
                doc = AppConfig.mJsoupUtil.getDocument(href);
            }
            Elements nListElements = doc.select("table.grid tbody tr");
            int max = page * num > nListElements.size() - 2 ? nListElements.size() - 2 : page * num;
            for (int i = (page - 1) * num + 1; i <= max; i++) {
                Novels novels = new Novels();
                Element novelListElement = nListElements.get(i);
                Element bookurl = novelListElement.select("td.even a").first();
                novels.setnDetailsUrl(bookurl.attr("href"));
                novels = ParseNovelDetail.parseDetail(novels.getnDetailsUrl());
                novels.setnBookID(novelListElement.select("td.odd input").first().attr("value"));
                Element removebookurl = novelListElement.select("td.even a").last();
                novels.setnBookCaseUrl(removebookurl.attr("href"));

                if (i > (page - 1) * num && list.size() < page * num) {
                    list.add(novels);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    public static boolean removeBookCase(String url, String bookUrl) throws Exception {
        try {
            Document doc = Jsoup.connect(url).cookies(AppConfig._Cookie).timeout(10000).get();
            if (doc.toString().contains(bookUrl)) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }
}
