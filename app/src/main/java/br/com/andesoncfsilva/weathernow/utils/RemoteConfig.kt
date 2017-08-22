package br.com.andesoncfsilva.weathernow.utils

import br.com.andesoncfsilva.weathernow.BuildConfig
import br.com.andesoncfsilva.weathernow.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


/**
 * Created by afsilva on 24/04/17.
 *
 */
object RemoteConfig {

    val HTTP_TIMEOUT_SECONDS: Long
        get() = config.getLong("HTTP_TIMEOUT_SECONDS")
    val HTTP_CACHE_MINUTES: Long
        get() = config.getLong("HTTP_CACHE_MINUTES")
    val OPEN_WEATHER_MAP_KEY: String
        get() = config.getString( "OPEN_WEATHER_MAP_KEY")
    val OPEN_WEATHER_MAP_URL: String
        get() = config.getString("OPEN_WEATHER_MAP_URL")
    val OPEN_WEATHER_MAP_URL_IMAGE: String
        get() = config.getString("OPEN_WEATHER_MAP_URL_IMAGE")

    private val config: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    private var cacheExpiration: Long = 5 * 60

    init {
        val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(!BuildConfig.DEBUG)
                .build()
        //expire the cache immediately for development mode.
        if (config.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }
        config.setConfigSettings(remoteConfigSettings)
        config.setDefaults(R.xml.remote_config_defaults)
    }


    fun fetch() {
        config.fetch(cacheExpiration)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        config.activateFetched()
                        WNLog.d("Firebase remote config fetched!")
                    }
                }
    }
}