package com.wux.wenku.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wux.wenku.BaseFragment;
import com.wux.wenku.R;
import com.wux.wenku.menu.DrawerAdapter;
import com.wux.wenku.menu.DrawerItem;
import com.wux.wenku.menu.SimpleItem;
import com.wux.wenku.menu.SpaceItem;
import com.wux.wenku.view.NovelsListFragment;
import com.wux.wenku.view.TagFragment;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeMenuActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, DrawerAdapter.OnItemSelectedListener, TagFragment.OnFragmentInteractionListener {
    private TabLayout layoutTab;
    private ViewPager viewpagerTab;
    private MyViewPagerAdapter myViewPagerAdapter;


    private String[] stringList;//名称
    private String[] urlList;// 链接
    private List<BaseFragment> fragmentList;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CART = 3;
    private static final int POS_LOGOUT = 5;
    private SlidingRootNavBuilder mNav;
    private boolean isFirst = true;
    private String imgUrl = "http://www.wenku8.com/images/noavatar.jpg";
    private int _SELECTEDPOS = 0;//当前浏览的界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        isSetNavigationIcon = false;
        stringList = getResources().getStringArray(R.array.menuname);
        urlList = getResources().getStringArray(R.array.menuurl);
        initView(toolbar);
        initNav(savedInstanceState, toolbar);
        initData();
    }

    private void initView(Toolbar toolbar) {
        layoutTab = (TabLayout) findViewById(R.id.layoutTab);
        viewpagerTab = (ViewPager) findViewById(R.id.viewpagerTab);
        fragmentList = new ArrayList<>();
        for (int i = 0; i < stringList.length; i++) {
            if (i == stringList.length - 1) {
                break;
            }
            NovelsListFragment tabLayoutFragment = new NovelsListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pos", i);
            bundle.putString("flag", "2");
            bundle.putString("url", urlList[i]);
            tabLayoutFragment.setArguments(bundle);
            fragmentList.add(tabLayoutFragment);
        }
//        TagFragment tagFragment = new TagFragment();
        fragmentList.add(TagFragment.newInstance());
    }

    private void initData() {
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), stringList, fragmentList);
        viewpagerTab.setAdapter(myViewPagerAdapter);
        viewpagerTab.setOffscreenPageLimit(6);
        viewpagerTab.addOnPageChangeListener(this);
//        代码中优先级高于xml
//        layoutTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        layoutTab.setupWithViewPager(viewpagerTab);
        layoutTab.setTabsFromPagerAdapter(myViewPagerAdapter);

    }

    private void initNav(Bundle savedInstanceState, Toolbar toolbar) {
        mNav = new SlidingRootNavBuilder(this);
        mNav.withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CART),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
        View menuView = mNav.getMenuView();
        SimpleDraweeView sdv_head = (SimpleDraweeView) menuView.findViewById(R.id.tv_head);
        sdv_head.setImageURI(Uri.parse("http://wx4.sinaimg.cn/mw690/006Ln7Hvgy1fhoupk1ynrj30u00u0dii.jpg"));
//        sdv_head.setImageResource(R.drawable.shio);
        TextView tv_user = (TextView) menuView.findViewById(R.id.tv_user);
//        tv_user.setText(AppConfig._UserName);
        sdv_head.setOnClickListener(this);
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.accent))
                .withSelectedTextTint(color(R.color.accent));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        fragmentList.get(position).firstLoadData();
        _SELECTEDPOS = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 左侧抽屉菜单的点击选项
     *
     * @param position
     */
    @Override
    public void onItemSelected(int position) {
        if (isFirst) {
            isFirst = false;
            return;
        }
        switch (position) {
            case 0:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, PageLayoutActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head:
                Intent loginIntent = new Intent(HomeMenuActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            default:
                break;
        }
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

    @Override
    public void onBackPressed() {
        if (!fragmentList.get(_SELECTEDPOS).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
