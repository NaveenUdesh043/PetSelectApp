package com.assesments.madassesmenttwo

import retrofit2.Call
import retrofit2.http.GET

interface DogApiInterface {

    @GET("woof.json")
    fun getDogImage(): Call<DogImage>
}
