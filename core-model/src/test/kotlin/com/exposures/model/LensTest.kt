package com.exposures.model

import org.junit.Assert.assertEquals
import org.junit.Test

class LensTest {

    private fun lens(
        lensType: LensType,
        focalLengthMm: Int? = null,
        focalLengthMinMm: Int? = null,
        focalLengthMaxMm: Int? = null,
    ) = Lens(
        id = "lens-1",
        name = "Test lens",
        cameraBodyId = null,
        minAperture = 2.8,
        maxAperture = 32.0,
        stopIncrement = StopIncrement.HALF_STOP,
        referencePhotoZoomRatio = 1.0,
        lensType = lensType,
        focalLengthMm = focalLengthMm,
        focalLengthMinMm = focalLengthMinMm,
        focalLengthMaxMm = focalLengthMaxMm,
        createdAt = 0L,
        updatedAt = 0L,
        syncStatus = SyncStatus.SYNCED,
        remoteId = null,
    )

    @Test
    fun `a prime lens offers its single focal length`() {
        assertEquals(listOf(50), lens(LensType.PRIME, focalLengthMm = 50).availableFocalLengths())
    }

    @Test
    fun `a prime lens with no focal length set offers nothing`() {
        assertEquals(emptyList<Int>(), lens(LensType.PRIME).availableFocalLengths())
    }

    @Test
    fun `a zoom lens offers every whole mm across its range`() {
        assertEquals(
            (24..70).toList(),
            lens(LensType.ZOOM, focalLengthMinMm = 24, focalLengthMaxMm = 70).availableFocalLengths(),
        )
    }

    @Test
    fun `a zoom lens with a single-mm range offers just that value`() {
        assertEquals(listOf(50), lens(LensType.ZOOM, focalLengthMinMm = 50, focalLengthMaxMm = 50).availableFocalLengths())
    }

    @Test
    fun `a zoom lens missing min or max offers nothing rather than crashing`() {
        assertEquals(emptyList<Int>(), lens(LensType.ZOOM, focalLengthMinMm = 24).availableFocalLengths())
        assertEquals(emptyList<Int>(), lens(LensType.ZOOM, focalLengthMaxMm = 70).availableFocalLengths())
        assertEquals(emptyList<Int>(), lens(LensType.ZOOM).availableFocalLengths())
    }

    @Test
    fun `a zoom lens with an inverted range offers nothing rather than crashing`() {
        assertEquals(emptyList<Int>(), lens(LensType.ZOOM, focalLengthMinMm = 70, focalLengthMaxMm = 24).availableFocalLengths())
    }
}
