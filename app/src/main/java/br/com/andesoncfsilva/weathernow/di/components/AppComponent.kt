package br.com.andesoncfsilva.weathernow.di.components

import android.content.Context
import br.com.andesoncfsilva.weathernow.WeatherNowApplication
import br.com.andesoncfsilva.weathernow.data.MapperCityWeather
import br.com.andesoncfsilva.weathernow.data.RestApi
import br.com.andesoncfsilva.weathernow.data.WeatherApi
import br.com.andesoncfsilva.weathernow.di.executors.PostExecutionThread
import br.com.andesoncfsilva.weathernow.di.executors.ThreadExecutor
import br.com.andesoncfsilva.weathernow.di.modules.AppModule
import br.com.andesoncfsilva.weathernow.utils.GeoCalculator
import br.com.andesoncfsilva.weathernow.utils.HardwareUtil
import br.com.andesoncfsilva.weathernow.utils.OpenWeatherMapUtil
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: WeatherNowApplication)

    fun context(): Context
    fun threadExecutor(): ThreadExecutor
    fun postExecutionThread(): PostExecutionThread

    fun gson(): Gson
    fun okHttpClient(): OkHttpClient
    fun retrofit(): Retrofit
    fun restApi(): RestApi
    fun weatherApi(): WeatherApi
    fun openWeatherMapApiKey(): OpenWeatherMapUtil
    fun hardwareUtil(): HardwareUtil
    fun geoCalculator(): GeoCalculator
    fun mapperCityWeather(): MapperCityWeather
}