package br.com.andesoncfsilva.weathernow.data

import br.com.andesoncfsilva.weathernow.data.response.CurrentWeatherResponse
import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.entities.UnitTemp
import br.com.andesoncfsilva.weathernow.utils.OpenWeatherMapUtil
import javax.inject.Inject

/**
 * Created by Anderson Silva on 18/08/17.
 *
 */
class MapperCityWeatherImpl @Inject constructor(val openWeatherMapUtil: OpenWeatherMapUtil) : MapperCityWeather {

    override fun convert(from: CurrentWeatherResponse?, unitTemp: UnitTemp): List<CityWeather>? {
        return from?.city?.map { city ->
            CityWeather(
                    id = city.id!!,
                    cityName = city.name!!,
                    latitude = city.coord?.lat!!,
                    longitude = city.coord?.lon!!,
                    weatherDescription = city.weather?.firstOrNull()?.description!!,
                    weatherImageUrl = openWeatherMapUtil.getUrlIcon(city.weather?.firstOrNull()?.icon!!),
                    currentTemperature = city.main?.temp!!,
                    maxTemperature = city.main?.tempMax!!,
                    minTemperature = city.main?.tempMin!!,
                    unit = unitTemp,
                    distance = 0.0,
                    userDistance = 0.0
            )
        }
    }

}

interface MapperCityWeather {
    fun convert(from: CurrentWeatherResponse?, unitTemp: UnitTemp): List<CityWeather>?
}