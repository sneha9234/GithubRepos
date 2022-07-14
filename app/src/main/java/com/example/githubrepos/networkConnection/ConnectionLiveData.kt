package com.example.githubrepos.networkConnection

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.*
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private val TAG = "C-Manager"
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()

        val networkCapabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.getNetworkCapabilities(cm.activeNetwork)
        } else {
            cm.getNetworkCapabilities(cm.allNetworks.find {
                cm.getNetworkCapabilities(it)?.hasCapability(NET_CAPABILITY_INTERNET) == true })
        }
        val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
        if (hasInternetCapability == true) {
            // check if this network actually has internet
            CoroutineScope(Dispatchers.IO).launch {
                val hasInternet = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cm.activeNetwork?.socketFactory?.let { DoesNetworkHaveInternet.execute(it) }
                } else {
                    cm.allNetworks.find {
                        cm.getNetworkCapabilities(it)?.hasCapability(NET_CAPABILITY_INTERNET) == true }
                        ?.let { DoesNetworkHaveInternet.execute(it.socketFactory) }
                }
                if (hasInternet == false) {
                    postValue(false)
                }
            }
        }
        else{
            postValue(false)
        }

        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        //  Called when a network is detected. If that network has internet, save it in the Set.
        override fun onAvailable(network: Network) {
            Log.d(TAG, "onAvailable: $network")
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            Log.d(TAG, "onAvailable: ${network}, $hasInternetCapability")
            if (hasInternetCapability == true) {
                // check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "onAvailable: adding network. $network")
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        // If the callback was registered with registerNetworkCallback() it will be called for each network which no longer satisfies the criteria of the callback.
        override fun onLost(network: Network) {
            Log.d(TAG, "onLost: $network")
            validNetworks.remove(network)
            checkValidNetworks()
        }


    }
}