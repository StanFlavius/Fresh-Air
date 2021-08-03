package com.example.freshair.APIs.ApiBreezoMeter;

import com.example.freshair.Functionalities.SingleFavouriteFragment.SingleFavouriteFragment;
import com.example.freshair.Models.ModelsBreezoMeter.DataBM;
import com.example.freshair.Models.ModelsForecast.DataBMF;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiBreezoMeterInterface {

    @GET("current-conditions")
    Single<DataBM> getData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("key") String key,
            @Query("features") String features
    );

    @GET("current-conditions")
    Single<DataBM> getOneData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("key") String key,
            @Query("features") String features
    );

    @GET("current-conditions")
    Single<DataBM> getSimpleData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("key") String key,
            @Query("features") String features
    );

    @GET("forecast/hourly")
    Single<DataBMF> getForecast(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("key") String key,
            @Query("hours") Integer hours,
            @Query("features") String features
    );
}
