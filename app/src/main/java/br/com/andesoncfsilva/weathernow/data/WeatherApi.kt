package br.com.andesoncfsilva.weathernow.data

import br.com.andesoncfsilva.weathernow.data.response.CurrentWeatherResponse
import br.com.andesoncfsilva.weathernow.entities.UnitTemp
import br.com.andesoncfsilva.weathernow.exception.RestAPIException
import br.com.andesoncfsilva.weathernow.utils.OpenWeatherMapUtil
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by andersoncfsilva on 17/08/17.
 *
 */
class WeatherApiImpl @Inject constructor(private val api: RestApi,
                                         private val openWeatherMapUtil: OpenWeatherMapUtil) : WeatherApi {

    override fun getCurrentWeather(unitTemp: UnitTemp?,
                                   lon_left: Double?,
                                   lat_bottom: Double?,
                                   lon_right: Double?,
                                   lat_top: Double?
    ): Observable<CurrentWeatherResponse> {
        return api.getCurrentWeather(
                openWeatherMapUtil.getKey(),
                "$lon_left,$lat_bottom,$lon_right,$lat_top,100",
                unitTemp.toString(),
                "pt")
                .doOnError { throw RestAPIException(it) }
    }
}

interface WeatherApi {
    fun getCurrentWeather(unitTemp: UnitTemp?,
                          lon_left: Double?,
                          lat_bottom: Double?,
                          lon_right: Double?,
                          lat_top: Double?): Observable<CurrentWeatherResponse>
}