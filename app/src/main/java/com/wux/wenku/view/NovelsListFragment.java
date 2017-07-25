package com.wux.wenku.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
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
import com.wux.wenku.BaseFragment;
import com.wux.wenku.R;
import com.wux.wenku.activity.HomeActivity;
import com.wux.wenku.activity.HomeMenuActivity;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Novels;
import com.wux.wenku.parse.ParseNovelList;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by WuX on 2017/4/16.
 */

public class NovelsListFragment extends BaseFragment implements AppConfig.OnHandlerCallBack {
    private View rootView = null;
    private HomeMenuActivity mContext;
    private MaterialListView mListView;
    private String _Url = "";
    private ArrayList<Novels> nlist = new ArrayList<Novels>();
    private int page = 1;
    private RefreshLayout refreshLayout;
    private BottomSheetView bottomSheetView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (HomeMenuActivity) getActivity();
        bottomSheetView = new BottomSheetView(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_novelslist_layout_refulsh, container, false);
            _Url = getArguments().getString("url");
            initView();
//            initData();
            if(0==getArguments().getInt("pos")){
                firstLoadData();
            }
        }
        return rootView;
    }


    private void initView() {
        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) rootView.findViewById(R.id.material_listview);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);

        final ImageView emptyView = (ImageView) rootView.findViewById(R.id.imageView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(emptyView);
        Picasso.with(mContext)
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
                Toast.makeText(mContext, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, final int position) {

                initNovelDetail(nlist.get(position).getnDetailsUrl());
                Log.d("CARD_TYPE", "" + card.getTag());
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
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
                            AppConfig.sendMessage(1, NovelsListFragment.this, 1, 1, data);
//
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConfig.sendMessage(0, e.getMessage());
                            refreshlayout.finishRefresh();
                        } finally {
                        }
                    }
                }).start();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ArrayList<Novels> list = ParseNovelList.parseNovelListList(_Url, page);
                                page++;
//                            nlist.addAll(list);
                                Bundle data = new Bundle();
                                data.putSerializable("data", list);
                                AppConfig.sendMessage(1, NovelsListFragment.this, 1, 2, data);
//
                            } catch (Exception e) {
                                e.printStackTrace();
                                AppConfig.sendMessage(0, e.getMessage());
                                refreshlayout.finishLoadmore();
                            } finally {
                            }
                        }
                    }).start();
                }
            }
        });
        // 底部上弹的小说详情
        bottomSheetView.initView(rootView);
//        bottomSheetView.initNest();
    }

    @Override
    public void reflush() {
        refreshLayout.autoRefresh();
    }

    private void initData() {
//        List<Card> cards = new ArrayList<>();
//        for (int i = 0; i < 35; i++) {
//            cards.add(getRandomCard(2));
//        }
//        mListView.getAdapter().addAll(cards);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Novels> list = ParseNovelList.parseNovelListList(_Url, page);
                    page++;
                    nlist.addAll(list);
                    Bundle data = new Bundle();
                    data.putSerializable("data", list);
                    AppConfig.sendMessage(1, NovelsListFragment.this, 1, 0, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
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
//                mContext.initNovelDetail((Novels) data.getSerializable("data"));
                break;
        }
    }

    private Card getRandomCard(Novels nove) {
        final CardProvider provider = new Card.Builder(mContext)
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
                .addAction(R.id.left_text_button, new TextViewAction(mContext)
                        .setText("left")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                card.getProvider().setTitle("CHANGED ON RUNTIME");
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(mContext)
                        .setText("right")
                        .setTextResourceColor(R.color.orange_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(mContext, "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
                                card.dismiss();
                            }
                        }));

        return provider.endConfig().build();
    }
    /**
     * 展示小说的封面，最近更新，目录链接，简介
     * <p>
     * 需要增加同类作品推荐，同作者作品展示
     */
    private void initNovelDetail(String url) {
        bottomSheetView.syncNovelFetail(url);
//        bottomSheetView.initNovelDetail(novels);
    }

    public boolean onBackPressed() {
        if (!bottomSheetView.isClose()) {
            return true;
        }
       return super.onBackPressed();
    }
}
