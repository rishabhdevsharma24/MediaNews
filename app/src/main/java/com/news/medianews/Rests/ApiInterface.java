package com.news.medianews.Rests;

import com.news.medianews.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<ResponseModel> getLatestNews(@Query("country") String country,@Query("from") String date, @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<ResponseModel> getLatestNewsy(@Query("country") String query, @Query("apiKey") String apiKey);

    @GET("everything")
    Call<ResponseModel> getLatestNews(@Query("q") String query,@Query("from") String date,@Query("to") String dateto, @Query("apiKey") String apiKey);

}
