package com.example.freshair.Functionalities.AirPollutionBlog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.Repo.AddPostRepo;

import java.util.List;

public class AddPostViewModel extends ViewModel {

    private MutableLiveData<List<Post>> postsList;
    private AddPostRepo mRepo;

    public void init(){
        postsList = new MutableLiveData<List<Post>>();
        mRepo = AddPostRepo.getInstance();
    }
}
