package com.example.freshair.Repo;

import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeter;
import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeterInterface;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsBreezoMeter.DataBM;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class AdviceRootRepo {

    public static final String API_KEY = "0ce43f13b56b428face8a2810d10af21";
    public static final String features = "breezometer_aqi,local_aqi,health_recommendations,pollutants_concentrations,pollutants_aqi_information";
    private static AdviceRootRepo instance;
    private List<Data> datas = new ArrayList<>();

    public static AdviceRootRepo getInstance(){
        if (instance == null){
            instance = new AdviceRootRepo();
        }
        return instance;
    }

    public Single<Data> getOneData(Double latitude, Double longitude){
        ApiBreezoMeterInterface apiBreezoMeterInterface = ApiBreezoMeter.getApiBreezoMeter().create(ApiBreezoMeterInterface.class);

        return apiBreezoMeterInterface.getOneData(latitude, longitude, API_KEY, features).map(x -> x.getData());
    }

    public Single<List<Data>> getDataAQi(List<com.google.android.gms.maps.model.LatLng> latLngList){

        ApiBreezoMeterInterface apiBreezoMeterInterface = ApiBreezoMeter.getApiBreezoMeter().create(ApiBreezoMeterInterface.class);

        List<LatLng> newList = new ArrayList<>();
        for(int i = 0; i <= 10; i++){
            newList.add(latLngList.get(i));
        }

        return Observable.fromIterable(newList)
                .flatMap(x -> apiBreezoMeterInterface.getData(x.latitude, x.longitude, API_KEY, features)
                        .toObservable().map(y -> y.getData())).toList();
    }
}
