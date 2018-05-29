package com.ryanmounla.mvvmarch.data;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ryanmounla on 2018-05-28.
 */

public interface EndpointService {

    @GET("/api/breeds/image/random")
    Call<DogImageResponse> getRandomDogImage();
}
