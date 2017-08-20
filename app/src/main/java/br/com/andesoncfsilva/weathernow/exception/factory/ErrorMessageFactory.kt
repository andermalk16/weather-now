package br.com.andesoncfsilva.weathernow.exception.factory

import android.content.Context
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.di.scopes.PerActivity
import br.com.andesoncfsilva.weathernow.exception.GPSResolutionRequiredException
import br.com.andesoncfsilva.weathernow.exception.NoConnectionException
import br.com.andesoncfsilva.weathernow.exception.NoGPSException
import br.com.andesoncfsilva.weathernow.exception.NoPermissionException
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
        } else if (exception is ConnectionShutdownException) {
            WNLog.w(exception)
            context.getString(R.string.error_interrupted_connection)
        } else if (exception is NoConnectionException || exception is UnknownHostException || exception is SocketException) {
            WNLog.w(exception)
            context.getString(R.string.error_no_connection)
        } else if (exception is NoPermissionException) {
            WNLog.w(exception)
            context.getString(R.string.error_permission)
        } else if (exception is SocketTimeoutException) {
            WNLog.w(exception)
            context.getString(R.string.error_timeout)
        } else if (exception is HttpException) {
            WNLog.e(exception)
            exception.message ?: context.getString(R.string.error_generic)
        } else {
            WNLog.e(exception)
            context.getString(R.string.error_generic)
        }
    }
}