package com.ferdu.chtgpt.network;

import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.RequestModel;
import com.ferdu.chtgpt.models.ResModel;
import com.ferdu.chtgpt.models.ResponseModel;
import com.ferdu.chtgpt.models.TransModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Requests {
    @Streaming
    @POST("/v1/completions")
    @Headers({"Content-Type:application/json"})
    Call<ResponseModel> getTex(@Header("Authorization") String token, @Body ReqModel reqModel);
    @Streaming
    @POST("/v1/chat/completions")
    @Headers({"Content-Type:application/json"})
    Call<ResModel> chatCompletions(@Header("Authorization") String token, @Body RequestModel reqModel);


    @GET
    Call<TransModel> getTransText(@Url String url, @Query("msg")String msg,@Query("type")int type);
}
