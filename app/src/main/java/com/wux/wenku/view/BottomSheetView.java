package com.wux.wenku.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wux.wenku.R;
import com.wux.wenku.activity.NovelsActivity;
import com.wux.wenku.adapter.RVRecommendAdapter;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Novels;
import com.wux.wenku.parse.ParseNovelDetail;
import com.wux.wenku.ui.RemoteProgressDialog;

/**
 * 单独处理tablayout中的小说详情界面，
 * <p>
 * Created by WuX on 2017/5/10.
 */

public class BottomSheetView implements View.OnClickListener, AppConfig.OnHandlerCallBack, RVRecommendAdapter.OnItemClickListener {

    private NestedScrollView detailBottom;// 详情页
    private BottomSheetBehavior bottomSheetBehavior;
    //小说详情页的控件
    private SimpleDraweeView sdv_cover = null;//封面
    private TextView tv_title = null;// 标题
    private TextView tv_lastupd = null;// 最后更新章节
    private TextView tv_catalog = null;// 目录链接
    private TextView tv_intro = null; // 简介
    private TextView tv_classify = null;// 分类
    private TextView tv_author = null;// 作者
    private TextView tv_status = null;// 状态
    private TextView tv_lastupdtime = null; // 最后更新时间
    private TextView tv_length = null; // 文章长度
    private TextView tv_addbookcase = null;// 加入书架
    private TextView tv_uservote = null;// 推荐本书
    private Activity mActivity = null;
    private CoordinatorLayout cl_bottom = null;

    private RVRecommendAdapter rvAdapter_type1;
    private RecyclerView rv_type1;
    private RVRecommendAdapter rvAdapter_type2;
    private RecyclerView rv_type2;
    private RVRecommendAdapter rvAdapter_others;
    private RecyclerView rv_others;

    private Novels mNovel = null;

    public BottomSheetView(Activity activity) {
        mActivity = activity;
    }

    public void initView() {
        detailBottom = (NestedScrollView) mActivity.findViewById(R.id.nestedBottom);
        sdv_cover = (SimpleDraweeView) mActivity.findViewById(R.id.sdv_cover);
        tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
        tv_lastupd = (TextView) mActivity.findViewById(R.id.tv_lastupd);
        tv_catalog = (TextView) mActivity.findViewById(R.id.tv_catalog);
        tv_addbookcase = (TextView) mActivity.findViewById(R.id.tv_addbookcase);
        tv_uservote = (TextView) mActivity.findViewById(R.id.tv_uservote);
        tv_intro = (TextView) mActivity.findViewById(R.id.tv_intro);
        tv_classify = (TextView) mActivity.findViewById(R.id.tv_classify);
        tv_author = (TextView) mActivity.findViewById(R.id.tv_author);
        tv_status = (TextView) mActivity.findViewById(R.id.tv_status);
        tv_lastupdtime = (TextView) mActivity.findViewById(R.id.tv_lastupdtime);
        tv_length = (TextView) mActivity.findViewById(R.id.tv_length);
        rv_type1 = (RecyclerView) mActivity.findViewById(R.id.rv_type1);
        rv_type2 = (RecyclerView) mActivity.findViewById(R.id.rv_type2);
        rv_others = (RecyclerView) mActivity.findViewById(R.id.rv_others);
        cl_bottom = (CoordinatorLayout) mActivity.findViewById(R.id.cl_bottom);
        tv_catalog.setOnClickListener(this);
        tv_lastupd.setOnClickListener(this);
        tv_uservote.setOnClickListener(this);
        tv_addbookcase.setOnClickListener(this);
        initNest();
    }

    public void initView(View view) {
        detailBottom = (NestedScrollView) view.findViewById(R.id.nestedBottom);
        sdv_cover = (SimpleDraweeView) view.findViewById(R.id.sdv_cover);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_lastupd = (TextView) view.findViewById(R.id.tv_lastupd);
        tv_catalog = (TextView) view.findViewById(R.id.tv_catalog);
        tv_addbookcase = (TextView) view.findViewById(R.id.tv_addbookcase);
        tv_uservote = (TextView) view.findViewById(R.id.tv_uservote);
        tv_intro = (TextView) view.findViewById(R.id.tv_intro);
        tv_classify = (TextView) view.findViewById(R.id.tv_classify);
        tv_author = (TextView) view.findViewById(R.id.tv_author);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        tv_lastupdtime = (TextView) view.findViewById(R.id.tv_lastupdtime);
        tv_length = (TextView) view.findViewById(R.id.tv_length);
        rv_type1 = (RecyclerView) view.findViewById(R.id.rv_type1);
        rv_type2 = (RecyclerView) view.findViewById(R.id.rv_type2);
        rv_others = (RecyclerView) view.findViewById(R.id.rv_others);
        cl_bottom = (CoordinatorLayout) view.findViewById(R.id.cl_bottom);
        tv_catalog.setOnClickListener(this);
        tv_lastupd.setOnClickListener(this);
        tv_uservote.setOnClickListener(this);
        tv_addbookcase.setOnClickListener(this);

        detailBottom.setFocusable(false);
        initNest();
    }

