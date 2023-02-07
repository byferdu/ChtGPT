package com.ferdu.chtgpt;

import android.app.Application;

import java.io.IOException;

import cn.leancloud.LCLogger;
import cn.leancloud.LeanCloud;
import cn.leancloud.core.LeanService;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //开启调试日志
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);
        // 初始化应用信息
       // LeanCloud.initialize(this,"Gvv2k8PugDTmYOCfuK8tiWd8-gzGzoHsz", "dpwAo94n81jPsHVxaWwdxJVu", "https://gvv2k8pu.lc-cn-n1-shared.com");
        //保存 Installation
        setRxJavaErrorHandler();
        // 配置 SDK 储存
        LeanCloud.setServer(LeanService.API, "https://8h0p0cot.lc-cn-n1-shared.com");
        // 配置 SDK 云引擎（用于访问云函数，使用 API 自定义域名，而非云引擎自定义域名）
      //  AVOSCloud.setServer(AVOSService.ENGINE, "https://xxx.example.com");
        // 配置 SDK 推送
       // LeanCloud.setServer(LeanService.PUSH, "https://8h0p0cot.lc-cn-n1-shared.com");
        // 配置 SDK 即时通讯
       // AVOSCloud.setServer(AVOSService.RTM, "https://xxx.example.com");
        // 提供 this、App ID 和 App Key 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.AVOSCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        LeanCloud.initialize(this, "8H0P0COtjawleOi9ZaNbiAWD-gzGzoHsz", "oQDSoDWR7dZFk5SWmw52ySft");
        // 注意这里千万不要调用 cn.leancloud.core.LeanCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        //LeanCloud.initializeSecurely(this, "8H0P0COtjawleOi9ZaNbiAWD-gzGzoHsz", "https://8h0p0cot.lc-cn-n1-shared.com");
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
