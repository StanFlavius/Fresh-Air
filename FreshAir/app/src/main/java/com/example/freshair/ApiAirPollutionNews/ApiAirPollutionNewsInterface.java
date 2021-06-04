package com.example.freshair.ApiAirPollutionNews;

import com.example.freshair.ModelsAirPollutionNews.News;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiAirPollutionNewsInterface {

    @GET("everything")
    Single<News> getNews(
            @Query("q") String q,
            @Query("from") String from,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );
}
