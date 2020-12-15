package com.wsayan.retrofitdemo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    var retrofit: Retrofit? = null
    val apiClient: Retrofit?
        get() = getApiClient(BuildConfig.SERVER_BASE_URL)


    fun getApiClient(url: String?): Retrofit? {

        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(if (BuildConfig.DEBUG) DefaultInterceptors.httpBodyLoggingInterceptor else DefaultInterceptors.httpNoneLoggingInterceptor)
            .build()
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit
    }

    object DefaultInterceptors {
        val httpBodyLoggingInterceptor: HttpLoggingInterceptor
            get() {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                return interceptor
            }
        val httpNoneLoggingInterceptor: HttpLoggingInterceptor
            get() {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
                return interceptor
            }
    }
}