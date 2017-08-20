package br.com.andesoncfsilva.weathernow.views

import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.views.base.MvpView

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
interface MainView : MvpView {
    fun setCameraPosition(latitude: Double, longitude: Double)
    fun showLoading()
    fun hideLoading()
    fun showError(e: Throwable)
    fun showCitiesWeather(cities: List<CityWeather>)
}