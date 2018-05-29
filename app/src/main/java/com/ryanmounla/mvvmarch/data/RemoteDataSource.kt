package com.ryanmounla.mvvmarch.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.widget.Toast

import com.ryanmounla.mvvmarch.model.Image

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by ryanmounla on 2018-05-28.
 */

class RemoteDataSource(private val context: Context) {

    private val endpointService: EndpointService = RetrofitHelper.getInstance()!!.provideRetrofit()!!.create(EndpointService::class.java)

    val randomDogImage: LiveData<Image>
        get() {
            val data = MutableLiveData<Image>()
            endpointService.randomDogImage.enqueue(object : Callback<DogImageResponse> {
                override fun onResponse(call: Call<DogImageResponse>, response: Response<DogImageResponse>) {
                    if (response.isSuccessful && response.body()!!.status.equals("success", ignoreCase = true)) {
                        val image = Image(response.body()!!.message)
                        data.setValue(image)
                    } else {
                        Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DogImageResponse>, t: Throwable) {
                    data.value = null
                }
            })
            return data
        }

}
