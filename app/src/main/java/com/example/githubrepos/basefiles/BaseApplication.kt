package com.example.githubrepos.basefiles

import android.app.Application
import com.example.networkmodule.wrapper.NetworkInitializer

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }
    open fun initDependencies() {
        NetworkInitializer().initialize(this, getBaseURL())
    }

    abstract fun getBaseURL(): String

}