package com.wux.wenku.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wux.wenku.R;
import com.wux.wenku.BaseActivity;

import java.util.List;

public class StaggeredListLayoutActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private ListView lvCatalog;
    private CatalogAdpter catalogAdpter;
    private List<String> stringList;

    private SimpleDraweeView sdvStaggerTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_list_layout);

        title = getIntent().getStringExtra("title");

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
        lvCatalog = (ListView) findViewById(R.id.lvCatalog);
        lvCatalog.setOnItemClickListener(this);
        catalogAdpter = new CatalogAdpter();
        lvCatalog.setAdapter(catalogAdpter);
//        lvCatalog.setHasFixedSize(true);
        sdvStaggerTop = (SimpleDraweeView) findViewById(R.id.sdvRvItem);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        sdvStaggerTop.setImageURI(Uri.parse("http://img.wenku8.com/image/1/1508/1508s.jpg"));
        ActivityOptions.makeSceneTransitionAnimation(this, sdvStaggerTop, "SimpleDraweeView");


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,position+"",Toast.LENGTH_SHORT).show();
    }

    class CatalogAdpter extends BaseAdapter {
        private String[] catalogs = {"1","2","3","4","2","3","4","2","3","4","2","3","4","2","3","4","2","3","4","2","3","4","2","3","4","2","3","4","2","3","4"};
        @Override
        public int getCount() {
            return catalogs.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (null == view) {
                view = LayoutInflater.from(StaggeredListLayoutActivity.this).inflate(R.layout.item_catalog, null);
            }
            TextView tv_catalog = (TextView) view.findViewById(R.id.tv_catalog);
            tv_catalog.setText(catalogs[position]);
            return view;
        }
    }
}
