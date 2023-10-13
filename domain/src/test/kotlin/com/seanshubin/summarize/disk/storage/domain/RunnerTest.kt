package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals

class RunnerTest {
    @Test
    fun runNoArgs() {
        // given
        val args = arrayOf<String>()
        val duration = Duration.ofSeconds(45296)
        val timer = TimerStub(duration)
        val summary1 = Summary(Paths.get("."), size = 1000, files = 1, dirs = 1)
        val summary2 = Summary(Paths.get("./file.txt"), size = 1000, files = 1, dirs = 0)
        val summary3 = Summary(Paths.get("./dir"), size = 0, files = 0, dirs = 1)
        val summaryList = listOf(summary1, summary2, summary3)
        val summaryListMap = mapOf("." to summaryList)
        val scanner = ScannerStub(summaryListMap)
        val summaryFormatMap = mapOf(
            summary1 to "summary-1",
            summary2 to "summary-2",
            summary3 to "summary-3"
        )
        val durationFormatMap = mapOf(
            duration to "duration-string"
        )
        val formatter = FormatterStub(summaryFormatMap, durationFormatMap)
        val emitLine = EmitLineStub()
        val runner = Runner(args, timer, scanner, formatter, emitLine)
        val expected = listOf(
            "summary-1",
            "summary-2",
            "summary-3",
            "duration-string"
        )

        // when
        runner.run()

        // then
        val actual = emitLine.linesEmitted()
        assertEquals(expected, actual)
    }

    @Test
    fun runWithArgs() {
        // given
        val args = arrayOf("base")
        val duration = Duration.ofSeconds(45296)
        val timer = TimerStub(duration)
        val summary1 = Summary(Paths.get("base"), size = 1000, files = 1, dirs = 1)
        val summary2 = Summary(Paths.get("base/file.txt"), size = 1000, files = 1, dirs = 0)
        val summary3 = Summary(Paths.get("base/dir"), size = 0, files = 0, dirs = 1)
        val summaryList = listOf(summary1, summary2, summary3)
        val summaryListMap = mapOf("base" to summaryList)
        val scanner = ScannerStub(summaryListMap)
        val summaryFormatMap = mapOf(
            summary1 to "summary-1",
            summary2 to "summary-2",
            summary3 to "summary-3"
        )
        val durationFormatMap = mapOf(
            duration to "duration-string"
        )
        val formatter = FormatterStub(summaryFormatMap, durationFormatMap)
        val emitLine = EmitLineStub()
        val runner = Runner(args, timer, scanner, formatter, emitLine)
        val expected = listOf(
            "summary-1",
            "summary-2",
            "summary-3",
            "duration-string"
        )

        // when
        runner.run()

        // then
        val actual = emitLine.linesEmitted()
        assertEquals(expected, actual)
    }

    class TimerStub(private val duration: Duration) : Timer {
        override fun <T> measure(f: () -> T): Pair<Duration, T> {
            return Pair(duration, f())
        }
    }

    class ScannerStub(private val summaryListMap: Map<String, List<Summary>>) : Scanner {
        override fun scan(path: Path): List<Summary> =
            summaryListMap.getValue(path.toString())
    }

    class FormatterStub(
        private val summaryFormatMap: Map<Summary, String>,
        private val durationFormatMap: Map<Duration, String>
    ) : Formatter {
        override fun composeSummary(summary: Summary): String =
            summaryFormatMap.getValue(summary)

        override fun composeDuration(duration: Duration): String =
            durationFormatMap.getValue(duration)
    }

    class EmitLineStub : (String) -> Unit {
        private val mutableLinesEmitted = mutableListOf<String>()
        override fun invoke(line: String) {
            mutableLinesEmitted.add(line)
        }

        fun linesEmitted(): List<String> {
            return mutableLinesEmitted
        }
    }
}
