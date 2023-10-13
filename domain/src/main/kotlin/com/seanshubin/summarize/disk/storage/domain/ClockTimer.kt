package com.seanshubin.summarize.disk.storage.domain

import java.time.Clock
import java.time.Duration

class ClockTimer(private val clock: Clock) : Timer {
    override fun <T> measure(f: () -> T): Pair<Duration, T> {
        val before = clock.instant()
        val result = f()
        val after = clock.instant()
        val duration = Duration.between(before, after)
        return Pair(duration, result)
    }
}
