package com.example.freshair.Repo;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.example.freshair.MainActivity;
import com.example.freshair.ModelsBlog.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AddPostRepo {
    private static AddPostRepo instance;

    public static AddPostRepo getInstance(){
        if(instance == null){
            instance = new AddPostRepo();
        }
        return instance;
    }


}
