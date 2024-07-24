package korlibs.time.core

import korlibs.time.*
import kotlinx.cinterop.*
import platform.posix.*
import kotlin.time.*

@OptIn(ExperimentalForeignApi::class)
actual var CoreTime: ICoreTime = object : ICoreTime {
    override fun currentTimeMillisDouble(): Double = memScoped {
        val timeVal = alloc<timeval>()
        gettimeofday(timeVal.ptr, null)
        val sec = timeVal.tv_sec
        val usec = timeVal.tv_usec
        ((sec * 1_000L) + (usec / 1_000L))
    }.toDouble()

    override fun localTimezoneOffset(time: Long): Duration = memScoped {
        val t = alloc<time_tVar>()
        val tm = alloc<tm>()
        t.value = (time / 1000L).convert()
        localtime_r(t.ptr, tm.ptr)
        tm.tm_gmtoff.toInt().seconds
    }

    override fun unaccurateSleep(duration: Duration) {
        val micros = duration.inWholeMicroseconds
        val s = micros / 1_000_000
        val u = micros % 1_000_000
        if (s > 0) platform.posix.sleep(s.convert())
        if (u > 0) platform.posix.usleep(u.convert())
    }

    override fun unaccurateYield() {
        platform.posix.sched_yield()
    }
}
