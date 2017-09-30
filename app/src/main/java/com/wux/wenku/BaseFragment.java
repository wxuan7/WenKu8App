package com.wux.wenku;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by WuX on 2017/4/16.
 */

public class BaseFragment extends Fragment {
    private Context mContext;
    private boolean isFirst = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    public void firstLoadData(){
        if(isFirst){
            isFirst = false;
            reflush();
        }
    }

    /**
     * 刷新
     */
    public void reflush(){

    }

    /**
     * 返回方法
     * @return
     */
    public boolean onBackPressed(){
        return false;
    }
}
