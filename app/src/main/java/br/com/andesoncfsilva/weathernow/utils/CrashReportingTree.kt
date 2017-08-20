package br.com.andesoncfsilva.weathernow.utils

import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import timber.log.Timber


/**
 * Created by anderson.silva on 17/08/17.
 *
 */
class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String, message: String, e: Throwable?) {
        try {

            FirebaseCrash.log(message)

            e?.let {
                if (priority == Log.ERROR) {
                    FirebaseCrash.report(it)
                }
            }

        } catch (e: Exception) {
            Log.e("Log", "Erro Log Firebase", e)
        }
    }

}