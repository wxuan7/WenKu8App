package com.wux.wenku.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;


import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.wux.wenku.R;
import com.wux.wenku.adapter.PageLayoutAdapter;

import xiao.free.horizontalrefreshlayout.HorizontalRefreshLayout;
import xiao.free.horizontalrefreshlayout.RefreshCallBack;
import xiao.free.horizontalrefreshlayout.refreshhead.NiceRefreshHeader;

public class PageLayoutActivity extends AppCompatActivity implements RefreshCallBack {
    private HorizontalRefreshLayout refreshLayout;
    protected RecyclerViewPager mRecyclerView;
    private PageLayoutAdapter mLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_layout);

        refreshLayout = (HorizontalRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setRefreshCallback(this);
        refreshLayout.setRefreshHeader(new NiceRefreshHeader(this), HorizontalRefreshLayout.LEFT);
        refreshLayout.setRefreshHeader(new NiceRefreshHeader(this), HorizontalRefreshLayout.RIGHT);

        mRecyclerView = (RecyclerViewPager) findViewById(R.id.viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layout);
        mLayoutAdapter = new PageLayoutAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(mLayoutAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);

        mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                int size = mLayoutAdapter.getItemCount();
                if (size > 1 && newPosition == size - 1) {
                    //mLayoutAdapter.getMore();
                    //refreshLayout.startAutoRefresh(HorizontalRefreshLayout.RIGHT);
                }
            }
        });

        //refreshLayout.startAutoRefresh(HorizontalRefreshLayout.LEFT);
    }

    @Override
    public void onLeftRefreshing() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.onRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onRightRefreshing() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutAdapter.getMore();
                refreshLayout.onRefreshComplete();
            }
        }, 2000);
    }

}
