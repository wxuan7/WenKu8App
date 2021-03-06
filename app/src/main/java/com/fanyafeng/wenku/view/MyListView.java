package com.fanyafeng.wenku.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by fanyafeng on 2016/1/9,0009.
 */
public class MyListView extends ListView {
    private OnScrollListener onScrollListener;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.onScrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.OnScroll(l, t, oldl, oldt);
        }
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
    }

    public interface OnScrollListener {
        public void OnScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

}
