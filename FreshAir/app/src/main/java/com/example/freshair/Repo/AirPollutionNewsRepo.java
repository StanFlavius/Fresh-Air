package com.example.freshair.Repo;

import io.reactivex.Single;

import com.example.freshair.APIs.ApiAirPollutionNews.ApiAirPollutionNews;
import com.example.freshair.APIs.ApiAirPollutionNews.ApiAirPollutionNewsInterface;
import com.example.freshair.Models.ModelsAirPollutionNews.Article;

import java.util.ArrayList;
import java.util.List;

public class
AirPollutionNewsRepo {
    public static final String API_KEY = "7abdfa14f7a54c28be1f7e01a7155dd4";
    private static AirPollutionNewsRepo instance;
    private List<Article> dataSet = new ArrayList<>();


    public static AirPollutionNewsRepo getInstance(){
        if(instance == null){
            instance = new AirPollutionNewsRepo();
        }
        return instance;
    }

    public Single<List<Article>> getListArticle(){
        ApiAirPollutionNewsInterface apiAirPollutionNewsInterface = ApiAirPollutionNews.getApiAirPollutionNews().create(ApiAirPollutionNewsInterface.class);

        return apiAirPollutionNewsInterface.getNews("air pollution","2021-04-31", "publishedAt", API_KEY).map(x -> x.getArticles());
    }

}
