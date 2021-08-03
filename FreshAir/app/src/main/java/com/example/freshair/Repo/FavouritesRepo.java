package com.example.freshair.Repo;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.Single;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeter;
import com.example.freshair.APIs.ApiBreezoMeter.ApiBreezoMeterInterface;
import com.example.freshair.Functionalities.SingleFavouriteFragment.SingleFavouriteFragment;
import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsBreezoMeter.DataBM;
import com.example.freshair.Models.ModelsFavourites.Favourite;
import com.example.freshair.Utils.ItemFavourite;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class FavouritesRepo {
    private static FavouritesRepo instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public List<Favourite> dataFavs = new ArrayList<>();
    public List<Favourite> dataFavs2 = new ArrayList<>();
    public static final String API_KEY = "0ce43f13b56b428face8a2810d10af21";
    public static final String features = "breezometer_aqi,local_aqi,health_recommendations,pollutants_concentrations,pollutants_aqi_information";

    public static FavouritesRepo getInstance(){
        if(instance == null){
            instance = new FavouritesRepo();
        }
        return instance;
    }

    public Single<List<Favourite>> getListFavs(){
        dataFavs = new ArrayList<>();
        return Single.create(subscriber -> {
            db.collection("Favourites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot doc : task.getResult()){
                                    if(doc.getString("userToken").equals(mAuth.getCurrentUser().getUid())){
                                        String loc = doc.getString("locationName");
                                        Double lat = doc.getDouble("latitude");
                                        Double longd = doc.getDouble("longitude");
                                        Favourite favourite = new Favourite(mAuth.getCurrentUser().getUid(), loc, lat, longd);
                                        dataFavs.add(favourite);
                                    }
                                }
                                subscriber.onSuccess(dataFavs);
                            }
                            else{
                                subscriber.onError(task.getException());
                                Log.i( "MESAJ", task.getException().getLocalizedMessage());
                            }
                        }
                    });
        });
    }

    public Single<List<Favourite>> getFavsFromDB(){
        return Single.create(subscriber -> {
            db.collection("Favourites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //locatii favorite din baza de date
                                List<Favourite> dataFavs = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.getString("userToken").equals(mAuth.getCurrentUser().getUid())) {
                                        String loc = doc.getString("locationName");
                                        Double lat = doc.getDouble("latitude");
                                        Double longd = doc.getDouble("longitude");
                                        Favourite favourite = new Favourite(mAuth.getCurrentUser().getUid(), loc, lat, longd);
                                        dataFavs.add(favourite);
                                    }
                                }
                                subscriber.onSuccess(dataFavs);
                            }
                            else{
                                subscriber.onError(task.getException());
                            }
                        }
                    });
        });
    }

    public Single<List<ItemFavourite>> getMoreData(List<Favourite> list){
        List<LatLng> latLngList = new ArrayList<LatLng>();
        for (Favourite fav : list){
            latLngList.add(new LatLng(fav.getLatitude(), fav.getLongitude()));
        }
        return getDataAQi(latLngList).map(x -> {
            ArrayList<ItemFavourite> its = new ArrayList<>();
            for (int i = 0; i < x.size(); i++) {
                its.add(new ItemFavourite(
                        list.get(i).getLocationName(),
                        x.get(i).getIndexes().getBaqi().getAqi(),
                        x.get(i).getIndexes().getBaqi().getCategory(),
                        list.get(i).getLatitude(),
                        list.get(i).getLongitude()
                ));
            }
            return its;
        });
    }

    /*public Single<List<ItemFavourite>> getListDataNotif() {
        return Single.create(subscriber -> {
            db.collection("Favourites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //locatii favorite din baza de date
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.getString("userToken").equals(mAuth.getCurrentUser().getUid())) {
                                        String loc = doc.getString("locationName");
                                        Double lat = doc.getDouble("latitude");
                                        Double longd = doc.getDouble("longitude");
                                        Favourite favourite = new Favourite(mAuth.getCurrentUser().getUid(), loc, lat, longd);
                                        dataFavs2.add(favourite);

                                    }
                                }

                                //preiau coordonatele pt fiecare locatie
                                List<LatLng> latLngList = new ArrayList<LatLng>();
                                for (Favourite fav : dataFavs2) {
                                    latLngList.add(new LatLng(fav.getLatitude(), fav.getLongitude()));
                                }

                                //pentru fiecare pereche de coordonate preiau datele despre calitatea aerului
                                ApiBreezoMeterInterface apiBreezoMeterInterface = ApiBreezoMeter.getApiBreezoMeter().create(ApiBreezoMeterInterface.class);

                                List<LatLng> newList = new ArrayList<>();
                                for (int i = 0; i < latLngList.size(); i++) {
                                    newList.add(latLngList.get(i));
                                }

                                List<Data> datas = new ArrayList<>();
                                for (LatLng latLng : newList) {
                                    Data d = apiBreezoMeterInterface.getSimpleData(latLng.latitude, latLng.longitude, API_KEY, features).map();
                                    datas.add(d);
                                }

                                //pentru fiecare locatie extrag datele care ma intereseaza
                                List<ItemFavourite> its = new ArrayList<>();
                                for (int i = 0; i < datas.size(); i++) {
                                    its.add(new ItemFavourite(
                                            dataFavs2.get(i).getLocationName(),
                                            datas.get(i).getIndexes().getBaqi().getAqi(),
                                            datas.get(i).getIndexes().getBaqi().getCategory(),
                                            dataFavs2.get(i).getLatitude(),
                                            dataFavs2.get(i).getLongitude()
                                    ));
                                }
                                subscriber.onSuccess(its);
                            }
                            else {
                                subscriber.onError(task.getException());
                            }
                        }
                    });
        })
    }*/

    public Single<List<ItemFavourite>> getForNotif(){

        return getFavsFromDB().observeOn(Schedulers.newThread()).flatMap(x -> getMoreData(x));

    }
  /*
  Observable.fromIterable(newList)
                .flatMap(x -> apiBreezoMeterInterface.getData(x.latitude, x.longitude, API_KEY, features)
                        .toObservable().map(y -> y.getData())).toList();
   */
    /*public List<Data> getDataAqi2(List<com.google.android.gms.maps.model.LatLng> latLngList){
        ApiBreezoMeterInterface apiBreezoMeterInterface = ApiBreezoMeter.getApiBreezoMeter().create(ApiBreezoMeterInterface.class);

        List<LatLng> newList = new ArrayList<>();
        for(int i = 0; i < latLngList.size(); i++){
            newList.add(latLngList.get(i));
        }

        List<Data> datas = new ArrayList<>();
        for(LatLng latLng : newList){
            Data d = apiBreezoMeterInterface.getSimpleData(latLng.latitude, latLng.longitude, API_KEY, features).getData();
            datas.add(d);
        }

        return datas;
    }*/

    public Single<List<Data>> getDataAQi(List<com.google.android.gms.maps.model.LatLng> latLngList){

        ApiBreezoMeterInterface apiBreezoMeterInterface = ApiBreezoMeter.getApiBreezoMeter().create(ApiBreezoMeterInterface.class);

        List<LatLng> newList = new ArrayList<>();
        for(int i = 0; i < latLngList.size(); i++){
            newList.add(latLngList.get(i));
        }

        return Observable.fromIterable(newList)
                .flatMap(x -> apiBreezoMeterInterface.getData(x.latitude, x.longitude, API_KEY, features)
                        .toObservable().map(y -> y.getData())).toList();
    }
}
