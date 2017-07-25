package com.wux.wenku.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.wux.wenku.R;

/**
 * Created by WuX on 2017/5/30.
 */

public class RemoteProgressDialog extends Thread {
    private CircleProgressView mCircleProgressView;
    private Dialog dialog;
    public RemoteProgressDialog( Activity activity,Runnable run){
        super(run);
//        dialog = new Dialog(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.progress_dialog,null);
//        AlertDialog.Builder b = new AlertDialog.Builder(activity, R.style.NoBackGroundDialog);
//        dialog = b.create();

        dialog = new Dialog(activity,R.style.DialogStyle);
//        WindowManager.LayoutParams wmParams = dialog.getWindow().getAttributes();
        //wmParams.format = PixelFormat.TRANSPARENT;  内容全透明
//        wmParams.format = PixelFormat.TRANSLUCENT;  //内容半透明
//        wmParams.alpha=0.1f;    //调节透明度，1.0最大
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
        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();
    }
}
