package com.example.freshair.Repo;

import com.example.freshair.ModelsBlog.Post;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class AirPollutionBlogRepo {
    private static AirPollutionBlogRepo instance;

    public List<Post> dataBlogs = new ArrayList<>();

    public static AirPollutionBlogRepo getInstance(){
        if(instance == null){
            instance = new AirPollutionBlogRepo();
        }
        return instance;
    }

}
