package br.com.andesoncfsilva.weathernow.utils

import timber.log.Timber

/**
 * Created by anderson.silva on 17/08/17.
 *
 */
object WNLog {
    val TAG = "WeatherNowLog"


    fun v(msg: String) {
        Timber.tag(TAG).v(msg)
    }

    fun v(msg: String, tr: Throwable) {
        Timber.tag(TAG).v(msg, tr)
    }

    fun d(msg: String) {
        Timber.tag(TAG).d(msg)
    }

    fun d(msg: String, tr: Throwable) {
        Timber.tag(TAG).d(msg, tr)
    }

    fun i(msg: String) {
        Timber.tag(TAG).i(msg)
    }

    fun i(msg: String, tr: Throwable) {
        Timber.tag(TAG).i(msg, tr)
    }

    fun w(msg: String) {
        Timber.tag(TAG).w(msg)
    }

    fun w(msg: String, tr: Throwable) {
        Timber.tag(TAG).w(msg, tr)
    }

    fun w(tr: Throwable) {
        Timber.tag(TAG).w(tr)
    }

    fun e(msg: String) {
        Timber.tag(TAG).e(msg)
    }

    fun e(msg: String, tr: Throwable) {
        Timber.tag(TAG).e(msg, tr)
    }

    fun e(tr: Throwable) {
        Timber.tag(TAG).e(tr)
    }
}
