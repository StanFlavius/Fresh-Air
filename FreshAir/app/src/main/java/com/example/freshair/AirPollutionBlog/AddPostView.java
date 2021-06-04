package com.example.freshair.AirPollutionBlog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.ModelsBlog.Post;
import com.example.freshair.Repo.AddPostRepo;

import java.util.List;

public class AddPostView extends ViewModel {

    private MutableLiveData<List<Post>> postsList;
    private AddPostRepo mRepo;

    public void init(){
        postsList = new MutableLiveData<List<Post>>();
        mRepo = AddPostRepo.getInstance();
    }

    public void addPost(String titlePost, String imageUrl, String content){
        mRepo.addPost(titlePost, imageUrl, content);
    }
}
