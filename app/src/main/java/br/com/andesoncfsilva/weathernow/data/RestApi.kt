package br.com.andesoncfsilva.weathernow.data

import br.com.andesoncfsilva.weathernow.data.response.CurrentWeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
interface RestApi {

    @GET("/data/2.5/box/city")
    fun getCurrentWeather(
            @Query("appid") apiKey: String,
            @Query("bbox") bbox: String,
            @Query("units") units: String,
            @Query("lang") lang: String = "pt"
    ): Observable<CurrentWeatherResponse>

}