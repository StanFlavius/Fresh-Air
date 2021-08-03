package com.example.freshair.Functionalities.Register;

import android.view.Display;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.Repo.RegisterRepo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RegisterModelView extends ViewModel {
    private MutableLiveData<List<String>> userEmails;
    private RegisterRepo mRepo;

    public void init(){
        userEmails = new MutableLiveData<>();
        mRepo = RegisterRepo.getInstance();
        mRepo.getEmails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(@NotNull List<String> strings) {
                        userEmails.postValue(strings);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }
                });
    }

    public LiveData<List<String>> getEmails(){return userEmails;}
}
