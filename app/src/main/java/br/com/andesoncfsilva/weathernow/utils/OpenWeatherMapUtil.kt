package br.com.andesoncfsilva.weathernow.utils

import android.content.Context
import br.com.andesoncfsilva.weathernow.BuildConfig
import javax.inject.Inject


/**
 * Created by andersoncfsilva on 17/08/17.
 * A class to take API key from properties, resources and compiled in.
 */
class OpenWeatherMapUtilImpl : OpenWeatherMapUtil {

    override fun getUrlIcon(code: String?): String {
        return "${BuildConfig.OPEN_WEATHER_MAP_URL_IMAGE}$code.png"
    }

    override fun getKey(): String {
        return BuildConfig.OPEN_WEATHER_MAP_KEY
    }
}

interface OpenWeatherMapUtil {
    fun getKey(): String
    fun getUrlIcon(code: String?): String
}