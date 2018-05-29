package com.ryanmounla.mvvmarch.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ryanmounla.mvvmarch.data.RemoteDataSource;
import com.ryanmounla.mvvmarch.model.Image;

/**
 * Created by ryanmounla on 2018-05-28.
 */

public class ImageViewModel extends AndroidViewModel {

    private RemoteDataSource dataSource;
    private LiveData<Image> imageObservable;

    public ImageViewModel(@NonNull Application application){
        super(application);
        this.dataSource = new RemoteDataSource(application);
    }

    public LiveData<Image> registerDogImageObservable(){
        imageObservable = dataSource.getRandomDogImage();
        return imageObservable;
    }
}
