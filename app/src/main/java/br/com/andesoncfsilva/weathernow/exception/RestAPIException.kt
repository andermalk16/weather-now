package br.com.andesoncfsilva.weathernow.exception

/**
 * Created by Anderson Silva on 16/08/17.
 *
 */
class RestAPIException(override val cause: Throwable?) : RuntimeException(cause = cause)