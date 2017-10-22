package com.wux.wenku.util;

import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by WuX on 2017/10/9.
 */

public class ProcessText {
    private long pages;//总页数
    private final int SIZE = 900;//每一页的字节数 字节数固定
    private long bytescount;//字节总数
    private long currentpage;//当前页面
    private RandomAccessFile readFile;

    //构造方法 传入当前页 为了实现书签的功能 记录用户读取的文章位置
    public ProcessText(File file, int currentpage) {
        try {


            readFile = new RandomAccessFile(file, "r");


            bytescount = readFile.length();//获得字节总数
            pages = bytescount / SIZE;//计算得出文本的页数
            this.currentpage = currentpage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //定位字节位置 根据页数定位文本的位置
    private void seek(long page) {
        try {
            //if(pages)
            readFile.seek(page * SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//固定读取 SIZE+30个字节的内容 为什么+30 ？

//读取的为字节 需要进行转码 转码不可能刚好转的就是文本内容,

//一页的末尾 和开始出有可能乱码 每一次多读30个字节 是为了第一页乱码位置

//在第二页就可以正常显示出内容 不影响阅读


    private String read() {
        //内容重叠防止 末尾字节乱码
        byte[] chs = new byte[SIZE + 30];
        try {

            readFile.read(chs);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(chs, Charset.forName("utf-8"));
    }

    //上一页功能的实现
    public String getPre() {
        String content = null;
        //第一页 的情况 定位在0字节处 读取内容 当前页数不改变
        if (currentpage <= 1) {
            seek(currentpage - 1);
            content = read();
        } else {
            //其它页 则定位到当前页-2 处 在读取指定字节内容 例如当前定位到第二页的末尾 2*SIZE  上一页应该是第一页 也就是从0位置 开始读取SIZE个字节
            seek(currentpage - 2);
            content = read();
            currentpage = currentpage - 1;
        }

        return content;
    }

    //下一页功能的实现
    public String getNext() {

        String content = null;
        if (currentpage >= pages) {//当前页为最后一页时候,显示的还是 最后一页
            seek(currentpage - 1);
            content = read();
        } else {
            seek(currentpage);
            content = read();
            currentpage = currentpage + 1;
        }

        return content;
    }

    /*********************************************************/
    public int[] getPage(TextView textView) {
        int count = textView.getLineCount();
        textView.setText(mContent);
        int pCount = getPageLineCount(textView);
        int pageNum = count / pCount;
        int page[] = new int[pageNum];
        for (int i = 0; i < pageNum; i++) {
            page[i] = textView.getLayout().getLineEnd((i + 1) * pCount - 1);
        }
        return page;
    }

    private int getPageLineCount(TextView view) {    /*
       * The first row's height is different from other row.
      */
        int h = view.getBottom() - view.getTop() - view.getPaddingTop();
        int firstH = getLineHeight(0, view);
        int otherH = getLineHeight(1, view);
        return (h - firstH) / otherH + 1;
    }

    private int getLineHeight(int line, TextView view) {
        Rect rect = new Rect();
        view.getLineBounds(line, rect);
        return rect.bottom - rect.top;
    }

    class ContentAdapter extends PagerAdapter {
        List mCache;
        private int[] mPage;
        private String mContent;

        public ContentAdapter(int[] page, String content) {
            mPage = page;
            mContent = content;
        }

        @Override
        public int getCount() {
            return mPage.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private String getText(int page) {
            if (page == 0) {
                return mContent.substring(0, mPage[0]);
            }
            return mContent.substring(mPage[page - 1], mPage[page]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = null;
            if (mCache == null) {
                mCache = new LinkedList();
            }
            if (mCache.size() > 0) {
                textView = (TextView) mCache.remove(0);
            } else {
                textView = new TextView(container.getContext());
            }
            textView.setText(getText(position));
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mCache.add(object);
        }
    }

}

