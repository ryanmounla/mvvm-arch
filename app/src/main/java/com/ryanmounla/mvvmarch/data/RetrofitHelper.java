package com.ryanmounla.mvvmarch.data;

import android.content.Context;
import android.support.annotation.NonNull;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * Created by ryanmounla on 2018-05-28.
 */

public class RetrofitHelper {

    private static RetrofitHelper sInstance = null;
    private final String baseURL = "https://dog.ceo/";
    private Retrofit retrofit;

    private RetrofitHelper(@NonNull Context context) {}

    public static RetrofitHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new RetrofitHelper(context);
        }

        return sInstance;
    }

    public Retrofit provideRetrofit() {
        if(retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(httpInterceptor())
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS);


            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    private HttpLoggingInterceptor httpInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BODY);
        return httpLoggingInterceptor;
    }
}