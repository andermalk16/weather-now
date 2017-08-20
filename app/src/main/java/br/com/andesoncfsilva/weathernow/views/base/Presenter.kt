package br.com.andesoncfsilva.weathernow.views.base

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
interface Presenter<in V : MvpView> {
    /**
     * Set or attach the view to this presenter
     */
    fun attachView(view: V)

    /**
     * Will be called if the view has been destroyed.
     */
    fun detachView()
}