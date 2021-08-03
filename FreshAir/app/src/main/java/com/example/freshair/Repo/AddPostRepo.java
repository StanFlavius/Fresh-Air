package com.example.freshair.Repo;

public class AddPostRepo {
    private static AddPostRepo instance;

    public static AddPostRepo getInstance(){
        if(instance == null){
            instance = new AddPostRepo();
        }
        return instance;
    }


}