    /**
     * 加载bottomSheet
     */
    private void initNest() {

        bottomSheetBehavior = BottomSheetBehavior.from(detailBottom);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    detailBottom.setVisibility(View.GONE);
                    detailBottom.setFocusable(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                detailBottom.setVisibility(View.VISIBLE);
//                detailBottom.setFocusable(true);
                ViewCompat.setAlpha(detailBottom, slideOffset);
            }
        });
    }

    /**
     * 切换table时收起
     */
    public void tableChanage() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void syncNovelFetail(final String url) {
        new RemoteProgressDialog(mActivity, new Runnable() {
            @Override
            public void run() {
                try {
                    mNovel = ParseNovelDetail.parseDetail(url);
                    AppConfig.sendMessage(1, BottomSheetView.this, 2, 0, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
                }
            }
        }).start();
    }

    public void initNovelDetail() {
        if (cl_bottom != null && cl_bottom.getVisibility() == View.GONE) {
            cl_bottom.setVisibility(View.VISIBLE);
        }
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            detailBottom.smoothScrollTo(0, 0);
            sdv_cover.setImageURI(Uri.parse(mNovel.getnCoverImgUrl()));
            if (null == mNovel.getnLastUpdChapter() || "".equals(mNovel.getnLastUpdChapter())) {
                tv_lastupd.setVisibility(View.GONE);
            } else {
                tv_lastupd.setText("最新章节：" + mNovel.getnLastUpdChapter());
            }
            tv_title.setText(mNovel.getnTitle());
            tv_intro.setText(mNovel.getnContent());
            tv_classify.setText(mNovel.getnClassify());
            tv_author.setText(mNovel.getnAuthor());
            tv_status.setText(mNovel.getnUpdStatus());
            tv_lastupdtime.setText(mNovel.getnUpdTime());
            tv_length.setText(mNovel.getnLength());

            rvAdapter_type1 = new RVRecommendAdapter(mActivity, mNovel.getRecommend1());
            rv_type1.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            rv_type1.setAdapter(rvAdapter_type1);
            rvAdapter_type1.setOnItemClickListener(this);

            rvAdapter_type2 = new RVRecommendAdapter(mActivity, mNovel.getRecommend2());
            rv_type2.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            rv_type2.setAdapter(rvAdapter_type2);
            rvAdapter_type2.setOnItemClickListener(this);
            if (mNovel.getOthers() != null) {
                rvAdapter_others = new RVRecommendAdapter(mActivity, mNovel.getOthers());
                rv_others.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                rv_others.setAdapter(rvAdapter_others);
                rvAdapter_others.setOnItemClickListener(this);
            }
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        AppConfig.sendMessage(1, BottomSheetView.this, 1, 0, null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            ;
        }
    }

    /**
     * 按返回键的时候检测是否已关闭详情页
     *
     * @return
     */
    public boolean isClose() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
        }
        return true;
    }

    @Override
    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1) {
            case 1:
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case 2:
                initNovelDetail();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_catalog:
                Intent intent = new Intent(mActivity, NovelsActivity.class);
                intent.putExtra("data", mNovel);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_lastupd:
                Intent intent2 = new Intent(mActivity, NovelsActivity.class);
                intent2.putExtra("data", mNovel);
                intent2.putExtra("index", -2);
                mActivity.startActivity(intent2);
                break;
            case R.id.tv_addbookcase:
                new RemoteProgressDialog(mActivity, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String msg = ParseNovelDetail.addBookCase(mNovel.getnAddBookCaseUrl());
//                            AppConfig.sendMessage(1, BottomSheetView.this, 2, 0, null);
                            AppConfig.sendMessage(0, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConfig.sendMessage(0, e.getMessage());
                        }
                    }
                }).start();
                break;
            case R.id.tv_uservote:
                new RemoteProgressDialog(mActivity, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String msg = ParseNovelDetail.addBookCase(mNovel.getnUserVote());
//                            AppConfig.sendMessage(1, BottomSheetView.this, 2, 0, null);
                            AppConfig.sendMessage(0, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConfig.sendMessage(0, e.getMessage());
                        }
                    }
                }).start();
                break;
        }
    }

    /**
     * 推荐的点击
     *
     * @param view
     * @param novel
     * @param position
     */
    @Override
    public void onItemClickListener(View parentView, View view, Novels novel, int position) {
        switch (parentView.getId()) {
            case R.id.rv_type1:
//                Toast.makeText(mActivity, "1:" + novel.getnTitle() + novel.getnDetailsUrl(), Toast.LENGTH_SHORT).show();
                isClose();
                syncNovelFetail(novel.getnDetailsUrl());
                break;
            case R.id.rv_type2:
//                Toast.makeText(mActivity, "2:" + novel.getnTitle() + novel.getnCoverImgUrl(), Toast.LENGTH_SHORT).show();
                isClose();
                syncNovelFetail(novel.getnDetailsUrl());
                break;
            case R.id.rv_others:
//                Toast.makeText(mActivity, "others:" + novel.getnTitle() + novel.getnCoverImgUrl(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 推荐部分的长按点击
     *
     * @param view
     * @param novel
     * @param position
     */
    @Override
    public void onItemLongClickListener(View parentView, View view, Novels novel, int position) {

    }
}
