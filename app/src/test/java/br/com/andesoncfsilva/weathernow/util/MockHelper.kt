package br.com.andesoncfsilva.weathernow.util

import br.com.andesoncfsilva.weathernow.data.response.*
import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.entities.GeoBox
import br.com.andesoncfsilva.weathernow.entities.UnitTemp

/**
 * Created by Anderson Silva on 20/08/17.
 */
object MockHelper {

    val latitude: Double = 1.0

    val longitude: Double = 1.0

    val unitTemp: UnitTemp = UnitTemp.CELSIUS

    val openMapKey: String = "openMapKey"

    val openMapUrl: String = "http://www.openMapUrl.com"

    val openMapIconCode: String = "openMapIconCode"

    val openMapIconUrl: String = "http://www.openMapUrl.com/icon.png"

    val geoBox: GeoBox = GeoBox(1.0, 1.0, 1.0, 1.0)

    val cityWeather: CityWeather = CityWeather(
            id = city.id!!,
            cityName = city.name!!,
            latitude = city.coord?.lat!!,
            longitude = city.coord?.lon!!,
            weatherDescription = city.weather?.firstOrNull()?.description!!,
            weatherImageUrl = city.weather?.firstOrNull()?.icon!!,
            currentTemperature = city.main?.temp!!,
            maxTemperature = city.main?.tempMax!!,
            minTemperature = city.main?.tempMin!!,
            unit = unitTemp,
            distance = 0.0
    )

    val citiesWeather : List<CityWeather> = arrayListOf(cityWeather.copy(), cityWeather.copy(), cityWeather.copy(), cityWeather.copy())

    val city: City
        get() {
            val weather = arrayListOf(Weather(description = "description", icon = "icon"))
            val coord = Coord(lat = 1.0, lon = 1.0)
            val main = Main(temp = 1.0, tempMax = 1.0, tempMin = 1.0)
            return City(id = 1, name = "name", coord = coord, main = main, weather = weather)
        }

    val currentWeatherResponse: CurrentWeatherResponse
        get() {
            val list = arrayListOf(city.copy(), city.copy(), city.copy(), city.copy())
            return CurrentWeatherResponse(city = list, cnt = list.count())
        }

}