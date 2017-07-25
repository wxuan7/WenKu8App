package com.wux.wenku.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.wux.wenku.BaseActivity;
import com.wux.wenku.R;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Novels;
import com.wux.wenku.parse.ParseNovelList;
import com.wux.wenku.view.BottomSheetView;
import com.wux.wenku.view.MaterialListFragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class TypeShowActivity extends BaseActivity implements AppConfig.OnHandlerCallBack, OnRefreshListener, OnLoadmoreListener {
    private MaterialListView mListView;
    private int page = 1;
    private String _Url;
    private ArrayList<Novels> nlist = new ArrayList<Novels>();
    private BottomSheetView bottomSheetView = null;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _Url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        bottomSheetView = new BottomSheetView(this);
        initView();
//        initData();
        refreshLayout.autoRefresh();
    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                mListView.smoothScrollToPosition(0);
            }
        });
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        // 底部上弹的小说详情
        bottomSheetView.initView();
        mListView = (MaterialListView) findViewById(R.id.mlv_type);
        ImageView emptyView = (ImageView) findViewById(R.id.imageView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(emptyView);
        Picasso.with(this)
                .load("http://pic.wenku8.com/pictures/1/1508/69182/84282.jpg")
                .resize(100, 100)
                .centerInside()
                .into(emptyView);

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, final int position) {

                bottomSheetView.syncNovelFetail(nlist.get(position).getnDetailsUrl());
                Log.d("CARD_TYPE", "" + card.getTag());
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
    }

    private void loadNovels(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Novels> list = ParseNovelList.parseSearchNovel(url);
                    Bundle data = new Bundle();
                    data.putSerializable("data", list);
                    AppConfig.sendMessage(1, TypeShowActivity.this, 1, 0, data);
                    AppConfig.sendMessage(0, "刷出" + list.size() + "喽  ( ｡ớ ₃ờ)ھ");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1) {
            case 1:
                int pos = nlist.size();
                ArrayList<Novels> list = (ArrayList) data.getSerializable("data");
                List<Card> cards = new ArrayList<Card>();
                for (Novels nove : list) {
                    cards.add(getRandomCard(nove));
                    nlist.add(nove);
                }
                if (arg2 == 0) {
                    mListView.getAdapter().addAll(cards);
                } else if (arg2 == 1) {
                    mListView.getAdapter().clearAll();
                    refreshLayout.finishRefresh();
//                    materialRefreshLayout.finishRefresh();
                    mListView.getAdapter().addAll(cards);
                } else if (arg2 == 2) {
                    for (Card card : cards) {
                        mListView.getAdapter().add(card);
                    }
                    refreshLayout.finishRefresh();
//                    materialRefreshLayout.finishRefreshLoadMore();
                    mListView.scrollToPosition(pos - 1);
//                    mListView.smoothScrollToPosition(pos);
                }
                mListView.getAdapter().notifyDataSetChanged();
                break;
            case 2:
//                mContext.initNovelDetail((Novels) data.getSerializable("data"));
                break;
        }
    }

    private Card getRandomCard(Novels nove) {
        final CardProvider provider = new Card.Builder(this)
                .setTag("BASIC_IMAGE_BUTTON_CARD")
//                        .setDismissible()
                .withProvider(new CardProvider<>())
                .setLayout(R.layout.material_basic_image_card_layout)
                .setTitle(nove.getnTitle())
                .setTitleGravity(Gravity.END)
                .setDescription(nove.getnContent())
                .setDescriptionGravity(Gravity.END)
                .setDrawable(nove.getnCoverImgUrl())
                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                    @Override
                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                        requestCreator.fit();
                    }
                })
                .addAction(R.id.left_text_button, new TextViewAction(this)
                        .setText("left")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
//                                Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                card.getProvider().setTitle("CHANGED ON RUNTIME");
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(this)
                        .setText("right")
                        .setTextResourceColor(R.color.orange_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
//                                Toast.makeText(mContext, "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
                                card.dismiss();
                            }
                        }));

        return provider.endConfig().build();
    }

    private void initData() {
//        bottomSheetView.initNest();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Novels> list = ParseNovelList.parseNovelListList(_Url, page);
                    page++;
                    nlist.addAll(list);
                    Bundle data = new Bundle();
                    data.putSerializable("data", list);
                    AppConfig.sendMessage(1, TypeShowActivity.this, 1, 0, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetView.isClose()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh(final RefreshLayout refreshLayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    page = 1;
                    ArrayList<Novels> list = ParseNovelList.parseNovelListList(_Url, page);
                    page++;
                    nlist.clear();
//                            nlist.addAll(list);
                    Bundle data = new Bundle();
                    data.putSerializable("data", list);
                    AppConfig.sendMessage(1, TypeShowActivity.this, 1, 1, data);
                    if (list.size() == 0) {
                        AppConfig.sendMessage(0, "没有更多啦╮(๑•́ ₃•̀๑)╭");
                    } else {
                        AppConfig.sendMessage(0, "刷出" + list.size() + "条喽  ( ｡ớ ₃ờ)ھ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
//                    materialRefreshLayout.finishRefresh();
                } finally {
                    refreshLayout.finishRefresh();

                }
            }
        }).start();
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshLayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Novels> list = ParseNovelList.parseNovelListList(_Url, page);
                    page++;
//                            nlist.addAll(list);
                    Bundle data = new Bundle();
                    data.putSerializable("data", list);
                    AppConfig.sendMessage(1, TypeShowActivity.this, 1, 2, data);
//
                    if (list.size() == 0) {
                        AppConfig.sendMessage(0, "没有更多啦╮(๑•́ ₃•̀๑)╭");
                    } else {
                        AppConfig.sendMessage(0, "刷出" + list.size() + "条喽  ( ｡ớ ₃ờ)ھ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
//                    materialRefreshLayout.finishRefreshLoadMore();
                } finally {
                    refreshLayout.finishLoadmore();

                }
            }
        }).start();
    }
}
