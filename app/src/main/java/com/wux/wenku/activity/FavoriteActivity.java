package com.wux.wenku.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.OnDismissCallback;
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
import com.wux.wenku.parse.ParseBookCaseList;
import com.wux.wenku.parse.ParseNovelDetail;
import com.wux.wenku.ui.RemoteProgressDialog;
import com.wux.wenku.view.BottomSheetView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class FavoriteActivity extends BaseActivity implements AppConfig.OnHandlerCallBack {
    private String bookCaseUrl = "";
    private List<Novels> nList = new ArrayList<>();
    private int page = 0;
    private RefreshLayout refreshLayout;
    private MaterialListView mListView;
    private BottomSheetView bottomSheetView = null;
    private String[] bookCaseArray = null;
    private Spinner sp_bookcase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isSetNavigationIcon = true;
        bookCaseUrl = getResources().getString(R.string.bookcase);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        bookCaseArray = getResources().getStringArray(R.array.boocasename);
        bottomSheetView = new BottomSheetView(this);
        initView();
//        refreshLayout.autoRefresh();
    }

    private void initView() {

//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.simple_spinner_item, bookCaseArray);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
//        sp_bookcase .setAdapter(adapter);

        mListView = (MaterialListView) findViewById(R.id.material_listview);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);

        final ImageView emptyView = (ImageView) findViewById(R.id.imageView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(emptyView);
        Picasso.with(this)
                .load("http://pic.wenku8.com/pictures/1/1508/69182/84282.jpg")
                .resize(100, 100)
                .centerInside()
                .into(emptyView);

        // Fill the array withProvider mock content

        // Set the dismiss listener
        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {
                // Show a toast
//                Toast.makeText(FavoriteActivity.this, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, final int position) {

                initNovelDetail(nList.get(position).getnDetailsUrl());
                Log.d("CARD_TYPE", "" + card.getTag());
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                showDelDialog(position);
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        try {
//                            nList = ParseBookCaseList.parseNovelList(bookCase,page);
//                            AppConfig.sendMessage(1,FavoriteActivity.this,0,0,null);
                            ArrayList<Novels> list = ParseBookCaseList.parseNovelList(bookCaseUrl, sp_bookcase.getSelectedItemPosition(), page);
                            page = 1;
//                            nlist.addAll(list);
                            Bundle data = new Bundle();
                            data.putSerializable("data", list);
                            AppConfig.sendMessage(1, FavoriteActivity.this, 1, 1, data);
                            if (list.size() == 0) {
                                AppConfig.sendMessage(0, "没有更多啦╮(๑•́ ₃•̀๑)╭");
                            } else {
                                AppConfig.sendMessage(0, "刷出" + list.size() + "条喽  ( ｡ớ ₃ờ)ھ");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConfig.sendMessage(0, e.getMessage());
                        } finally {
                            refreshlayout.finishRefresh();
                        }
                    }
                }).start();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            page++;
                            ArrayList<Novels> list = ParseBookCaseList.parseNovelList(bookCaseUrl, sp_bookcase.getSelectedItemPosition(), page);
//                            nlist.addAll(list);
                            Bundle data = new Bundle();
                            data.putSerializable("data", list);
                            AppConfig.sendMessage(1, FavoriteActivity.this, 1, 2, data);
                            if (list.size() == 0) {
                                AppConfig.sendMessage(0, "没有更多啦╮(๑•́ ₃•̀๑)╭");
                            } else {
                                AppConfig.sendMessage(0, "刷出" + list.size() + "条喽  ( ｡ớ ₃ờ)ھ");
                            }
                        } catch (Exception e) {
                            page--;
                            e.printStackTrace();
                            AppConfig.sendMessage(0, e.getMessage());
                        } finally {
                            refreshlayout.finishLoadmore();
                        }
                    }
                }).start();
            }
        });

        bottomSheetView.initView();
        sp_bookcase = (Spinner) findViewById(R.id.sp_bookcase);
        sp_bookcase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListView.getAdapter().clearAll();
                refreshLayout.autoRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*
    这里使用了 android.support.v7.app.AlertDialog.Builder
    可以直接在头部写 import android.support.v7.app.AlertDialog
    那么下面就可以写成 AlertDialog.Builder
    */
    private void showDelDialog(final int pos) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("删除");
        builder.setMessage("确定从书架移除“"+nList.get(pos).getnTitle()+"”吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new RemoteProgressDialog(FavoriteActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            String removeUrl = bookCaseUrl+sp_bookcase.getSelectedItemPosition()+"&delid="+nList.get(pos).getnBookID();
                            boolean msg = ParseBookCaseList.removeBookCase(nList.get(pos).getnBookCaseUrl(),nList.get(pos).getnDetailsUrl());
//                             ParseNovelDetail.addBookCase(mNovel.getnAddBookCaseUrl());
                            if(msg) {
                                AppConfig.sendMessage(1, FavoriteActivity.this, 2, pos, null);
                                AppConfig.sendMessage(0,"成功移除");
                            }else{
                                AppConfig.sendMessage(0,"发生意外，移除失败喽");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConfig.sendMessage(0, e.getMessage());
                        }
                    }
                }).start();
            }
        });
        builder.show();
    }

    private void loadBookCase() throws Exception {
        nList = ParseBookCaseList.parseNovelList(bookCaseUrl, sp_bookcase.getSelectedItemPosition(), page);
    }

    @Override
    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1) {
            case 1:
                int pos = nList.size();
                ArrayList<Novels> list = (ArrayList) data.getSerializable("data");
                List<Card> cards = new ArrayList<Card>();
                for (Novels nove : list) {
                    Card card = getRandomCard(nove);
                    card.setDismissible(true);
                    cards.add(card);
                    nList.add(nove);
                }
                if (arg2 == 0) {
                    mListView.getAdapter().addAll(cards);
                } else if (arg2 == 1) {
                    mListView.getAdapter().clearAll();
//                    refreshLayout.finishRefresh();
                    mListView.getAdapter().addAll(cards);
                } else if (arg2 == 2) {
                    for (Card card : cards) {
                        mListView.getAdapter().add(card);
                    }
                    refreshLayout.finishLoadmore();
                    mListView.scrollToPosition(pos - 1);
//                    mListView.smoothScrollToPosition(pos);
                }
                mListView.getAdapter().notifyDataSetChanged();
                break;
            case 2:
                mListView.getAdapter().remove(mListView.getAdapter().getCard(arg2),true);
                nList.remove(arg2);
                break;
        }
    }

    private void initNovelDetail(String url) {
        bottomSheetView.syncNovelFetail(url);
//        bottomSheetView.initNovelDetail(novels);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetView.isClose()) {
            super.onBackPressed();
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
                                Toast.makeText(FavoriteActivity.this, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                card.getProvider().setTitle("CHANGED ON RUNTIME");
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(this)
                        .setText("right")
                        .setTextResourceColor(R.color.orange_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(FavoriteActivity.this, "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
                                card.dismiss();
                            }
                        }));

        return provider.endConfig().build();
    }
}
