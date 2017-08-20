package br.com.andesoncfsilva.weathernow.di.executors

import io.reactivex.Scheduler
import java.util.concurrent.Executor

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute the
 */
interface ThreadExecutor {
    val scheduler: Scheduler
    val executor: Executor
}