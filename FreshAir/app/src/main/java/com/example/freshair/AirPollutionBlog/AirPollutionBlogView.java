package com.example.freshair.AirPollutionBlog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.ModelsBlog.Post;
import com.example.freshair.Repo.AirPollutionBlogRepo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AirPollutionBlogView extends ViewModel {
    private MutableLiveData<List<Post>> postsList;
    private AirPollutionBlogRepo mRepo;

    public void init(){
        postsList = new MutableLiveData<List<Post>>();
        mRepo = AirPollutionBlogRepo.getInstance();
        mRepo.getListPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Post>>() {
                    @Override
                    public void onSuccess(@NotNull List<Post> posts) {
                        postsList.postValue(posts);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }
                });
    }

    public LiveData<List<Post>> getPosts(){
        return postsList;
    }
}
