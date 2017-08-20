package br.com.andesoncfsilva.weathernow.views.base

import android.support.v7.app.AppCompatActivity
import br.com.andesoncfsilva.weathernow.di.components.ActivityComponent
import br.com.andesoncfsilva.weathernow.di.components.DaggerActivityComponent
import br.com.andesoncfsilva.weathernow.di.modules.ActivityModule
import br.com.andesoncfsilva.weathernow.utils.app

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
abstract class MvpActivity<V : MvpView, P : Presenter<V>> : AppCompatActivity(), MvpView {

    var presenter: P? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        initializeInjector()
        presenter = createPresenter()
        attachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        detachView()
    }

    val component: ActivityComponent
        get() = DaggerActivityComponent.builder()
                .appComponent(getApplicationComponent())
                .activityModule(ActivityModule(this))
                .build()

    fun getApplicationComponent() = app.component

    abstract fun initializeInjector()

    /**
     * Instantiate a presenter instance

     * @return The [Presenter] for this view
     */
    abstract fun createPresenter(): P

    /**
     * Attaches the view to the presenter
     */
    internal fun attachView() {
        presenter?.attachView(this as V)
    }

    /**
     * Called to detach the view from presenter
     */
    internal fun detachView() {
        presenter?.detachView()
    }

    val mvpView: V
        get() = this as V

}
