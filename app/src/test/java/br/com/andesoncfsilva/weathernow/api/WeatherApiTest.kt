package br.com.andesoncfsilva.weathernow.api

import br.com.andesoncfsilva.weathernow.BuildConfig
import br.com.andesoncfsilva.weathernow.data.RestApi
import br.com.andesoncfsilva.weathernow.data.WeatherApi
import br.com.andesoncfsilva.weathernow.data.WeatherApiImpl
import br.com.andesoncfsilva.weathernow.exception.RestAPIException
import br.com.andesoncfsilva.weathernow.util.MockHelper
import br.com.andesoncfsilva.weathernow.utils.OpenWeatherMapUtil
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by afsilva on 21/08/17.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class WeatherApiTest {

    @Mock lateinit var mockRestApi: RestApi
    @Mock lateinit var mockOpenWeatherMapUtil: OpenWeatherMapUtil
    lateinit var mockWeatherApi: WeatherApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockWeatherApi = WeatherApiImpl(mockRestApi, mockOpenWeatherMapUtil)
    }

    @Test
    fun shouldReturnsApiObjectResponse() {
        `when`(mockOpenWeatherMapUtil.getKey()).thenReturn(MockHelper.openMapKey)
        `when`(mockRestApi.getCurrentWeather(
                MockHelper.openMapKey,
                MockHelper.geoBox.toString(),
                MockHelper.unitTemp.toString(),
                "pt")).thenReturn(Observable.just(MockHelper.currentWeatherResponse))

        mockWeatherApi.getCurrentWeather(
                MockHelper.unitTemp,
                MockHelper.geoBox.longitudeMin,
                MockHelper.geoBox.latitudeMin,
                MockHelper.geoBox.longitudeMax,
                MockHelper.geoBox.longitudeMax)
                .test()
                .assertResult(MockHelper.currentWeatherResponse)

        verify(mockOpenWeatherMapUtil).getKey()
        verify(mockRestApi).getCurrentWeather(
                MockHelper.openMapKey,
                MockHelper.geoBox.toString(),
                MockHelper.unitTemp.toString(),
                "pt")
    }

    @Test
    fun shouldThrowsRestAPIException() {
        `when`(mockOpenWeatherMapUtil.getKey()).thenReturn(MockHelper.openMapKey)
        `when`(mockRestApi.getCurrentWeather(
                MockHelper.openMapKey,
                MockHelper.geoBox.toString(),
                MockHelper.unitTemp.toString(),
                "pt")).thenReturn(Observable.error(RestAPIException(RuntimeException())))

        mockWeatherApi.getCurrentWeather(
                MockHelper.unitTemp,
                MockHelper.geoBox.longitudeMin,
                MockHelper.geoBox.latitudeMin,
                MockHelper.geoBox.longitudeMax,
                MockHelper.geoBox.longitudeMax)
                .test()
                .onError(RestAPIException(RuntimeException()))

        verify(mockOpenWeatherMapUtil).getKey()
        verify(mockRestApi).getCurrentWeather(
                MockHelper.openMapKey,
                MockHelper.geoBox.toString(),
                MockHelper.unitTemp.toString(),
                "pt")
    }
}
