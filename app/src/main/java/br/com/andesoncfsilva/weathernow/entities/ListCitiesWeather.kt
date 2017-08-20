package br.com.andesoncfsilva.weathernow.entities

/**
 * Created by Anderson Silva on 18/08/17.
 *
 */
data class ListCitiesWeather(val currentLatitude: Double,
                             val currentLongitude: Double,
                             val cities: List<CityWeather>)