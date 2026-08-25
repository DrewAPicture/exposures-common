package com.exposures.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ExposureValueTest {

    @Test
    fun `exposure values 1 through 20 are labeled with their plain number`() {
        val expected = (1..20).map(Int::toString)

        val actual = (1..20).map(ExposureValue::label)

        assertEquals(expected, actual)
    }

    @Test
    fun `default exposure value is 10`() {
        assertEquals("10", ExposureValue.label(ExposureValue.DEFAULT))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects an exposure value below the minimum`() {
        ExposureValue.label(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects an exposure value above the maximum`() {
        ExposureValue.label(21)
    }
}
