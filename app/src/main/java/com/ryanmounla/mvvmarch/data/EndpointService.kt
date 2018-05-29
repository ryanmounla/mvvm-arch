package com.ryanmounla.mvvmarch.data

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by ryanmounla on 2018-05-28.
 */

interface EndpointService {

    @get:GET("/api/breeds/image/random")
    val randomDogImage: Call<DogImageResponse>
}
