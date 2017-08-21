package br.com.andesoncfsilva.weathernow.presenter

import android.Manifest
import br.com.andesoncfsilva.weathernow.data.MapperCityWeather
import br.com.andesoncfsilva.weathernow.data.WeatherApi
import br.com.andesoncfsilva.weathernow.di.executors.PostExecutionThread
import br.com.andesoncfsilva.weathernow.di.executors.ThreadExecutor
import br.com.andesoncfsilva.weathernow.exception.GPSResolutionRequiredException
import br.com.andesoncfsilva.weathernow.interactors.ListWeatherInteractor
import br.com.andesoncfsilva.weathernow.interactors.ListWeatherInteractorImpl
import br.com.andesoncfsilva.weathernow.interactors.LocationInteractor
import br.com.andesoncfsilva.weathernow.interactors.LocationInteractorImpl
import br.com.andesoncfsilva.weathernow.util.MockHelper
import br.com.andesoncfsilva.weathernow.utils.GeoCalculator
import br.com.andesoncfsilva.weathernow.utils.HardwareUtil
import br.com.andesoncfsilva.weathernow.views.MainPresenter
import br.com.andesoncfsilva.weathernow.views.MainView
import com.patloew.rxlocation.FusedLocation
import com.patloew.rxlocation.LocationSettings
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by afsilva on 21/08/17.
 */
class MainPresenterTest {

    @Mock lateinit var mockPostExecutionThread: PostExecutionThread
    @Mock lateinit var mockThreadExecutor: ThreadExecutor
    @Mock lateinit var mockHardwareUtil: HardwareUtil
    @Mock lateinit var mockWeatherApi: WeatherApi
    @Mock lateinit var mockGeoCalculator: GeoCalculator
    @Mock lateinit var mockMapperCityWeather: MapperCityWeather
    @Mock lateinit var mockRxPermission: RxPermissions
    @Mock lateinit var mockRxLocation: RxLocation
    @Mock lateinit var mockFusedLocation: FusedLocation
    @Mock lateinit var mockLocationSettings: LocationSettings
    @Mock lateinit var mockMainView: MainView

    lateinit var mockListWeatherInteractor: ListWeatherInteractor
    lateinit var mockLocationInteractor: LocationInteractor
    lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setupMockReturns()

        mockLocationInteractor = LocationInteractorImpl(
                mockThreadExecutor,
                mockRxPermission,
                mockPostExecutionThread,
                mockRxLocation)
        mockListWeatherInteractor = ListWeatherInteractorImpl(
                mockThreadExecutor,
                mockHardwareUtil,
                mockPostExecutionThread,
                mockWeatherApi,
                mockMapperCityWeather,
                mockGeoCalculator)

        presenter = MainPresenter(mockListWeatherInteractor, mockLocationInteractor)
        presenter.attachView(mockMainView)
    }

    private fun setupMockReturns() {
        `when`(mockPostExecutionThread.scheduler).thenReturn(Schedulers.trampoline())
        `when`(mockThreadExecutor.scheduler).thenReturn(Schedulers.trampoline())
        `when`(mockMapperCityWeather.convert(MockHelper.currentWeatherResponse, MockHelper.unitTemp)).thenReturn(MockHelper.citiesWeather)
        `when`(mockHardwareUtil.connected()).thenReturn(true)
        `when`(mockGeoCalculator.calculateBox(MockHelper.latitude, MockHelper.longitude)).thenReturn(MockHelper.geoBox)
        `when`(mockGeoCalculator.calculateDistance(MockHelper.cityWeather, MockHelper.latitude, MockHelper.longitude)).thenReturn(50000.0)
        `when`(mockWeatherApi.getCurrentWeather(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Observable.just(MockHelper.currentWeatherResponse))
        `when`(mockRxPermission.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET))
                .thenReturn(Observable.just(true))
        `when`(mockRxLocation.settings()).thenReturn(mockLocationSettings)
        `when`(mockRxLocation.settings().checkAndHandleResolution(MockHelper.locationRequest)).thenReturn(Single.just(true))
        `when`(mockRxLocation.location()).thenReturn(mockFusedLocation)
        `when`(mockRxLocation.location().updates(MockHelper.locationRequest)).thenReturn(Observable.just(MockHelper.location))
    }

    @Test
    fun shouldSetDefaultCameraPositionWithoutErrors(){
        presenter.setDefaultCameraPosition()

        assertThat(presenter.userLatitude).isNotNull()
        assertThat(presenter.userLongitude).isNotNull()
        assertThat(presenter.loading).isFalse()

        verify(mockMainView).hideLoading()
        verify(mockMainView).showFragments()
        verify(mockMainView).refreshMenu()
        verify(mockMainView).setCameraPosition(MockHelper.latitude, MockHelper.longitude)
    }

    @Test
    fun shouldSetDefaultCameraPositionWithNoPermissionException(){
        `when`(mockRxPermission.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET))
                .thenReturn(Observable.just(false))

        presenter.setDefaultCameraPosition()

        assertThat(presenter.userLatitude).isNull()
        assertThat(presenter.userLongitude).isNull()
        assertThat(presenter.loading).isFalse()

        verify(mockMainView).hideLoading()
        verify(mockMainView).showError(any())
        verify(mockMainView, never()).hideFragments()
    }

    @Test
    fun shouldSetDefaultCameraPositionWithGPSResolutionRequiredException(){
        `when`(mockRxLocation.settings().checkAndHandleResolution(MockHelper.locationRequest))
                .thenReturn(Single.just(false))

        presenter.setDefaultCameraPosition()

        assertThat(presenter.userLatitude).isNull()
        assertThat(presenter.userLongitude).isNull()
        assertThat(presenter.loading).isFalse()

        verify(mockMainView).hideLoading()
        verify(mockMainView).showError(any())
        verify(mockMainView).hideFragments()
    }

    @Test
    fun shouldGetWeatherListWithoutErrors(){
        presenter.userLatitude = 0.0
        presenter.userLongitude = 0.0
        presenter.getWeatherList(MockHelper.unitTemp, MockHelper.latitude, MockHelper.longitude)

        assertThat(presenter.loading).isFalse()

        verify(mockMainView).hideLoading()
        verify(mockMainView).showFragments()
        verify(mockMainView).refreshMenu()
        verify(mockMainView).showCitiesWeather(MockHelper.citiesWeather)
    }

    @Test
    fun shouldGetWeatherListWithErrors(){
        `when`(mockHardwareUtil.connected()).thenReturn(false)

        presenter.userLatitude = 0.0
        presenter.userLongitude = 0.0
        presenter.getWeatherList(MockHelper.unitTemp, MockHelper.latitude, MockHelper.longitude)

        assertThat(presenter.loading).isFalse()

        verify(mockMainView).hideLoading()
        verify(mockMainView).showError(any())
    }
}