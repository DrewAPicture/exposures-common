package com.exposures.datalayer.mapper

import com.exposures.model.CameraBody
import com.exposures.model.Exposure
import com.exposures.model.FilmBack
import com.exposures.model.FilmBackType
import com.exposures.model.FilmColorType
import com.exposures.model.FilmFormat
import com.exposures.model.FilmMedium
import com.exposures.model.FilmMediumStatus
import com.exposures.model.FilmMediumType
import com.exposures.model.Lens
import com.exposures.model.LightMeter
import com.exposures.model.LightMeterType
import com.exposures.model.PhotoStatus
import com.exposures.model.ShutterSpeed
import com.exposures.model.StopIncrement
import com.exposures.model.SyncStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class DtoMappersTest {

    @Test
    fun `camera body round-trips through its dto, independent of local sync status`() {
        val original = CameraBody(
            id = "body-1",
            name = "RZ67 Pro II",
            manufacturer = "Mamiya",
            availableShutterSpeeds = ShutterSpeed.standardRange(ShutterSpeed.fraction(400), ShutterSpeed.wholeSeconds(8)),
            hasBulbMode = true,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.PENDING_SYNC,
            remoteId = null,
        )

        val roundTripped = original.toDto().toDomain(syncStatus = SyncStatus.SYNCED)

        assertEquals(original.copy(syncStatus = SyncStatus.SYNCED), roundTripped)
    }

    @Test
    fun `lens round-trips through its dto`() {
        val original = Lens(
            id = "lens-1",
            name = "110mm f/2.8 W",
            cameraBodyId = "body-1",
            minAperture = 2.8,
            maxAperture = 32.0,
            stopIncrement = StopIncrement.HALF_STOP,
            referencePhotoZoomRatio = 3.0,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.SYNCED,
            remoteId = "remote-1",
        )

        assertEquals(original, original.toDto().toDomain(syncStatus = SyncStatus.SYNCED))
    }

    @Test
    fun `light meter round-trips through its dto`() {
        val original = LightMeter(
            id = "meter-1",
            name = "Spotmeter V",
            manufacturer = "Pentax",
            type = LightMeterType.SPOT,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.SYNCED,
            remoteId = "remote-1",
        )

        assertEquals(original, original.toDto().toDomain(syncStatus = SyncStatus.SYNCED))
    }

    @Test
    fun `film back round-trips through its dto`() {
        val original = FilmBack(
            id = "back-1",
            name = "6x7 back",
            cameraBodyId = "body-1",
            type = FilmBackType.ROLL_6X7,
            availableFrameCounts = listOf(10, 11),
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.SYNCED,
            remoteId = "remote-1",
        )

        assertEquals(original, original.toDto().toDomain(syncStatus = SyncStatus.SYNCED))
    }

    @Test
    fun `film medium round-trips through its dto, including an unset light meter`() {
        val original = FilmMedium(
            id = "medium-1",
            name = "Portra 400 — Film 1",
            filmStock = "Kodak Portra 400",
            boxSpeedIso = 400,
            format = FilmFormat.MEDIUM_FORMAT_120,
            colorType = FilmColorType.COLOR,
            cameraBodyId = "body-1",
            lightMeterId = null,
            filmBackId = "back-1",
            type = FilmMediumType.ROLL,
            targetFrameCount = 10,
            status = FilmMediumStatus.AVAILABLE,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.SYNCED,
            remoteId = null,
        )

        assertEquals(original, original.toDto().toDomain(syncStatus = SyncStatus.SYNCED))
    }

    @Test
    fun `film medium round-trips through its dto with a light meter assigned`() {
        val original = FilmMedium(
            id = "medium-1",
            name = "HP5 Plus — Film 1",
            filmStock = "Ilford HP5 Plus",
            boxSpeedIso = 400,
            format = FilmFormat.MEDIUM_FORMAT_120,
            colorType = FilmColorType.BLACK_AND_WHITE,
            cameraBodyId = "body-1",
            lightMeterId = "meter-1",
            filmBackId = "back-1",
            type = FilmMediumType.ROLL,
            targetFrameCount = 10,
            status = FilmMediumStatus.AVAILABLE,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.SYNCED,
            remoteId = null,
        )

        assertEquals(original, original.toDto().toDomain(syncStatus = SyncStatus.SYNCED))
    }

    @Test
    fun `sheet film medium round-trips through its dto with no film back`() {
        val original = FilmMedium(
            id = "medium-1",
            name = "Tri-X Sheet Pack",
            filmStock = "Kodak Tri-X 320",
            boxSpeedIso = 320,
            format = FilmFormat.LARGE_FORMAT,
            colorType = FilmColorType.BLACK_AND_WHITE,
            cameraBodyId = "body-1",
            lightMeterId = null,
            filmBackId = null,
            type = FilmMediumType.SHEET,
            targetFrameCount = 10,
            status = FilmMediumStatus.AVAILABLE,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.SYNCED,
            remoteId = null,
        )

        assertEquals(original, original.toDto().toDomain(syncStatus = SyncStatus.SYNCED))
    }

    @Test
    fun `exposure round-trips through its dto, including an exposure value`() {
        val original = Exposure(
            id = "exp-1",
            filmMediumId = "medium-1",
            frameNumber = 3,
            lensId = "lens-1",
            focalLengthMm = null,
            shutterSpeed = ShutterSpeed.fraction(250),
            aperture = 5.6,
            isoUsed = 400,
            exposureValue = 3,
            notes = "backlit",
            capturedAt = 100L,
            referencePhotoStatus = PhotoStatus.NONE,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.PENDING_SYNC,
            remoteId = null,
        )

        val roundTripped = original.toDto().toDomain(syncStatus = SyncStatus.SYNCED)

        assertEquals(original.copy(syncStatus = SyncStatus.SYNCED), roundTripped)
    }

    @Test
    fun `exposure isFavorite round-trips through its dto`() {
        val original = Exposure(
            id = "exp-1",
            filmMediumId = "medium-1",
            frameNumber = 3,
            lensId = "lens-1",
            focalLengthMm = null,
            shutterSpeed = ShutterSpeed.fraction(250),
            aperture = 5.6,
            isoUsed = 400,
            exposureValue = null,
            notes = null,
            capturedAt = 100L,
            referencePhotoStatus = PhotoStatus.NONE,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.PENDING_SYNC,
            remoteId = null,
            isFavorite = true,
        )

        val roundTripped = original.toDto().toDomain(syncStatus = SyncStatus.SYNCED)

        assertEquals(original.copy(syncStatus = SyncStatus.SYNCED), roundTripped)
    }

    @Test
    fun `exposure with no exposure value round-trips through its dto`() {
        val original = Exposure(
            id = "exp-1",
            filmMediumId = "medium-1",
            frameNumber = 3,
            lensId = "lens-1",
            focalLengthMm = null,
            shutterSpeed = ShutterSpeed.fraction(250),
            aperture = 5.6,
            isoUsed = 400,
            exposureValue = null,
            notes = "backlit",
            capturedAt = 100L,
            referencePhotoStatus = PhotoStatus.NONE,
            createdAt = 1L,
            updatedAt = 2L,
            syncStatus = SyncStatus.PENDING_SYNC,
            remoteId = null,
        )

        val roundTripped = original.toDto().toDomain(syncStatus = SyncStatus.SYNCED)

        assertEquals(original.copy(syncStatus = SyncStatus.SYNCED), roundTripped)
    }

    @Test
    fun `photo status dto reflects the exposure's current status`() {
        val exposure = Exposure(
            id = "exp-1",
            filmMediumId = "medium-1",
            frameNumber = 1,
            lensId = "lens-1",
            focalLengthMm = null,
            shutterSpeed = ShutterSpeed.fraction(125),
            aperture = 8.0,
            isoUsed = 400,
            exposureValue = null,
            notes = null,
            capturedAt = 0L,
            referencePhotoStatus = PhotoStatus.CAPTURED,
            createdAt = 0L,
            updatedAt = 0L,
            syncStatus = SyncStatus.SYNCED,
            remoteId = null,
        )

        val dto = exposure.toPhotoStatusDto()

        assertEquals("exp-1", dto.exposureId)
        assertEquals("CAPTURED", dto.referencePhotoStatus)
    }
}
