package com.jingchen.pulltorefresh;

import android.app.Application;
import android.util.Log;

/**
 * Created by maoting on 2016/5/20.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()));
    }

    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private final Thread.UncaughtExceptionHandler mDefautl;

        public MyUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
            mDefautl = handler;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            Log.e("Application", throwable.getMessage(), throwable);
            throwable.printStackTrace();

            if(mDefautl != null) {
                mDefautl.uncaughtException(thread, throwable);
            }
        }
    }
}
