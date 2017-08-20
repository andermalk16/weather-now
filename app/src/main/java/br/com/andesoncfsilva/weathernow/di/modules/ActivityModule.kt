package br.com.andesoncfsilva.weathernow.di.modules

import android.app.Activity
import br.com.andesoncfsilva.weathernow.di.scopes.PerActivity
import br.com.andesoncfsilva.weathernow.interactors.ListWeatherInteractor
import br.com.andesoncfsilva.weathernow.interactors.ListWeatherInteractorImpl
import br.com.andesoncfsilva.weathernow.interactors.LocationInteractor
import br.com.andesoncfsilva.weathernow.interactors.LocationInteractorImpl
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
@Module
class ActivityModule(val activity: Activity) {

    @Provides @PerActivity internal fun activity(): Activity = this.activity

    @Provides @PerActivity
    fun providesLocationInteractor(locationInteractor: LocationInteractorImpl): LocationInteractor = locationInteractor

    @Provides @PerActivity
    fun providesLauncherInteractor(listWeatherInteractor: ListWeatherInteractorImpl): ListWeatherInteractor = listWeatherInteractor

    @Provides @PerActivity
    fun providesRxPermissions(activity: Activity): RxPermissions = RxPermissions(activity)

    @Provides @PerActivity
    fun provideRxLocation(activity: Activity): RxLocation = RxLocation(activity)

}