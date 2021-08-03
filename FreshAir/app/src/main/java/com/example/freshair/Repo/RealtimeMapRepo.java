package com.example.freshair.Repo;

import androidx.annotation.NonNull;

import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeter;
import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeterInterface;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class RealtimeMapRepo {
    public static final String API_KEY = "0ce43f13b56b428face8a2810d10af21";
    public static final String features = "breezometer_aqi,local_aqi,health_recommendations,pollutants_concentrations,pollutants_aqi_information";
    private static RealtimeMapRepo instance;
    private List<Data> datas = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<LatLng> dataCoordinates = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private List<String> addressList = new ArrayList<>();

    public static RealtimeMapRepo getInstance(){
        if (instance == null){
            instance = new RealtimeMapRepo();
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
        for(int i = 0; i <= 50; i++){
            newList.add(latLngList.get(i));
        }

        return Observable.fromIterable(newList)
                .flatMap(x -> apiBreezoMeterInterface.getData(x.latitude, x.longitude, API_KEY, features)
                        .toObservable().map(y -> y.getData())).toList();
    }

    public Single<List<String>> getAllFav(){
        return Single.create(subscriber -> {
            db.collection("Favourites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(QueryDocumentSnapshot doc : task.getResult()){
                                    if (doc.getString("userToken").equals(mAuth.getCurrentUser().getUid()))
                                        addressList.add(doc.getString("locationName"));
                                        //dataCoordinates.add(new LatLng(doc.getDouble("latitude"), doc.getDouble("longitude")));
                                }
                                subscriber.onSuccess(addressList);
                            }
                        }
                    });
        });
    }
}
