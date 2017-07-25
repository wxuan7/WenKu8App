package com.wux.wenku.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fanyafeng.wenku.Constant.MaterialDesignConstant;
import com.wux.wenku.BaseActivity;
import com.wux.wenku.R;
import com.wux.wenku.adapter.RVAdapter;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Chapters;
import com.wux.wenku.model.Novels;
import com.wux.wenku.parse.ParseArticle;
import com.wux.wenku.parse.ParseChapteresList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaggeredGridLayoutActivity extends BaseActivity implements AppConfig.OnHandlerCallBack {

    private RecyclerView rvStagger;//章节列表
    private NestedScrollView articleBottom;//文章
    private TextView tv_article;//正文文本
    private BottomSheetBehavior bottomSheetBehavior;

    private List<String> stringList;
    private RVAdapter rvAdapter;

    private SimpleDraweeView sdvStaggerTop;
    private Novels mNovels = null;
    private List<String> titles_list = new ArrayList<>();
    private List<Chapters> nList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_grid_layout);
        mNovels = (Novels) getIntent().getSerializableExtra("novel");
        title = mNovels.getnTitle();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "正在保存", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
        initData();
        loadChapteresList();
    }


    private void initView() {
        rvStagger = (RecyclerView) findViewById(R.id.rvStagger);
        rvStagger.setHasFixedSize(true);
        sdvStaggerTop = (SimpleDraweeView) findViewById(R.id.sdvRvItem);
        articleBottom = (NestedScrollView) findViewById(R.id.articleBottom);
        tv_article = (TextView) findViewById(R.id.tv_article);
    }

    /**
     * 加载bottomSheet
     *
     * @param view
     */
    private void initNest(final View view) {
        bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                view.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(view, slideOffset);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        if (mNovels.getnCoverImgUrl() != null && !"".equals(mNovels.getnCoverImgUrl())) {
            ActivityOptions.makeSceneTransitionAnimation(this, sdvStaggerTop, "SimpleDraweeView");
            sdvStaggerTop.setImageURI(Uri.parse(mNovels.getnCoverImgUrl()));
        } else {
            sdvStaggerTop.setImageURI(Uri.parse("http://img0.imgtn.bdimg.com/it/u=3808587152,3416392106&fm=21&gp=0.jpg"));
            sdvStaggerTop.setImageURI(Uri.parse("http://img.wenku8.com/image/1/1508/1508s.jpg"));
        }
        stringList = new ArrayList<>();
        stringList = Arrays.asList(MaterialDesignConstant.imageList);
//        List<String> list = new ArrayList<String>();
//        for (int i = 0; i < 50; i++) {
//            list.add(i + "");
//        }
        rvAdapter = new RVAdapter(this, titles_list, 2);
        rvStagger.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rvStagger.setAdapter(rvAdapter);

        rvAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, String string, final int position) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    String namr = "";
//                    for (int i = 0; i < 100; i++) {
//                        namr += System.currentTimeMillis() + "\r\n";
//                    }
//                    tv_article.setText(namr);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String text = ParseArticle.parseArticle(mNovels.getnTitle(),nList.get(position).getChapterName(),nList.get(position).getUrl());
                                Bundle data = new Bundle();
                                data.putString("text", text.replace(" ", "\n"));
                                AppConfig.sendMessage(1, StaggeredGridLayoutActivity.this, 2, 0, data);
                            } catch (Exception e) {
                                e.printStackTrace();
                                AppConfig.sendMessage(0,e.getMessage());
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onItemLongClickListener(View view, String string, int position) {

            }
        });
        initNest(articleBottom);
    }

    private void loadChapteresList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Chapters> list = ParseChapteresList.parseChapteresList(mNovels.getnDetailsUrl());
                    nList.addAll(list);
                    for (Chapters ch : list) {
                        mNovels.addChapterse(ch);
                        titles_list.add(ch.getChapterName());
                    }
                    AppConfig.sendMessage(1, StaggeredGridLayoutActivity.this, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0,e.getMessage());
                }

            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();

        }
    }

    @Override

    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1) {
            case 1:
                rvAdapter.notifyDataSetChanged();
                break;
            case 2:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                String namr = data.getString("text");
                tv_article.setText(namr);
                break;
        }
    }
}
