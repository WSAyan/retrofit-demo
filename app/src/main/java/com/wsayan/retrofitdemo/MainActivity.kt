package com.wsayan.retrofitdemo

import BaseResponse
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.wsayan.retrofitdemo.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var apiInterface: ApiInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, RES)
        apiInterface = RetrofitClient.apiClient?.create(ApiInterface::class.java)

        binding.retrofitButton.setOnClickListener {
            loadUserInfoWithRetrofit()
        }

        binding.fuelButton.setOnClickListener {
            loadUserInfoWithFuel()
        }
    }

    private fun loadUserInfoWithRetrofit() {
        clearFields()
        val call: Call<BaseResponse?>? = apiInterface?.getUserInfo()
        call?.enqueue(object : Callback<BaseResponse?> {
            override fun onResponse(call: Call<BaseResponse?>, response: Response<BaseResponse?>) {
                val data = response.body()?.data
                Picasso.get()
                    .load(data?.avatar)
                    .into(binding.ppImageView);
                binding.nameTextView.text = "${data?.first_name} ${data?.last_name}"
                binding.emailTextView.text = data?.email
            }

            override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                val throwable = t
                Log.wtf(TAG, t.message)
            }
        })
    }

    private fun loadUserInfoWithFuel() {
        clearFields()
        val httpAsync = "${BuildConfig.SERVER_BASE_URL}api/users/2"
            .httpGet()
            .responseString { request, response, result ->
                result.fold(success = {
                    Log.wtf(TAG, it)

                    val gson = Gson()
                    val responseData = gson.fromJson(it, BaseResponse::class.java)
                    val data = responseData.data

                    Picasso.get()
                        .load(data.avatar)
                        .into(binding.ppImageView);
                    binding.nameTextView.text = "${data?.first_name} ${data?.last_name}"
                    binding.emailTextView.text = data.email

                }, failure = {
                    Log.wtf(TAG, String(it.errorData))
                })
            }

        httpAsync.join()
    }

    private fun clearFields() {
        binding.ppImageView.setImageDrawable(null)
        binding.nameTextView.text = ""
        binding.emailTextView.text = ""
    }

    companion object {
        const val RES = R.layout.activity_main
        const val TAG = "MainActivity"
    }
}