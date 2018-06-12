package com.wux.wenku.model;

import com.wux.wenku.app.AppConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuX on 2017/4/16.
 */

public class Novels implements Serializable {
    private String nTitle;// 标题
    private String nContent;// 简介
    private String nAuthor;// 作者
    private String nClassify;// 分类
    private String nUpdTime;// 最后更新时间
    private String nCoverImgUrl;// 封面链接
    private String nDetailsUrl;// 详情链接
    private String nCatalogUrl;// 目录链接
    private String nLastUpdChapter;// 最新章节
    private String nLastUpdChapterUrl;// 最新章节的链接
    private String nUpdStatus;//更新状态
    private String nLength;//文章长度
    private String nAddBookCaseUrl;//加入书架的url
    private String nUserVote;//用户推荐
    private List<Chapters> chapterses = new ArrayList<>(); // 目录
    private List<Novels> recommend1 = null;// 同分类推荐
    private List<Novels> recommend2 = null;// 同分类完结推荐
    private List<Novels> others = null;
    private String nBookCaseUrl = null;// 移除书架的URL
    private String nBookID = null;// 网站中的编号

    public void addOthers(Novels novel) {
        if (others == null) {
            others = new ArrayList<>();
        }
        others.add(novel);
    }

    public List<Novels> getOthers() {
        if (others == null) {
            others = new ArrayList<>();
        }
        return others;
    }

    public void addRecommend1(Novels novel) {
        if (recommend1 == null) {
            recommend1 = new ArrayList<>();
        }
        recommend1.add(novel);
    }

    public List<Novels> getRecommend1() {
        return recommend1;
    }

    public void addRecommend2(Novels novel) {
        if (recommend2 == null) {
            recommend2 = new ArrayList<>();
        }
        recommend2.add(novel);
    }

    public List<Novels> getRecommend2() {
        return recommend2;
    }

    public String getnLength() {
        return nLength;
    }

    public void setnLength(String nLength) {
        this.nLength = nLength;
    }

    public String getnUpdStatus() {
        return nUpdStatus;
    }

    public void setnUpdStatus(String nUpdStatus) {
        this.nUpdStatus = nUpdStatus;
    }

    public String getnLastUpdChapter() {
        return nLastUpdChapter;
    }

    public void setnLastUpdChapter(String nLastUpdChapter) {
        this.nLastUpdChapter = nLastUpdChapter;
    }

    public String getnLastUpdChapterUrl() {
        return nLastUpdChapterUrl;
    }

    public void setnLastUpdChapterUrl(String nLastUpdChapterUrl) {
        this.nLastUpdChapterUrl = nLastUpdChapterUrl;
    }

    public void addChapterse(Chapters chapters) {
        if (null == chapterses) {
            chapterses = new ArrayList<Chapters>();
        }
        chapterses.add(chapters);
    }

    public List<Chapters> getChapterses() {
        return chapterses;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public String getnContent() {
        return nContent;
    }

    public void setnContent(String nContent) {
        this.nContent = nContent;
    }

    public String getnAuthor() {
        return nAuthor;
    }

    public void setnAuthor(String nAuthor) {
        this.nAuthor = nAuthor;
    }

    public String getnClassify() {
        return nClassify;
    }

    public void setnClassify(String nClassify) {
        this.nClassify = nClassify;
    }

    public String getnUpdTime() {
        return nUpdTime;
    }

    public void setnUpdTime(String nUpdTime) {
        this.nUpdTime = nUpdTime;
    }

    public String getnCoverImgUrl() {
        return nCoverImgUrl;
    }

    public void setnCoverImgUrl(String nCoverImgUrl) {
        this.nCoverImgUrl = nCoverImgUrl;
    }

    public String getnDetailsUrl() {
        return nDetailsUrl;
    }

    public void setnDetailsUrl(String nDetailsUrl) {
        this.nDetailsUrl = nDetailsUrl;
    }

    public String getnCatalogUrl() {
        return nCatalogUrl;
    }

    public void setnCatalogUrl(String nCatalogUrl) {
        this.nCatalogUrl = nCatalogUrl;
    }

    public String getnAddBookCaseUrl() {
        return nAddBookCaseUrl;
    }

    public void setnAddBookCaseUrl(String nAddBookCaseUrl) {
        this.nAddBookCaseUrl = nAddBookCaseUrl;
    }

    public String getnUserVote() {
        return nUserVote;
    }

    public void setnUserVote(String nUserVote) {
        this.nUserVote = nUserVote;
    }

    public String getnBookCaseUrl() {
        String url = nBookCaseUrl.replace("javascript:if(confirm('确实要将本书移出书架么？')) document.location='", "").replace("'", "").replace(";", "");
        url = AppConfig._IndexURL + url;
        return url;
    }

    public void setnBookCaseUrl(String nBookCaseUrl) {
        this.nBookCaseUrl = nBookCaseUrl;
    }

    public String getnBookID() {
        return nBookID;
    }

    public void setnBookID(String nBookID) {
        this.nBookID = nBookID;
    }
}
