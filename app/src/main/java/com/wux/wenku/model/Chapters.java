package com.wux.wenku.model;

import java.io.Serializable;

/**
 * Created by WuX on 2017/4/27.
 */

public class Chapters implements Serializable {
    private int index;// 序号
    private String chapterName;// 文章名
    private String text;//文章内容
    private String url;// 链接

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
