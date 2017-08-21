package br.com.andesoncfsilva.weathernow.interactor

import android.Manifest
import android.location.Location
import br.com.andesoncfsilva.weathernow.di.executors.PostExecutionThread
import br.com.andesoncfsilva.weathernow.di.executors.ThreadExecutor
import br.com.andesoncfsilva.weathernow.exception.GPSResolutionRequiredException
import br.com.andesoncfsilva.weathernow.exception.NoGPSException
import br.com.andesoncfsilva.weathernow.exception.NoPermissionException
import br.com.andesoncfsilva.weathernow.interactors.LocationInteractor
import br.com.andesoncfsilva.weathernow.interactors.LocationInteractorImpl
import br.com.andesoncfsilva.weathernow.util.MockHelper
import com.patloew.rxlocation.FusedLocation
import com.patloew.rxlocation.LocationSettings
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by afsilva on 21/08/17.
 *
 */
class LocationInteractorTest {

    @Mock lateinit var mockRxPermission: RxPermissions
    @Mock lateinit var mockPostExecutionThread: PostExecutionThread
    @Mock lateinit var mockThreadExecutor: ThreadExecutor
    @Mock lateinit var mockRxLocation: RxLocation
    @Mock lateinit var mockFusedLocation: FusedLocation
    @Mock lateinit var mockLocationSettings: LocationSettings

    lateinit var mockLocationInteractor: LocationInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(mockPostExecutionThread.scheduler).thenReturn(Schedulers.trampoline())
        `when`(mockThreadExecutor.scheduler).thenReturn(Schedulers.trampoline())

        mockLocationInteractor = LocationInteractorImpl(
                mockThreadExecutor,
                mockRxPermission,
                mockPostExecutionThread,
                mockRxLocation)
    }

    @Test
    fun shouldExecuteInteractorWithoutErrors() {
        var executeOk = false
        var result: Location? = null

        `when`(mockRxPermission.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET))
                .thenReturn(Observable.just(true))
        `when`(mockRxLocation.settings()).thenReturn(mockLocationSettings)
        `when`(mockRxLocation.settings().checkAndHandleResolution(MockHelper.locationRequest)).thenReturn(Single.just(true))
        `when`(mockRxLocation.location()).thenReturn(mockFusedLocation)
        `when`(mockRxLocation.location().updates(MockHelper.locationRequest)).thenReturn(Observable.just(MockHelper.location))

        mockLocationInteractor.execute(
                Consumer { executeOk = true; result = it },
                Consumer { Assertions.fail("error", it) })

        assertThat(executeOk).isTrue()
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(MockHelper.location)


        verify(mockRxPermission).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET)
        verify(mockRxLocation.settings()).checkAndHandleResolution(MockHelper.locationRequest)
        verify(mockRxLocation.location()).updates(MockHelper.locationRequest)

    }

    @Test
    fun shouldThrowsNoPermissionException() {
        var executeOk = false
        var result: Location? = null
        var error: Throwable? = null

        `when`(mockRxPermission.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET))
                .thenReturn(Observable.just(false))
        `when`(mockRxLocation.settings()).thenReturn(mockLocationSettings)
        `when`(mockRxLocation.settings().checkAndHandleResolution(MockHelper.locationRequest)).thenReturn(Single.just(true))
        `when`(mockRxLocation.location()).thenReturn(mockFusedLocation)
        `when`(mockRxLocation.location().updates(MockHelper.locationRequest)).thenReturn(Observable.just(MockHelper.location))

        mockLocationInteractor.execute(
                Consumer { executeOk = true; result = it },
                Consumer { error = it })

        assertThat(executeOk).isFalse()
        assertThat(result).isNull()
        assertThat(error).isInstanceOf(NoPermissionException::class.java)


        verify(mockRxPermission).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET)
        verify(mockRxLocation.settings(), never()).checkAndHandleResolution(MockHelper.locationRequest)
        verify(mockRxLocation.location(), never()).updates(MockHelper.locationRequest)
    }

    @Test
    fun shouldThrowsGPSResolutionRequiredException() {
        var executeOk = false
        var result: Location? = null
        var error: Throwable? = null

        `when`(mockRxPermission.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET))
                .thenReturn(Observable.just(true))
        `when`(mockRxLocation.settings()).thenReturn(mockLocationSettings)
        `when`(mockRxLocation.settings().checkAndHandleResolution(MockHelper.locationRequest)).thenReturn(Single.just(false))
        `when`(mockRxLocation.location()).thenReturn(mockFusedLocation)
        `when`(mockRxLocation.location().updates(MockHelper.locationRequest)).thenReturn(Observable.just(MockHelper.location))

        mockLocationInteractor.execute(
                Consumer { executeOk = true; result = it },
                Consumer { error = it })

        assertThat(executeOk).isFalse()
        assertThat(result).isNull()
        assertThat(error).isInstanceOf(GPSResolutionRequiredException::class.java)


        verify(mockRxPermission).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET)
        verify(mockRxLocation.settings()).checkAndHandleResolution(MockHelper.locationRequest)
        verify(mockRxLocation.location(), never()).updates(MockHelper.locationRequest)
    }
}