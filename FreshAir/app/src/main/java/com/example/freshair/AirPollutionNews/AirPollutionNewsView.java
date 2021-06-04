package com.example.freshair.AirPollutionNews;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshair.ModelsAirPollutionNews.Article;
import com.example.freshair.Repo.AirPollutionNewsRepo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AirPollutionNewsView extends ViewModel {

    private MutableLiveData<List<Article>> articlesList;
    private AirPollutionNewsRepo mRepo;

    public void init(){
        articlesList = new MutableLiveData<List<Article>>();
        mRepo = AirPollutionNewsRepo.getInstance();
        mRepo.getListArticle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Article>>() {
                    @Override
                    public void onSuccess(@NonNull List<Article> articles) {
                        articlesList.postValue(articles);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    public LiveData<List<Article>> getArticles() {
        return articlesList;
    }
}
