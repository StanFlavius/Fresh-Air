package com.example.freshair.Functionalities.HomeScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.Functionalities.AdviceRoot.AdviceRootViewModel;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Repo.AdviceRootRepo;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeScreenViewModel extends ViewModel {
    private MutableLiveData<Data> dataAqi;
    private AdviceRootRepo mRepo;

    public void init(Double lat, Double lon){
        dataAqi = new MutableLiveData<>();
        mRepo = AdviceRootRepo.getInstance();
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

    public LiveData<Data> getData(){ return dataAqi; }
}
