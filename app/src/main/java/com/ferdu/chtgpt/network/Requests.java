package com.ferdu.chtgpt.network;

import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.ResModel;
import com.ferdu.chtgpt.models.ResponseModel2;
import com.ferdu.chtgpt.models.TransModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Requests {
    @POST("/v1/completions")
    @Headers({"Content-Type:application/json","CONNECT_TIMEOUT:300000", "READ_TIMEOUT:300000", "WRITE_TIMEOUT:300000"})
    Call<ResponseModel2> getTex(@Header("Authorization") String token, @Body ReqModel reqModel);

    @POST("/v1/text-davinci-002/completions")
    @Headers({"Content-Type:application/json","CONNECT_TIMEOUT:300000", "READ_TIMEOUT:300000", "WRITE_TIMEOUT:300000"})
    Call<ResModel> getTex2(@Header("Authorization") String token, @Body ReqModel reqModel);


    @GET
    Call<TransModel> getTransText(@Url String url, @Query("msg")String msg,@Query("type")int type);
}
