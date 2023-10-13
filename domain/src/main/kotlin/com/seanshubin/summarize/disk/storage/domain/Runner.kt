package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.Paths

class Runner(
    private val args: Array<String>,
    private val timer: Timer,
    private val scanner: Scanner,
    private val formatter: Formatter,
    private val emitLine: (String)->Unit
) : Runnable {
    override fun run() {
        val pathName = args.getOrNull(0) ?: "."
        val path = Paths.get(pathName)
        val (duration, summaryLines) = timer.measure {
            val summaries = scanner.scan(path)
            val summaryLines = summaries.map { formatter.composeSummary(it) }
            summaryLines
        }
        val durationLine = formatter.composeDuration(duration)
        val allLines = summaryLines + listOf(durationLine)
        allLines.forEach(emitLine)
    }
}
