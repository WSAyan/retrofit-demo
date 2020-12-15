package com.wsayan.retrofitdemo

import BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("api/users/2")
    fun getUserInfo(): Call<BaseResponse?>?
}