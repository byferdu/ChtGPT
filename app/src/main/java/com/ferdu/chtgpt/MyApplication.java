package com.ferdu.chtgpt;

import android.app.Application;

import java.io.IOException;

import cn.leancloud.LeanCloud;
import cn.leancloud.core.LeanService;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        setRxJavaErrorHandler();
        // 配置 SDK 储存
        LeanCloud.setServer(LeanService.API, "https://8h0p0cot.lc-cn-n1-shared.com");
        LeanCloud.initialize(this, "8H0P0COtjawleOi9ZaNbiAWD-gzGzoHsz", "oQDSoDWR7dZFk5SWmw52ySft");

    }

    private void setRxJavaErrorHandler() {
        if (RxJavaPlugins.getErrorHandler() != null || RxJavaPlugins.isLockdown()) {
           // LogUtils.d("App", "setRxJavaErrorHandler getErrorHandler()!=null||isLockdown()");
            return;
        }
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) {
                if (e instanceof UndeliverableException) {
                    e = e.getCause();
                    //LogUtils.d("App", "setRxJavaErrorHandler UndeliverableException=" + e);
                    return;
                } else if ((e instanceof IOException)) {
                    // fine, irrelevant network problem or API that throws on cancellation
                    return;
                } else if (e instanceof InterruptedException) {
                    // fine, some blocking code was interrupted by a dispose call
                    return;
                } else if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                    // that's likely a bug in the application
                    Thread.UncaughtExceptionHandler uncaughtExceptionHandler =
                            Thread.currentThread().getUncaughtExceptionHandler();
                    if (uncaughtExceptionHandler != null) {
                        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
                    }
                    return;
                } else if (e instanceof IllegalStateException) {
                    // that's a bug in RxJava or in a custom operator
                    Thread.UncaughtExceptionHandler uncaughtExceptionHandler =
                            Thread.currentThread().getUncaughtExceptionHandler();
                    if (uncaughtExceptionHandler != null) {
                        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
                    }
                    return;
                }
               // LogUtils.d("App", "setRxJavaErrorHandler unknown exception=" + e);
            }
        });
    }

}
