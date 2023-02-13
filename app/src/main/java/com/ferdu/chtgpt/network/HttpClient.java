package com.ferdu.chtgpt.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class HttpClient {
    private static final long REQUEST_TIMEOUT_DURATION = 120000;
    // private static final long REQUEST_TIMEOUT_DURATION = 40000;
    private static Retrofit retrofit;
    // private static HttpClient instance;
    private static Context context;
    private static OkHttpClient okHttpClient;
    //  private  Requests api;

    //   public HttpClient(Context context) {

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(REQUEST_TIMEOUT_DURATION, TimeUnit.MILLISECONDS)
//                .readTimeout(REQUEST_TIMEOUT_DURATION, TimeUnit.MILLISECONDS)
//                .addInterceptor(new MyInterceptor(context))
//                .build();
   //     HttpClient.context = context;
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        api = retrofit.create(Requests.class);
     //}

    private static final String BASE_URL = "https://api.openai.com";

    public static void setContext(Context context) {
        HttpClient.context = context;
    }
    public static Retrofit getRetrofit() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT_DURATION, TimeUnit.MILLISECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION, TimeUnit.MILLISECONDS)
                .addInterceptor(new MyInterceptor(context))
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(LenientGsonConverterFactory.create())
                    .build();

            // retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }


}
