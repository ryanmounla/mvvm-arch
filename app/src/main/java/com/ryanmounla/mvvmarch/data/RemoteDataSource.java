package com.ryanmounla.mvvmarch.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.Toast;

import com.ryanmounla.mvvmarch.model.Image;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ryanmounla on 2018-05-28.
 */

public class RemoteDataSource {

    private final EndpointService endpointService;
    private Context context;

    public RemoteDataSource(Context context) {
        this.context = context;
        endpointService = RetrofitHelper.getInstance(context).provideRetrofit().create(EndpointService.class);
    }

    public LiveData<Image> getRandomDogImage() {
        final MutableLiveData<Image> data = new MutableLiveData<>();
        endpointService.getRandomDogImage().enqueue(new Callback<DogImageResponse>() {
            @Override
            public void onResponse(Call<DogImageResponse> call, Response<DogImageResponse> response) {
                if (response.isSuccessful() && response.body().status.equalsIgnoreCase("success")) {
                    Image image = new Image();
                    image.url = response.body().message;
                    data.setValue(image);
                } else {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DogImageResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
