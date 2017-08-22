package br.com.andesoncfsilva.weathernow

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import br.com.andesoncfsilva.weathernow.di.components.AppComponent
import br.com.andesoncfsilva.weathernow.di.components.DaggerAppComponent
import br.com.andesoncfsilva.weathernow.di.modules.AppModule
import br.com.andesoncfsilva.weathernow.utils.CrashReportingTree
import timber.log.Timber

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
class WeatherNowApplication : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        timberConfig()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun timberConfig() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}