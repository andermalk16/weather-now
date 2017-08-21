package br.com.andesoncfsilva.weathernow.exception.factory

import android.content.Context
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.di.scopes.PerActivity
import br.com.andesoncfsilva.weathernow.exception.*
import br.com.andesoncfsilva.weathernow.utils.WNLog
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
@PerActivity
class ErrorMessageFactory @Inject constructor(private val context: Context) {

    fun create(exception: Throwable): String {
        return if (exception is NoGPSException || exception is GPSResolutionRequiredException) {
            WNLog.w(exception)
            context.getString(R.string.error_no_gps)
        } else if (exception is NoConnectionException) {
            WNLog.w(exception)
            context.getString(R.string.error_no_connection)
        } else if (exception is NoPermissionException) {
            WNLog.w(exception)
            context.getString(R.string.error_permission)
        } else if (exception is RestAPIException) {
            WNLog.w(exception)
            context.getString(R.string.error_connection)
        } else {
            WNLog.e(exception)
            context.getString(R.string.error_generic)
        }
    }
}