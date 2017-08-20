package br.com.andesoncfsilva.weathernow.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject


class HardwareUtilImpl @Inject constructor(private val context: Context) : HardwareUtil {

    override fun connected(): Boolean {
        val systemService = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        val cm = systemService as ConnectivityManager
        val netInfo: NetworkInfo? = cm.activeNetworkInfo
        return netInfo?.isConnectedOrConnecting == true
    }
}

interface HardwareUtil {

    fun connected(): Boolean
}