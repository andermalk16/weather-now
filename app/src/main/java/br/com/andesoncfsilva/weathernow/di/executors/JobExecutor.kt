package br.com.andesoncfsilva.weathernow.di.executors

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject


/**
 * Decorated [java.util.concurrent.ThreadPoolExecutor]
 */

class JobExecutor @Inject constructor() : ThreadExecutor {

    override val scheduler: Scheduler
        get() {
            return Schedulers.from(myThreadPoolExecutor)
        }

    override val executor: Executor
        get() {
            return myThreadPoolExecutor
        }

    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE: Int = CPU_COUNT + 1
    private val MAXIMUM_POOL_SIZE: Int = CPU_COUNT * 2 + 1
    private val KEEP_ALIVE: Long = 1

    private val yourFactory = object : ThreadFactory {
        private val mCount = AtomicInteger(1)

        override fun newThread(r: Runnable): Thread {
            val group = ThreadGroup("ThreadGroup")
            return Thread(group, r, "BackgroundThread #" + mCount.andIncrement, 2000000)
        }
    }

    private val sPoolWorkQueue = LinkedBlockingQueue<Runnable>(128)


    val myThreadPoolExecutor: Executor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, yourFactory)
}
