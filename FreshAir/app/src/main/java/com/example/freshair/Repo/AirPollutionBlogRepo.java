package com.example.freshair.Repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.freshair.Models.ModelsBlog.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class AirPollutionBlogRepo {
    private static AirPollutionBlogRepo instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<Post> dataBlogs = new ArrayList<>();

    public static AirPollutionBlogRepo getInstance(){
        if(instance == null){
            instance = new AirPollutionBlogRepo();
        }
        return instance;
    }

    public Single<List<Post>> getListPosts(){
        dataBlogs = new ArrayList<>();
        return Single.create(subscriber -> {
            db.collection("Posts")
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot doc : task.getResult()){
                                    String titlePost = doc.getString("titlePost");
                                    String contentPost = doc.getString("contentPost");
                                    String firstName = doc.getString("firstName");
                                    String lastName = doc.getString("lastName");
                                    String createdAt = doc.getString("createdAt");
                                    String urlImage = doc.getString("urlImage");

                                    Post post = new Post(firstName, lastName, titlePost, contentPost, createdAt, urlImage);
                                    dataBlogs.add(post);
                                }
                                subscriber.onSuccess(dataBlogs);
                            }
                            else{
                                subscriber.onError(task.getException());
                                Log.i( "MESAJ", task.getException().getLocalizedMessage());
                            }
                        }
                    });
        });
    }
}
