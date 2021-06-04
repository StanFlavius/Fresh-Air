package com.example.freshair.Repo;

import com.example.freshair.ModelsBlog.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AddPostRepo {
    private static AddPostRepo instance;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public List<Post> dataBlogs = new ArrayList<>();

    public static AddPostRepo getInstance(){
        if(instance == null){
            instance = new AddPostRepo();
        }
        return instance;
    }

    public void addPost(String titlePost, String imageUrl, String content){

    }
}
