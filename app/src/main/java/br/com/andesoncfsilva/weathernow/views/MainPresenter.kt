package br.com.andesoncfsilva.weathernow.views

import br.com.andesoncfsilva.weathernow.di.scopes.PerActivity
import br.com.andesoncfsilva.weathernow.entities.UnitTemp
import br.com.andesoncfsilva.weathernow.exception.GPSResolutionRequiredException
import br.com.andesoncfsilva.weathernow.exception.NoGPSException
import br.com.andesoncfsilva.weathernow.interactors.ListWeatherInteractor
import br.com.andesoncfsilva.weathernow.interactors.LocationInteractor
import br.com.andesoncfsilva.weathernow.views.base.MvpPresenter
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
@PerActivity
class MainPresenter @Inject constructor(private val listWeatherInteractor: ListWeatherInteractor,
                                        private val locationInteractor: LocationInteractor
) : MvpPresenter<MainView>() {

    var loading = false
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    override fun detachView() {
        listWeatherInteractor.unsubscribe()
        super.detachView()
    }

    fun setDefaultCameraPosition() {
        view?.showLoading()
        locationInteractor.execute(
                //onSuccessr
                Consumer {
                    userLatitude = it.latitude
                    userLongitude = it.longitude
                    view?.showFragments()
                    view?.refreshMenu()
                    view?.setCameraPosition(it.latitude, it.longitude)
                },
                //onError
                Consumer {
                    view?.hideLoading()
                    view?.showError(it)
                    if (it is NoGPSException || it is GPSResolutionRequiredException)
                        view?.hideFragments()
                })
    }

    fun getWeatherList(unitTemp: UnitTemp, latitude: Double, longitude: Double) {
        if (!loading) {
            loading = true
            view?.showLoading()
            listWeatherInteractor.execute(unitTemp, userLatitude, userLongitude, latitude, longitude,
                    //onSuccess
                    Consumer {
                        loading = false
                        view?.hideLoading()
                        view?.showFragments()
                        view?.refreshMenu()
                        view?.showCitiesWeather(it.cities)
                    },
                    //onError
                    Consumer {
                        loading = false
                        view?.hideLoading()
                        view?.showError(it)
                    })
        }

    }
}