package com.example.freshair.Functionalities.AdviceRoot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Repo.AdviceRootRepo;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AdviceRootViewModel extends ViewModel {

    private MutableLiveData<List<Data>> datasAQI = new MutableLiveData<>();
    private AdviceRootRepo mRepo;

    public void init(ArrayList<LatLng> latLngList){
        mRepo = AdviceRootRepo.getInstance();
        mRepo.getDataAQi(latLngList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Data>>() {
                    @Override
                    public void onSuccess(@NotNull List<Data> datas) {
                        datasAQI.postValue(datas);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        System.out.println("EROARE");
                    }
                });
    }

    public LiveData<List<Data>> getData(){
        return datasAQI;
    }


}
