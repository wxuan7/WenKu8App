package com.wux.wenku.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wux.wenku.BaseActivity;
import com.wux.wenku.BaseFragment;
import com.wux.wenku.R;
import com.wux.wenku.adapter.RVAdapter;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Chapters;
import com.wux.wenku.model.Novels;
import com.wux.wenku.parse.ParseArticle;
import com.wux.wenku.parse.ParseChapteresList;
import com.wux.wenku.ui.RemoteProgressDialog;
import com.wux.wenku.ui.ScrollAwareFABBehavior;
import com.wux.wenku.view.MaterialListFragment;
import com.wux.wenku.view.TagFragment;

import java.util.ArrayList;
import java.util.List;

public class NovelsBookMarkActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, RVAdapter.OnItemClickListener, AppConfig.OnHandlerCallBack,ViewPager.OnPageChangeListener,TagFragment.OnFragmentInteractionListener {
    private TabLayout layoutTab;
    private ViewPager viewpagerTab;

    private DrawerLayout mDrawer = null;//抽屉
    private RecyclerView rv_catalog = null;//目录
    private TextView tv_text = null;//正文
    private TextView tv_title = null;//章节
    private SimpleDraweeView sdvStaggerTop;//封面
    private ScrollView sv_text = null;
    private RVAdapter adapter = null;
    private Novels mNovels = null;
    private List<String> titles_list = new ArrayList<>();
    private int index = -1;
    private ScrollAwareFABBehavior behavior;

    private MaterialRefreshLayout materialRefreshLayout;
    private FloatingActionButton fb1;
    private List<BaseFragment> fragmentList;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novels_book_mark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNovels = (Novels) getIntent().getSerializableExtra("data");
        index = getIntent().getIntExtra("index", -1);


        title = mNovels.getnTitle();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        initView();
        fabGone();
        loadChapteresList();
        initData();
    }

    private void initView() {
        layoutTab = (TabLayout) findViewById(R.id.layoutTab);
        viewpagerTab = (ViewPager) findViewById(R.id.viewpagerTab);
        fragmentList = new ArrayList<>();
//        TagFragment tagFragment = new TagFragment();
        fragmentList.add(TagFragment.newInstance());
        fragmentList.add(TagFragment.newInstance());

        fb1 = (FloatingActionButton) findViewById(R.id.fab1);
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "收藏", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        behavior = new ScrollAwareFABBehavior(NovelsBookMarkActivity.this, null);
        sv_text = (ScrollView) findViewById(R.id.sv_text);
//        sdvStaggerTop = (SimpleDraweeView) findViewById(R.id.sdvRvItem);
//        sdvStaggerTop.setImageURI(Uri.parse(mNovels.getnCoverImgUrl()));
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        rv_catalog = (RecyclerView) findViewById(R.id.rvStagger);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_title = (TextView) findViewById(R.id.tv_title);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                lastChapter();
            }

            @Override
            public void onfinish() {
//                Toast.makeText(NovelsActivity.this, "finish", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                nextChapter(materialRefreshLayout);

            }
        });
        tv_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    behavior.animateIn(fb1);
                    fabGone();
                }

                return false;
            }
        });
    }
    private void initData() {
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), new String[]{"目录","书签"}, fragmentList);
        viewpagerTab.setAdapter(myViewPagerAdapter);
        viewpagerTab.setOffscreenPageLimit(2);
        viewpagerTab.addOnPageChangeListener(this);
//        代码中优先级高于xml
//        layoutTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        layoutTab.setupWithViewPager(viewpagerTab);
        layoutTab.setTabsFromPagerAdapter(myViewPagerAdapter);
    }

    private void fabGone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    AppConfig.sendMessage(1, NovelsBookMarkActivity.this, 3, 0, null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadChapteresList() {
        new RemoteProgressDialog(this,new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Chapters> list = ParseChapteresList.parseChapteresList(mNovels.getnCatalogUrl());
                    for (Chapters ch : list) {
                        mNovels.addChapterse(ch);
                        titles_list.add(ch.getChapterName());
                    }
                    AppConfig.sendMessage(1, NovelsBookMarkActivity.this, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
                }
            }
        }).start();
    }

    private void initCatalog() {
        adapter = new RVAdapter(this, titles_list, 2);
//        rv_catalog.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//        rv_catalog.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        mDrawer.openDrawer(GravityCompat.START);
        if (index > -1) {
//            rv_catalog.smoothScrollToPosition(index);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClickListener(View view, String string, final int position) {
        Toast.makeText(NovelsBookMarkActivity.this, string, Toast.LENGTH_SHORT).show();
        String url = mNovels.getChapterses().get(position).getUrl();
        if (null == url || url.length() == 0) {
            return;
        }
        index = position;
        initText();
    }

    private void initText() {
        mDrawer.closeDrawer(GravityCompat.START);
        final Chapters chapters = mNovels.getChapterses().get(index);
//        if (null == chapters.getText() || "".equals(chapters.getText())) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String text = ParseArticle.parseArticle(mNovels.getnTitle(), chapters.getChapterName(), chapters.getUrl());
                    chapters.setText(text);
                    Bundle data = new Bundle();
                    data.putString("text", text.replace(" ", "\n"));
                    AppConfig.sendMessage(1, NovelsBookMarkActivity.this, 2, 0, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
                }
            }
        }).start();
