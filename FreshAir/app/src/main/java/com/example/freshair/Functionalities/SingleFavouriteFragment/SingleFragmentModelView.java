package com.example.freshair.Functionalities.SingleFavouriteFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsForecast.DataF;
import com.example.freshair.Repo.RealtimeMapRepo;
import com.example.freshair.Repo.SingleFavouriteRepo;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SingleFragmentModelView extends ViewModel {

    private MutableLiveData<Data> dataAqi;
    private MutableLiveData<List<DataF>> dataForecast;
    private SingleFavouriteRepo mRepo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void init(Double lat, Double lon) {
        dataAqi = new MutableLiveData<>();
        mRepo = SingleFavouriteRepo.getInstance();
        mRepo.getOneData(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Data>() {
                    @Override
                    public void onSuccess(@NotNull Data data) {
                        dataAqi.postValue(data);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }
                });
    }

    public void setDataForecast(Double lat, Double lon){
        dataForecast = new MutableLiveData<>();
        mRepo = SingleFavouriteRepo.getInstance();
        mRepo.getForecast(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DataF>>() {
                    @Override
                    public void onSuccess(@NotNull List<DataF> dataF) {
                        dataForecast.postValue(dataF);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d("single", "eroare");
                    }
                });
    }

    public LiveData<Data> getData(){return dataAqi;}

    public LiveData<List<DataF>> getDataForecast(){return dataForecast;}

}
