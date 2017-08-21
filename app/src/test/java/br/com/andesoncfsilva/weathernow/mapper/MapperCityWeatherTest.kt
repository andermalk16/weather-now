package br.com.andesoncfsilva.weathernow.mapper

import br.com.andesoncfsilva.weathernow.data.MapperCityWeatherImpl
import br.com.andesoncfsilva.weathernow.interactors.ListWeatherInteractor
import br.com.andesoncfsilva.weathernow.interactors.ListWeatherInteractorImpl
import br.com.andesoncfsilva.weathernow.util.MockHelper
import br.com.andesoncfsilva.weathernow.utils.OpenWeatherMapUtil
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Created by afsilva on 21/08/17.
 *
 */
class MapperCityWeatherTest{

    @Mock lateinit var openWeatherMapUtil: OpenWeatherMapUtil

    lateinit var mapper: MapperCityWeatherImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(openWeatherMapUtil.getKey()).thenReturn(MockHelper.openMapKey)
        `when`(openWeatherMapUtil.getUrlIcon(any())).thenReturn(MockHelper.openMapIconUrl)

        mapper = MapperCityWeatherImpl(openWeatherMapUtil)
    }

    @Test
    fun shouldReturnsResponseApiMappedObject(){
        val list = mapper.convert(MockHelper.currentWeatherResponse, MockHelper.unitTemp)
        assertThat(list).isNotEmpty
        assertThat(list?.count()).isEqualTo(MockHelper.currentWeatherResponse.city?.count())
    }

    @Test
    fun shouldReturnsNullObject(){
        val list = mapper.convert(null, MockHelper.unitTemp)
        assertThat(list).isNull()
    }
}