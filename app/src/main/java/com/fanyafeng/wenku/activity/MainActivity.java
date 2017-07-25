package com.fanyafeng.wenku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.wux.wenku.BaseActivity;
import com.wux.wenku.R;
import com.wux.wenku.activity.LoginActivity;
import com.wux.wenku.activity.StaggeredGridLayoutActivity;
import com.wux.wenku.activity.HomeActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        title = "测试基类";
        isSetNavigationIcon = false;

        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnSnackbar:
                startActivity(new Intent(this, SnackBarActivity.class));
                break;
            case R.id.btnTestFloat:
                startActivity(new Intent(this, FloatingActionButtonActivity.class));
                break;
            case R.id.btnTestEditText:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnTestDrawer:
                startActivity(new Intent(this, DrawerLayoutActivity.class));
                break;
            case R.id.btnTestTabLayout:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.btnTestToolbarEnter:
                startActivity(new Intent(this, ToolbarEnterAlwaysActivity.class));
                break;
            case R.id.btnTestToolbarExit:
                startActivity(new Intent(this, ToolbarExitUntilCollapsedActivity.class));
                break;
            case R.id.btnTestStagger:
                startActivity(new Intent(this, StaggeredGridLayoutActivity.class));
                break;
            case R.id.btnTestBottomSheet:
                startActivity(new Intent(this, BottomSheetActivity.class));
                break;
            case R.id.btnToolbarViewpager:
                startActivity(new Intent(this, StaggeredActivity.class));
                break;
            case R.id.btnTestV7:
                startActivity(new Intent(this, V7WidgetActivity.class));
                break;
            case R.id.btnTestSwitchList:
                startActivity(new Intent(this, SwitchListActivity.class));
                break;
            case R.id.btnNest:
                startActivity(new Intent(this, NestActivity.class));
                break;
            case R.id.btnTestFocusToBig:
                startActivity(new Intent(this, FocusActivity.class));
                break;
        }
    }
}
