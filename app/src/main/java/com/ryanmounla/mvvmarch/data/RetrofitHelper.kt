package com.ryanmounla.mvvmarch.data

import android.content.Context


import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.logging.HttpLoggingInterceptor.Level.BODY

/**
 * Created by ryanmounla on 2018-05-28.
 */

class RetrofitHelper {
    private val baseURL = "https://dog.ceo/"
    private var retrofit: Retrofit? = null

    fun provideRetrofit(): Retrofit? {
        if (retrofit == null) {
            val clientBuilder = OkHttpClient.Builder()
                    .addInterceptor(httpInterceptor())
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)


            retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        }
        return retrofit
    }

    private fun httpInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = BODY
        return httpLoggingInterceptor
    }

    companion object {

        private var sInstance: RetrofitHelper? = null

        fun getInstance(context: Context): RetrofitHelper? {
            if (sInstance == null) {
                sInstance = RetrofitHelper()
            }

            return sInstance
        }
    }
}