package br.com.andesoncfsilva.weathernow.interactors

import android.location.Location
import br.com.andesoncfsilva.weathernow.data.MapperCityWeather
import br.com.andesoncfsilva.weathernow.data.WeatherApi
import br.com.andesoncfsilva.weathernow.di.executors.PostExecutionThread
import br.com.andesoncfsilva.weathernow.di.executors.ThreadExecutor
import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.entities.ListCitiesWeather
import br.com.andesoncfsilva.weathernow.entities.UnitTemp
import br.com.andesoncfsilva.weathernow.exception.NoConnectionException
import br.com.andesoncfsilva.weathernow.utils.HardwareUtil
import br.com.andesoncfsilva.weathernow.utils.WNLog
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
class ListWeatherInteractorImpl
@Inject constructor(private val threadExecutor: ThreadExecutor,
                    private val hardwareUtil: HardwareUtil,
                    private val rxPermissions: RxPermissions,
                    private val postExecutionThread: PostExecutionThread,
                    private val weatherApi: WeatherApi,
                    private val mapperCityWeather: MapperCityWeather) : ListWeatherInteractor {

    val radiusMetersSearch = 100000
    val radiusMetersRange = 50000
    val earthSize = 6378000
    val disposable = CompositeDisposable()

    override fun execute(unitTemp: UnitTemp,
                         userLatitude: Double, userLongitude: Double,
                         latitude: Double, longitude: Double,
                         onSuccess: Consumer<in ListCitiesWeather>, onError: Consumer<in Throwable>) {


        disposable.add(Observable.just(hardwareUtil.connected())
                .doOnNext { if (!it) throw NoConnectionException() }

                //get weather list filtered
                .flatMap {

                    //calculate a bounding box around the geo point
                    // 6378000 Size of the Earth (in meters)
                    val longitudeD = Math.asin(radiusMetersSearch / (earthSize * Math.cos(Math.PI * latitude / 180))) * 180 / Math.PI
                    val latitudeD = Math.asin(radiusMetersSearch.toDouble() / earthSize.toDouble()) * 180 / Math.PI

                    val latitudeMax = latitude + latitudeD
                    val latitudeMin = latitude - latitudeD
                    val longitudeMax = longitude + longitudeD
                    val longitudeMin = longitude - longitudeD

                    weatherApi.getCurrentWeather(unitTemp, longitudeMin, latitudeMin, longitudeMax, latitudeMax)
                            .map {
                                WNLog.d("Count Response: ${it?.city?.count()}")
                                mapperCityWeather.convert(it, unitTemp)
                            }
                            .flatMap { Observable.fromIterable(it) }
                            .observeOn(postExecutionThread.scheduler)
                            .subscribeOn(threadExecutor.scheduler)
                }
                .map {
                    it.copy(
                            distance = calculateDistance(it, latitude, longitude),
                            userDistance = calculateDistance(it, userLatitude, userLongitude)
                    )
                }
                .filter { it.distance <= radiusMetersRange }
                .toList()
                .map {
                    WNLog.d("Count Filtered: ${it?.count()}")
                    it.sortBy { c -> c.userDistance }
                    ListCitiesWeather(latitude, longitude, it)
                }
                //handle threads executions
                .observeOn(postExecutionThread.scheduler)
                .subscribeOn(threadExecutor.scheduler)
                .subscribe(onSuccess, onError))
    }

    private fun calculateDistance(city: CityWeather, latitude: Double, longitude: Double): Double {

        val locationA = Location("A")
        locationA.latitude = latitude
        locationA.longitude = longitude
        val locationB = Location("B")
        locationB.latitude = city.latitude
        locationB.longitude = city.longitude
        return locationA.distanceTo(locationB).toDouble()

    }


    override fun unsubscribe() {
        disposable.clear()
    }

}

interface ListWeatherInteractor : Interactor {
    fun execute(unitTemp: UnitTemp,
                userLatitude: Double, userLongitude: Double,
                latitude: Double, longitude: Double,
                onSuccess: Consumer<in ListCitiesWeather>, onError: Consumer<in Throwable>)
}