package com.ferdu.chtgpt.network;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class MyInterceptor implements Interceptor {
    private final Context context;

    public MyInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            return chain.proceed(chain.request());
        } catch (SocketTimeoutException e) {
            // Show a toast message
            Toast.makeText(context, "请求超时😕", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
