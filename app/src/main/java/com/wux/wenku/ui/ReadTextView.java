package com.wux.wenku.ui;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wuxuan on 2017/10/25.
 */

public class ReadTextView extends android.support.v7.widget.AppCompatTextView {

    public ReadTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReadTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadTextView(Context context) {
        super(context);
    }


    /**
     * 获取可显示行数
     *
     * @return
     */
    @Override
    public int getLineCount() {
        return super.getLineCount();
    }

    /**
     * 获取每行可显示字数
     *
     * @return
     */
    public int getNumOfLine() {
        float fontSize = getTextSize();
        float w = getWidth();
        float h = getHeight();
        float fontScalex = getTextScaleX();
        float p_left = getPaddingLeft();
        float p_right = getPaddingRight();
        int num = (int) (w - p_left - p_right + fontScalex) / (int) (fontScalex + fontSize);
        return num;
    }
}
