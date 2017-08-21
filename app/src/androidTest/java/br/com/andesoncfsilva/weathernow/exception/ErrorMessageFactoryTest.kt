package br.com.andesoncfsilva.weathernow.exception

import android.content.Context
import android.support.test.runner.AndroidJUnit4
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.exception.factory.ErrorMessageFactory
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.InstrumentationRegistry
import android.test.RenamingDelegatingContext
import org.junit.Before



/**
 * Created by afsilva on 21/08/17.
 */

@RunWith(AndroidJUnit4::class)
class ErrorMessageFactoryTest {


    lateinit var errorMessageFactory: ErrorMessageFactory

    lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockContext = RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_")
        errorMessageFactory = ErrorMessageFactory(mockContext)
    }

    @Test
    fun shouldReturnsNoGPSExceptionMessage() {
        errorMessageFactory = ErrorMessageFactory(mockContext)
        val expectedMessage = mockContext.getString(R.string.error_no_gps)
        val actualMessage = errorMessageFactory.create(NoGPSException())
        assert(actualMessage == expectedMessage)
    }

    @Test
    fun shouldReturnsGPSResolutionRequiredExceptionMessage() {
        errorMessageFactory = ErrorMessageFactory(mockContext)
        val expectedMessage = mockContext.getString(R.string.error_no_gps)
        val actualMessage = errorMessageFactory.create(GPSResolutionRequiredException())
        assert(actualMessage == expectedMessage)
    }

    @Test
    fun shouldReturnsNoConnectionExceptionMessage() {
        errorMessageFactory = ErrorMessageFactory(mockContext)
        val expectedMessage = mockContext.getString(R.string.error_no_connection)
        val actualMessage = errorMessageFactory.create(NoConnectionException())
        assert(actualMessage == expectedMessage)
    }

    @Test
    fun shouldReturnsNoPermissionExceptionMessage() {
        errorMessageFactory = ErrorMessageFactory(mockContext)
        val expectedMessage = mockContext.getString(R.string.error_permission)
        val actualMessage = errorMessageFactory.create(NoPermissionException())
        assert(actualMessage == expectedMessage)
    }

    @Test
    fun shouldReturnsRestAPIExceptionMessage() {
        errorMessageFactory = ErrorMessageFactory(mockContext)
        val expectedMessage = mockContext.getString(R.string.error_connection)
        val actualMessage = errorMessageFactory.create(RestAPIException(RuntimeException()))
        assert(actualMessage == expectedMessage)
    }

    @Test
    fun shouldReturnsDefaultMessage() {
        errorMessageFactory = ErrorMessageFactory(mockContext)
        val expectedMessage = mockContext.getString(R.string.error_generic)
        val actualMessage = errorMessageFactory.create(RuntimeException())
        assert(actualMessage == expectedMessage)
    }
}