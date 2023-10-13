package com.seanshubin.summarize.disk.storage.console

import com.seanshubin.summarize.disk.storage.domain.*
import java.time.Clock

class Dependencies(args:Array<String>) {
    private val clock:Clock = Clock.systemUTC()
    private val timer: Timer = ClockTimer(clock)
    private val files:FilesContract = FilesDelegate
    private val scanner: Scanner = FileScanner(files)
    private val formatter: Formatter = LineFormatter()
    private val emitLine:(String)->Unit = ::println
    val runner:Runnable = Runner(args, timer, scanner, formatter, emitLine)
}
