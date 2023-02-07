package com.ferdu.chtgpt.util.update.impl;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ferdu.chtgpt.util.update.IDownloadCallback;
import com.ferdu.chtgpt.util.update.INetCallback;
import com.ferdu.chtgpt.util.update.INetManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpNetManager implements INetManager {
    private static final String TAG = "OkHttpNetManager";

    private static final OkHttpClient sOkHttpClient;

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        sOkHttpClient = builder.build();
    }

    @Override
    public void get(String url, final INetCallback netCallback, Object tag) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().tag(tag).build();

        Call call = sOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallback.onFailed(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallback.onSuccess(string);
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallback.onFailed(e);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void download(String url, final File targetFile, final IDownloadCallback downloadCallback, Object tag) {
        if (!targetFile.exists()) {
            Objects.requireNonNull(targetFile.getParentFile()).mkdirs();
        }

        //发起请求
        Request.Builder builder = new Request.Builder();
        final Request request = builder.url(url).get().tag(tag).build();
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadCallback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                InputStream is = null;
                OutputStream os = null;

                try {
                    final long totalLen = Objects.requireNonNull(response.body()).contentLength();

                    is = Objects.requireNonNull(response.body()).byteStream();
                    os = new FileOutputStream(targetFile);

                    byte[] buffer = new byte[8 * 1024];
                    int bufferLen;
                    int curLen = 0;
                    while (!call.isCanceled() && (bufferLen = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bufferLen);
                        os.flush();
                        curLen += bufferLen;

                        final int finalCurLen = curLen;
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                downloadCallback.progress((int) (finalCurLen * 1.0f / totalLen * 100));
                            }
                        });
                    }

                    if (call.isCanceled()){
                        return;
                    }

                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            downloadCallback.onSuccess(targetFile);
                        }
                    });

                } catch (final FileNotFoundException e) {
                    if (call.isCanceled()){
                        return;
                    }
                    e.printStackTrace();
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            downloadCallback.onFailure(e);
                        }
                    });
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                }
            }
        });
    }
    @Override
    public void cancel(Object tag) {
        List<Call> queuedCalls = sOkHttpClient.dispatcher().queuedCalls();
        if (queuedCalls != null) {
            for (Call call : queuedCalls) {
                if (tag.equals(call.request().tag())) {
                    Log.d("cancel", "find call = " + tag);
                    call.cancel();
                }
            }
        }

        List<Call> runningCalls = sOkHttpClient.dispatcher().runningCalls();
        if (runningCalls != null) {
            for (Call call : runningCalls) {
                if (tag.equals(call.request().tag())) {
                    Log.d("cancel", "find call = " + tag);
                    call.cancel();
                }
            }
        }
    }
}
