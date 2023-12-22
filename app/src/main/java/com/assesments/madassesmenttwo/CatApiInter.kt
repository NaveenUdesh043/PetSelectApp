package com.assesments.madassesmenttwo

import retrofit2.Call
import retrofit2.http.GET

interface CatApiInter {
    @GET("images/search")
    fun getCatImage(): Call<List<CatImage>>
}