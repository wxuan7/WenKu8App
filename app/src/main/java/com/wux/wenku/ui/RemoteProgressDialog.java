package com.wux.wenku.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.wux.wenku.R;
import com.wux.wenku.app.AppConfig;

/**
 * Created by WuX on 2017/5/30.
 */

public class RemoteProgressDialog extends Thread implements AppConfig.OnHandlerCallBack{
    private CircleProgressView mCircleProgressView;
    private Dialog dialog;
    public RemoteProgressDialog( Activity activity,Runnable run){
        super(run);
        View view = activity.getLayoutInflater().inflate(R.layout.progress_dialog,null);
        dialog = new Dialog(activity,R.style.DialogStyle);
        dialog.setContentView(view);
        mCircleProgressView = (CircleProgressView) view.findViewById(R.id.circle_progress);
    }
    @Override
    public synchronized void start() {
        showProgressBar();
        super.start();
    }
    @Override
    public void run() {
        try{
            super.run();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            hideProgressBar();
        }
    }

    public void showProgressBar() {
        mCircleProgressView.setVisibility(View.VISIBLE);
        mCircleProgressView.spin();
        dialog.show();
    }


    public void hideProgressBar() {
        dialog.dismiss();
        AppConfig.sendMessage(1,this,1,0,null);
    }

    @Override
    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1){
            case 1:
                mCircleProgressView.setVisibility(View.GONE);
                mCircleProgressView.stopSpinning();
                break;
        }
    }
}
