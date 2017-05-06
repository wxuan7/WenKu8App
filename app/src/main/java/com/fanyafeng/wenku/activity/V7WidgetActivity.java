package com.fanyafeng.wenku.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.wux.wenku.BaseActivity;
import com.wux.wenku.R;
import com.fanyafeng.wenku.view.FocusView;

public class V7WidgetActivity extends BaseActivity {
    private ImageButton ibLocal;
    private FocusView focusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v7_widget);

        title = "v7包中的一些控件";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
        initData();
    }

    private void initView() {
        focusView = (FocusView) findViewById(R.id.focusView);
        ibLocal = (ImageButton) findViewById(R.id.ibLocal);
        ibLocal.requestFocus();
    }

    private void initData() {
        ibLocal.setOnFocusChangeListener(onFocusChangeListener);
    }

    private View mCurrentView;

    public View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mCurrentView = v;
                enlargeAnim(v);
                focusView.setVisibility(View.VISIBLE);
                focusView.setAnchorView(v);
            } else {
                reduceAnim(v);
            }
        }
    };

//    private View mCurrentView;
//
//    public View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
//
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            if (hasFocus) {
//                mCurrentView = v;
//                enlargeAnim(v);
//            } else {
//                reduceAnim(v);
//            }
//        }
//    };
//
//    public View getCurrentView() {
//        return mCurrentView;
//    }


    public void enlargeAnim(View v) {
        Animation a = android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.uikit_enlarge);
        a.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        a.setFillAfter(true);
        v.clearAnimation();
        v.setAnimation(a);
        v.bringToFront();
        a.start();
    }

    public void reduceAnim(View v) {
        Animation a = android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.uikit_reduce);
        a.setFillAfter(true);
        v.clearAnimation();
        v.startAnimation(a);
        a.start();
    }

}