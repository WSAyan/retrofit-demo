package com.wsayan.retrofitdemo

import BaseResponse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
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
        loadUserInfo()
    }

    private fun loadUserInfo() {
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

            }
        })
    }

    companion object {
        const val RES = R.layout.activity_main
    }
}