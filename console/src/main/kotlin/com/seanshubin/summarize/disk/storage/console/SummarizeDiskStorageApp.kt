package com.seanshubin.summarize.disk.storage.console

object SummarizeDiskStorageApp {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies(args).runner.run()
    }
}
