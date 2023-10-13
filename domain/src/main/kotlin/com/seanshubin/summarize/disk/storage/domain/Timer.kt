package com.seanshubin.summarize.disk.storage.domain

import java.time.Duration

interface Timer {
    fun <T> measure(f: () -> T): Pair<Duration, T>
}
