package com.wux.wenku.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListAdapter;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.wux.wenku.BaseActivity;
import com.wux.wenku.R;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Novels;
import com.wux.wenku.parse.ParseNovelList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class SearchActivity extends BaseActivity implements AppConfig.OnHandlerCallBack {
    private final String searchUrl = "http://www.wenku8.com/modules/article/search" +
            ".php?searchtype=articlename&searchkey=";
    private final String searchUrl2 = "http://www.wenku8.com/wap/article/search" +
            ".php?searchtype=articlename&searchkey=";
    private EditText et_search;
    private MaterialListView mlv_search;
    private MaterialListAdapter adapter;
    private List<Novels> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        mlv_search = (MaterialListView) findViewById(R.id.mlv_search);
        mlv_search.setItemAnimator(new SlideInLeftAnimator());
        mlv_search.getItemAnimator().setAddDuration(300);
        mlv_search.getItemAnimator().setRemoveDuration(300);
        Button bt_search = (Button) findViewById(R.id.bt_search);
        bt_search.setOnClickListener(this);
        adapter = mlv_search.getAdapter();
        emptyList();
    }

    private void emptyList() {
        final ImageView emptyView = new ImageView(this);//(ImageView) findViewById(R.id.imageView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mlv_search.setEmptyView(emptyView);
        Picasso.with(this)
                .load("http://pic.wenku8.com/pictures/1/1508/69182/84282.jpg")
                .resize(100, 100)
                .centerInside()
                .into(emptyView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url = searchUrl + URLEncoder.encode(et_search.getText()
                                    .toString(), "gbk") + "&charset=gbk";
                            ArrayList<Novels> list = ParseNovelList.parseSearchNovel(url);
                            Bundle data = new Bundle();
                            data.putSerializable("data", list);
                            AppConfig.sendMessage(1, SearchActivity.this, 1, 0, data);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
        }
        super.onClick(v);
    }

    private String toGBK(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] b = str.getBytes("GBK");
            for (byte by : b) {
                sb.append("%" + Integer.toHexString((by & 0xff)).toUpperCase());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
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
                });

        return provider.endConfig().build();
    }

    @Override
    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1) {
            case 1:
                ArrayList<Novels> list = (ArrayList) data.getSerializable("data");
                List<Card> cards = new ArrayList<Card>();
                for (Novels nove : list) {
                    cards.add(getRandomCard(nove));
                    mList.add(nove);
                }

                adapter.clearAll();
                adapter.notifyDataSetChanged();

                adapter.addAll(cards);
                Toast.makeText(this, cards.size() + "相关作品", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                break;
        }
    }
}
