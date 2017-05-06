package com.wux.wenku.util;


import android.content.Context;
import android.util.Log;

/**
 * Created by WuX on 2017/4/27.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    private static CrashHandler INSTANCE ;
    private Context context;

    //1.私有化构造方法
    private CrashHandler(){

    }

    public static synchronized CrashHandler getInstance(){
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    public void init(Context context){
        this.context = context;
    }


    public void uncaughtException(Thread arg0, Throwable arg1) {
        System.out.println("程序挂掉了 ");
        // 在此可以把用户手机的一些信息以及异常信息捕获并上传,
        //干掉当前的程序
        Log.e("动漫化作品",arg1.getMessage());
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
