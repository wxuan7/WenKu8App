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

    public static Novels parseDetail(String detailsUrl) throws Exception {
        Novels novels = new Novels();
//        setCookies();//设置cookie
        ArrayList<Chapters> list = new ArrayList<Chapters>();
        try {
            Document doc = AppConfig.mJsoupUtil.getDocument(detailsUrl);//Jsoup.connect(detailsUrl).cookies(AppConfig._Cookie).timeout(10000).get();
//            Elements masthead = doc.select("div#content div tabel tbody tr td span");
            Elements masthead1 = doc.select("div#content div");
            Elements masthead = masthead1.first().select("table").get(2).select("tbody tr td span");
            Element lastElement = null;
            Element contentElement;
            if (masthead.size() == 5) {
                lastElement = masthead.get(2);
                contentElement = masthead.get(4);
            } else if (masthead.size() == 4) {
                lastElement = masthead.get(1);
                contentElement = masthead.get(3);
            } else {
                contentElement = masthead.get(2);
            }
            //  最近更新的章节信息
            if (null != lastElement) {
                novels.setnLastUpdChapter(lastElement.text());// 最新一章节名
                Log.e("解析作品详情-最后一章节", lastElement.text());
                try {
                    novels.setnLastUpdChapterUrl(lastElement.select("a").first().attr("href"));// 最新章节链接
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            novels.setnContent(contentElement.text());//作品的简介
            Log.e("解析作品详情-简介", contentElement.text());

            Element titleEle = masthead1.first().select("table tbody tr td table tbody tr td span b").first();
            novels.setnTitle(titleEle.text());
            Elements catalogElements = doc.select("div#content div div div span fieldset div a");
            Element catalogElement = catalogElements.first();
            novels.setnCatalogUrl(catalogElement.attr("href"));//目录的链接
//            Elements NovelListElements = masthead.select("td div");
            if (catalogElements.size() >= 2) {
                novels.setnAddBookCaseUrl(catalogElements.get(1).attr("href"));
            }
            if (catalogElements.size() >= 3) {
                novels.setnUserVote(catalogElements.get(2).attr("href"));
            }
            Element paramEle = masthead1.first().select("table tbody tr").get(2);
            Elements paramEles = paramEle.select("td");
            //防止某些文章的介绍信息不全导致索引溢出
            for (int i = 0; i < paramEles.size(); i++) {
                switch (i) {
                    case 0:
                        String classify = paramEles.get(0).text();//文库分类
                        novels.setnClassify(classify);
                        break;
                    case 1:
                        String author = paramEles.get(1).text();//作者
                        novels.setnAuthor(author);
                        break;
                    case 2:
                        String updStatus = paramEles.get(2).text();//更新状态
                        novels.setnUpdStatus(updStatus);
                        break;
                    case 3:
                        String lastUpdTime = paramEles.get(3).text();//最后更新时间
                        novels.setnUpdTime(lastUpdTime);
                        break;
                    case 4:
                        String len = paramEles.get(4).text();//最后更新时间
                        novels.setnLength(len);
                        break;
                }
            }
            novels.setnDetailsUrl(detailsUrl);
            String baseUrl = detailsUrl.substring(0, detailsUrl.lastIndexOf("/"));
//            Element paramEle2 = masthead1.first().select("table").first().select("tbody tr td span").first();
            Elements paramEle2 = masthead1.first().select("table").get(2).select("tbody tr td img");
            novels.setnCoverImgUrl(paramEle2.attr("src"));
            /************同分类推荐**************/
            Elements paramEle3 = masthead1.first().select("table").get(3).select("tbody tr td");
            Elements paramEle3_type1 = paramEle3.get(1).select("div div");
            for (int i = 0; i < paramEle3_type1.size(); i++) {
                Element e1 = paramEle3_type1.get(i);
                Novels novel = new Novels();
                novel.setnCoverImgUrl(e1.select("a img").first().attr("src"));
                novel.setnDetailsUrl(e1.select("a").first().attr("href"));
                novel.setnTitle(e1.select("a").first().attr("title"));
                novels.addRecommend1(novel);
            }
            /************同分类推荐**************/
            Elements paramEle4 = masthead1.first().select("table").get(3).select("tbody tr td");
            Elements paramEle4_type1 = paramEle4.get(3).select("div div");
            for (int i = 0; i < paramEle4_type1.size(); i++) {
                Element e1 = paramEle4_type1.get(i);
                Novels novel = new Novels();
                novel.setnCoverImgUrl(e1.select("a img").first().attr("src"));
                novel.setnDetailsUrl(e1.select("a").first().attr("href"));
                novel.setnTitle(e1.select("a").first().attr("title"));
                novels.addRecommend2(novel);
            }
            /************同作者作品**************/
            // 解析作者专栏的URL
           /* Element paramEle5 = masthead1.first().select("div").get(4).select("div span").get(2).select("fieldset div a").first();
            String othersUrl = paramEle5.attr("href");
            // 解析下一个界面
            getOthers(novels,othersUrl);
            if(novels.getOthers().size()==0){
                String autor = novels.getnAuthor().substring(novels.getnAuthor().indexOf("：")+1);
                othersUrl = "http://www.wenku8.com/modules/article/authorarticle.php?author="+ URLEncoder.encode(autor,"gbk");
                getOthers(novels,othersUrl);
            }*/
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("解析作品详情", msg);
            e.printStackTrace();
            throw e;
        }
        return novels;
    }

    private static void getOthers(Novels novels, String url) throws Exception {
        Document otherDoc = AppConfig.mJsoupUtil.getDocument(url);//Jsoup.connect(url).cookies(AppConfig._Cookie).timeout(10000).get();
        Elements otherEles = otherDoc.select("table.grid tbody tr td div div");
        for (int i = 0; i < otherEles.size(); i = i + 2) {
            Element e = otherEles.get(i).select("a").first();
            Novels n = new Novels();
            n.setnTitle(e.attr("title"));
            n.setnDetailsUrl(e.attr("href"));
            n.setnCoverImgUrl(e.select("img").attr("src"));
            novels.addOthers(n);
        }
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

    /**
     * 加入书架
     *
     * @param url
     * @return
     */
    public static String addBookCase(String url) throws Exception {
        String title = "";
        try {
            long begin = System.currentTimeMillis();
            Document doc = Jsoup.connect(url).cookies(AppConfig._Cookie).timeout(10000).get();
            Element ele1 = doc.select("div.blocktitle").first();
            title = ele1.text();
            Element ele2 = doc.select("div.blockcontent div").first();
            title += "\n" + ele2.text();
            long end = System.currentTimeMillis();
            if (end - begin < 1000) {
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return title;
    }
}
