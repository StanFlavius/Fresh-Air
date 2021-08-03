package com.example.freshair.Functionalities.Favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsFavourites.Favourite;
import com.example.freshair.Repo.AirPollutionBlogRepo;
import com.example.freshair.Repo.FavouritesRepo;
import com.example.freshair.Utils.ItemFavourite;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FavouritesViewModel extends ViewModel {
    private MutableLiveData<List<ItemFavourite>> favsList;
    private FavouritesRepo mRepo;

    public void init(){
        favsList = new MutableLiveData<List<ItemFavourite>>();
        mRepo = FavouritesRepo.getInstance();
        mRepo.getListFavs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Favourite>>() {
                    @Override
                    public void onSuccess(@NotNull List<Favourite> favs) {
                        List<LatLng> latLngList = new ArrayList<LatLng>();
                        for (Favourite fav : favs){
                            latLngList.add(new LatLng(fav.getLatitude(), fav.getLongitude()));
                        }
                        mRepo.getDataAQi(latLngList)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSingleObserver<List<Data>>() {
                                    @Override
                                    public void onSuccess(@NotNull List<Data> datas) {
                                        List<ItemFavourite> its = new ArrayList<>();
                                        for(int i = 0; i < datas.size(); i++){
                                            its.add(new ItemFavourite(
                                               favs.get(i).getLocationName(),
                                                    datas.get(i).getIndexes().getBaqi().getAqi(),
                                                    datas.get(i).getIndexes().getBaqi().getCategory(),
                                                    favs.get(i).getLatitude(),
                                                    favs.get(i).getLongitude()
                                            ));
                                        }
                                        favsList.postValue(its);
                                    }

                                    @Override
                                    public void onError(@NotNull Throwable e) {
                                        System.out.println("EROARE");
                                    }
                                });
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }
                });
    }


    public LiveData<List<ItemFavourite>> getFavs(){
        return favsList;
    }

}
