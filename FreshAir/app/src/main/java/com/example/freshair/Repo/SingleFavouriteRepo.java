package com.example.freshair.Repo;

import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeter;
import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeterInterface;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsForecast.DataF;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class SingleFavouriteRepo {

    public static final String API_KEY = "0ce43f13b56b428face8a2810d10af21";
    public static final String features = "breezometer_aqi,local_aqi,health_recommendations,pollutants_concentrations,pollutants_aqi_information";
    public static final String featuresForecast = "breezometer_aqi";
    public static final Integer hours = 10;
    private static SingleFavouriteRepo instance;
    private List<Data> datas = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<LatLng> dataCoordinates = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static SingleFavouriteRepo getInstance(){
        if (instance == null){
            instance = new SingleFavouriteRepo();
        }
        return instance;
    }

    public Single<Data> getOneData(Double latitude, Double longitude){
        ApiBreezoMeterInterface apiBreezoMeterInterface = ApiBreezoMeter.getApiBreezoMeter().create(ApiBreezoMeterInterface.class);

        return apiBreezoMeterInterface.getOneData(latitude, longitude, API_KEY, features).map(x -> x.getData());
    }

    public Single<List<DataF>> getForecast(Double latitude, Double longitude){
        ApiBreezoMeterInterface apiBreezoMeterInterface = ApiBreezoMeter.getApiBreezoMeter().create(ApiBreezoMeterInterface.class);

        return apiBreezoMeterInterface.getForecast(latitude, longitude, API_KEY, hours, featuresForecast).map(x -> x.getDataF());
    }

}
