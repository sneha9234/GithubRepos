package com.example.networkmodule.wrapper

import android.content.Context

class NetworkInitializer {

    private lateinit var baseUrl:String
    private lateinit var context: Context

    /*
    Initialization of retrofit object with application context and
    base url of restful apis
    * */
    fun initialize(context: Context, baseUrl: String){
        this.baseUrl = baseUrl
        this.context = context
        NetworkController.init(baseUrl, context)
    }

    fun context(): Context {
        return context
    }

}