package com.seanshubin.summarize.disk.storage.domain

import java.time.Duration

interface Formatter {
    fun composeSummary(summary: Summary): String
    fun composeDuration(duration: Duration): String
}
