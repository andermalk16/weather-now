package br.com.andesoncfsilva.weathernow.utils

import android.util.Log
import timber.log.Timber


/**
 * Created by anderson.silva on 17/08/17.
 *
 */
class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String, message: String, e: Throwable?) {
        try {
            //TODO: CALL FIREBASE AND FABRIC

        } catch (e: Exception) {
            Log.e(WNLog.TAG, "Erro Log", e)
        }
    }

}