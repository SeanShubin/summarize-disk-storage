package com.seanshubin.summarize.disk.storage.domain

import java.time.Duration

class LineFormatter : Formatter {
    override fun composeSummary(summary: Summary): String =
        "${summary.size} ${summary.path} files:${summary.files} dirs:${summary.dirs}"

    override fun composeDuration(duration: Duration): String {
        val hours = String.format("%02d", duration.toHoursPart())
        val minutes = String.format("%02d", duration.toMinutesPart())
        val seconds = String.format("%02d", duration.toSecondsPart())
        return "$hours:$minutes:$seconds"
    }
}
