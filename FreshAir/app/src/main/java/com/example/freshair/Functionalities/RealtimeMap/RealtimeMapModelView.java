package com.example.freshair.Functionalities.RealtimeMap;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsFavourites.Favourite;
import com.example.freshair.Repo.AdviceRootRepo;
import com.example.freshair.Repo.RealtimeMapRepo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RealtimeMapModelView extends ViewModel {
    private MutableLiveData<Data> dataAqi;
    private MutableLiveData<List<String>> favouritesList;
    private RealtimeMapRepo mRepo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void init(Double lat, Double lon){
        dataAqi = new MutableLiveData<>();
        mRepo = RealtimeMapRepo.getInstance();
        mRepo.getOneData(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Data>() {
                    @Override
                    public void onSuccess(@NotNull Data data) {
                        dataAqi.postValue(data);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }
                });

        favouritesList = new MutableLiveData<>();
        mRepo.getAllFav()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(@NotNull List<String> fav) {
                        favouritesList.postValue(fav);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }
                });
    }

    public LiveData<Data> getData(){ return dataAqi; }

    public LiveData<List<String>> getFav(){return favouritesList;}

    public void addFav(Favourite fav){
        db.collection("Favourites")
                .document(UUID.randomUUID().toString())
                .set(fav)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("FAILED", e.getLocalizedMessage());
                    }
                });
    }

    public void deleteFav(String address, String userToken){
        List<String> ids = new ArrayList<>();
        db.collection("Favourites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                if (doc.getString("userToken").equals(userToken) &&
                                        (doc.getString("locationName").equals(address)))
                                    ids.add(doc.getId());
                            }
                            for(String id : ids){
                                db.collection("Favourites").document(id)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
