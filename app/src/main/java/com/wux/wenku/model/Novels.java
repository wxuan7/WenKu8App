package com.wux.wenku.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuX on 2017/4/16.
 */

public class Novels  implements Serializable{
    private String nTitle;// 标题
    private String nContent;// 简介
    private String nAuthor;// 作者
    private String nClassify;// 分类
    private String nUpdTime;// 最后更新时间
    private String nCoverImgUrl;// 封面链接
    private String nDetailsUrl;// 详情链接
    private String nCatalogUrl;// 目录链接
    private List<Chapters> chapterses = null; // 目录
    private String nLastUpdChapter;// 最新章节
    private String nLastUpdChapterUrl;// 最新章节的链接

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

    public void addChapterse(Chapters chapters){
        if(null== chapterses){
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
}