//        } else {
//            mDrawer.closeDrawer(GravityCompat.START);
//            sv_text.smoothScrollTo(0, 0);
//            tv_text.setText(chapters.getText());
//            title = chapters.getChapterName();
//            tv_title.setText(chapters.getChapterName());
//            if (index > -1) {
//                rv_catalog.smoothScrollToPosition(index);
//            }
//        }
    }

    @Override
    public void onItemLongClickListener(View view, String string, int position) {

    }

    @Override
    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1) {
            case 1:
                initCatalog();// 解析目录数据后，开始判断载入的章节；
                if (index == -1) {
                    for (int i = 0; i < mNovels.getChapterses().size(); i++) {
                        Chapters chapters = mNovels.getChapterses().get(i);
                        if (null != chapters.getUrl() && !"".equals(chapters.getUrl())) {
                            index = i;
                            break;
                        }
                    }
                } else if (index == -2) {
                    for (int i = mNovels.getChapterses().size() - 1; i > -1; i--) {
                        Chapters chapters = mNovels.getChapterses().get(i);
                        if (null != chapters.getUrl() && !"".equals(chapters.getUrl())) {
                            index = i;
                            break;
                        }
                    }
                }
                initText();
                break;
            case 2:
                mDrawer.closeDrawer(GravityCompat.START);
//                sv_text.scrollTo(0,0);
                sv_text.smoothScrollTo(0, 0);
                Chapters chapters = mNovels.getChapterses().get(index);
                tv_text.setText(chapters.getText());
                title = chapters.getChapterName();
                tv_title.setText(chapters.getChapterName());
                if (index > -1) {
//                    rv_catalog.smoothScrollToPosition(index);
                }
                break;
            case 3:
                behavior.animateOut(fb1);
                break;
        }
    }

    /**
     * 下一章节
     */
    private void nextChapter(final View view) {
        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    if (index < mNovels.getChapterses().size() - 1) {
                        for (int i = index + 1; i < mNovels.getChapterses().size(); i++) {
                            Chapters chapters = mNovels.getChapterses().get(i);
                            if (null != chapters.getUrl() && !"".equals(chapters.getUrl())) {
                                index = i;
//                                if (null == chapters.getText() || "".equals(chapters.getText())) {
                                try {
                                    String text = ParseArticle.parseArticle(mNovels.getnTitle(), chapters.getChapterName(), chapters.getUrl());
                                    chapters.setText(text);
                                    AppConfig.sendMessage(1, NovelsBookMarkActivity.this, 2, 0, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    AppConfig.sendMessage(0, e.getMessage());
                                }
//                                } else {
//                                    Thread.sleep(1000);
//                                    AppConfig.sendMessage(1, NovelsActivity.this, 2, 0, null);
//                                }
                                break;
                            }
                        }
                    } else {
                        Snackbar.make(view, "已经是最新章节", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
//                        AppConfig.sendMessage(0, "已经是最新章节");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        })).start();
    }

    /**
     * 上一章节
     */
    private void lastChapter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (index > 0) {
                        for (int i = index - 1; i > -1; i--) {
                            Chapters chapters = mNovels.getChapterses().get(i);
                            if (null != chapters.getUrl() && !"".equals(chapters.getUrl())) {
                                index = i;
//                                if (null == chapters.getText() || "".equals(chapters.getText())) {
                                try {
                                    String text = ParseArticle.parseArticle(mNovels.getnTitle(), chapters.getChapterName(), chapters.getUrl());
                                    chapters.setText(text);
                                    AppConfig.sendMessage(1, NovelsBookMarkActivity.this, 2, 0, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    AppConfig.sendMessage(0, e.getMessage());
                                }
//                                } else {
//                                    Thread.sleep(1000);
//                                    AppConfig.sendMessage(1, NovelsActivity.this, 2, 0, null);
//                                }
                                break;
                            }
                        }
                    } else {
                        AppConfig.sendMessage(0, "已经到开头啦");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    materialRefreshLayout.finishRefresh();
                }
            }
        }).start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        private String[] titleList;
        private List<BaseFragment> fragmentList;

        public MyViewPagerAdapter(FragmentManager fm, String[] titleList, List<BaseFragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}
