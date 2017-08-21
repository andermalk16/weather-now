package br.com.andesoncfsilva.weathernow.interactors

import android.Manifest
import android.location.Location
import br.com.andesoncfsilva.weathernow.di.executors.PostExecutionThread
import br.com.andesoncfsilva.weathernow.di.executors.ThreadExecutor
import br.com.andesoncfsilva.weathernow.exception.GPSResolutionRequiredException
import br.com.andesoncfsilva.weathernow.exception.NoGPSException
import br.com.andesoncfsilva.weathernow.exception.NoPermissionException
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
class LocationInteractorImpl
@Inject constructor(private val threadExecutor: ThreadExecutor,
                    private val rxPermissions: RxPermissions,
                    private val postExecutionThread: PostExecutionThread,
                    private val locationProvider: RxLocation) : LocationInteractor {

    val disposable = CompositeDisposable()


    override fun execute(onSuccess: Consumer<in Location>, onError: Consumer<in Throwable>) {


        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(500)


        disposable.add(rxPermissions
                //verify permissions
                .request(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET
                )
                .doOnNext { if (!it) throw NoPermissionException() }

                //check GPS Resolution
                .flatMap {
                    locationProvider.settings()
                            .checkAndHandleResolution(locationRequest)
                            .toObservable()
                            .observeOn(postExecutionThread.scheduler)
                            .subscribeOn(threadExecutor.scheduler)
                }
                .doOnNext { if (!it) throw GPSResolutionRequiredException() }
                //get Location
                .flatMap {
                    locationProvider.location()
                            .updates(locationRequest)
                            .timeout(10, TimeUnit.SECONDS)
                            .firstOrError()
                            .doOnError { throw NoGPSException() }
                            .toObservable()
                            .observeOn(postExecutionThread.scheduler)
                            .subscribeOn(threadExecutor.scheduler)
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

interface LocationInteractor : Interactor {
    fun execute(onSuccess: Consumer<in Location>, onError: Consumer<in Throwable>)
}