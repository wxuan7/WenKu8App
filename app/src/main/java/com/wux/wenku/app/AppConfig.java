package com.wux.wenku.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wux.wenku.util.CrashHandler;

import java.util.Map;

/**
 * Created by 365rili on 16/6/14.
 */
public class AppConfig extends Application {
    public static Map<String, String> _Cookie ;
    public static String _UserName = "kuien";
    public static String _Pwd = "kuien";
    public static String _LoginURL = "http://www.wenku8.com/login.php?do=submit";//登录url
    public static MainHandler mainHandler = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);
        mainHandler = new MainHandler(this);
        /* 初始化日志 */
//        ConfigureLog4J.configure();
    }
    public static void sendMessage(int what, Object obj) {
        if (mainHandler != null) {
            mainHandler.sendMessage(mainHandler.obtainMessage(what, obj));
        }
    }

    public static void sendMessage(int what, Object obj, int arg1, int arg2, Bundle data) {
        if (mainHandler != null) {
            Message msg = Message.obtain(mainHandler, what, arg1, arg2, obj);
            msg.setData(data);
            msg.sendToTarget();
        }
    }
    public static class MainHandler extends Handler {
        private Context context = null;

        public MainHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    if (msg.obj != null && msg.obj instanceof OnHandlerCallBack) {
                        ((OnHandlerCallBack) msg.obj)
                                .handlercallback(msg.arg1, msg.arg2, msg.getData());
                    }
                    break;
            }
        }
    }

    public interface OnHandlerCallBack {
        public void handlercallback(int arg1, int arg2, Bundle data);
    }
}
