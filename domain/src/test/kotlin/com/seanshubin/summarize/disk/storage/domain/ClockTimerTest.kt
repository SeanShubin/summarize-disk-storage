package com.seanshubin.summarize.disk.storage.domain

import org.junit.Test
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

class ClockTimerTest {
    @Test
    fun measure() {
        // given
        val firstTime = Instant.ofEpochMilli(5000)
        val secondTime = Instant.ofEpochMilli(8000)
        val clock = ClockStub(firstTime, secondTime)
        val clockTimer = ClockTimer(clock)
        val expectedDuration = Duration.of(3, ChronoUnit.SECONDS)
        val expectedResult = "result"

        // when
        val (actualDuration, actualResult) = clockTimer.measure { "result" }

        // then
        assertEquals(expectedDuration, actualDuration)
        assertEquals(expectedResult, actualResult)
    }

    class ClockStub(vararg val times:Instant) : Clock() {
        var index =0
        override fun instant(): Instant {
            return times[index++]
        }

        override fun withZone(zone: ZoneId?): Clock {
            throw UnsupportedOperationException("not implemented")
        }

        override fun getZone(): ZoneId {
            throw UnsupportedOperationException("not implemented")
        }
    }
}
