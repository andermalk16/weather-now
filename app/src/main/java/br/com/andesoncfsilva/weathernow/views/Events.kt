package br.com.andesoncfsilva.weathernow.views

import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.entities.UnitTemp

/**
 * Created by Anderson Silva on 19/08/17.
 *
 */
class ShowCitiesWeather(val list: List<CityWeather>)

class SetCameraPosition(val latitude: Double, val longitude: Double)
class UpdateCities(val latitude: Double, val longitude: Double)
