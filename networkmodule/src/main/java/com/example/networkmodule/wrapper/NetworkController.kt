package com.example.networkmodule.wrapper

import android.content.Context
import com.example.networkmodule.retrofit.RetrofitClient

object NetworkController {

    private lateinit var retrofitClient: RetrofitClient
    private lateinit var baseUrl: String

    fun init(baseUrl: String, context: Context) {
        this.baseUrl = baseUrl
        this.retrofitClient = RetrofitClient(
            NetworkController.baseUrl,
            context
        )
    }

    fun getAPIClient(): RetrofitClient {
        return retrofitClient
    }

}