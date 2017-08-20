package br.com.andesoncfsilva.weathernow.entities

import java.io.Serializable

/**
 * Created by Anderson Silva on 18/08/17.
 */
data class CityWeather(val id: Int,
                       val cityName: String,
                       val latitude: Double,
                       val longitude: Double,
                       val weatherDescription: String,
                       val weatherImageUrl: String,
                       val currentTemperature: Double,
                       val maxTemperature: Double,
                       val minTemperature: Double,
                       val unit: UnitTemp,
                       val distance: Double = 0.0,
                       val userDistance: Double = 0.0
                       ) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is CityWeather)
            return false
        return cityName == other.cityName && id == other.id
    }

    override fun hashCode(): Int =
            cityName.hashCode() * 31 + id

    override fun toString(): String {
        return "CityWeather(name=$cityName, code=$id"
    }
}