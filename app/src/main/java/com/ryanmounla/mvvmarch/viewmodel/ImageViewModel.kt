package com.ryanmounla.mvvmarch.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.ryanmounla.mvvmarch.data.RemoteDataSource
import com.ryanmounla.mvvmarch.model.Image

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    private val dataSource: RemoteDataSource = RemoteDataSource(application)
    private lateinit var imageObservable: LiveData<Image>

    fun registerDogImageObservable(): LiveData<Image> {
        imageObservable = dataSource.randomDogImage
        return imageObservable
    }
}