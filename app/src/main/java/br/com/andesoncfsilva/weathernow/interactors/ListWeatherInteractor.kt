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
import br.com.andesoncfsilva.weathernow.utils.GeoCalculator
import br.com.andesoncfsilva.weathernow.utils.HardwareUtil
import br.com.andesoncfsilva.weathernow.utils.WNLog
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
                    private val postExecutionThread: PostExecutionThread,
                    private val weatherApi: WeatherApi,
                    private val mapperCityWeather: MapperCityWeather,
                    private val geoCalculator: GeoCalculator) : ListWeatherInteractor {

    val radiusMetersRange = 50000
    val disposable = CompositeDisposable()

    override fun execute(unitTemp: UnitTemp,
                         userLatitude: Double, userLongitude: Double,
                         latitude: Double, longitude: Double,
                         onSuccess: Consumer<in ListCitiesWeather>, onError: Consumer<in Throwable>) {


        disposable.add(Observable.just(hardwareUtil.connected())
                .doOnNext { if (!it) throw NoConnectionException() }

                //get weather list filtered
                .flatMap {
                    val geo = geoCalculator.calculateBox(latitude, longitude)
                    weatherApi.getCurrentWeather(unitTemp, geo.longitudeMin, geo.latitudeMin, geo.longitudeMax, geo.latitudeMax)
                            .flatMap { Observable.just(mapperCityWeather.convert(it, unitTemp)) }
                            .flatMap { Observable.fromIterable(it) }
                            .observeOn(postExecutionThread.scheduler)
                            .subscribeOn(threadExecutor.scheduler)
                }
                .map {
                    it.copy(
                            distance = geoCalculator.calculateDistance(it, latitude, longitude),
                            userDistance = geoCalculator.calculateDistance(it, userLatitude, userLongitude)
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