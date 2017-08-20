package br.com.andesoncfsilva.weathernow.views.base

/**
 * Created by Anderson Silva on 16/08/17.
 */

open class MvpPresenter<V : MvpView> : Presenter<V> {

    private var viewRef: java.lang.ref.WeakReference<V>? = null

    override fun attachView(view: V) {
        viewRef = java.lang.ref.WeakReference(view)
    }

    /**
     * @return `null`, if view is not attached, otherwise the concrete view instance
     */
    val view: V?
        get() = viewRef?.get()

    /**
     * Checks if a view is attached to this presenter.
     */
    val isViewAttached: Boolean
        get() = viewRef?.get() != null

    override fun detachView() {
        if (isViewAttached) {
            viewRef?.clear()
            viewRef = null
        }
    }
}

