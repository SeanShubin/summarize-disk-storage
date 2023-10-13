package com.seanshubin.summarize.disk.storage.domain

import java.nio.file.Paths
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals

class LineFormatterTest {
    @Test
    fun summary(){
        // given
        val lineFormatter = LineFormatter()
        val path = Paths.get("the-path")
        val size = 1000L
        val files = 100
        val dirs = 10
        val summary = Summary(path, size, files, dirs)
        val expected = "1000 the-path files:100 dirs:10"

        // when
        val actual = lineFormatter.composeSummary(summary)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun duration(){
        // given
        val lineFormatter = LineFormatter()
        val duration = Duration.ofMillis(45296000L)
        val expected = "12:34:56"

        // when
        val actual = lineFormatter.composeDuration(duration)

        // then
        assertEquals(expected, actual)
    }
}
