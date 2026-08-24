package com.exposures.datalayer.contract

import com.exposures.datalayer.DataLayerJson
import com.exposures.datalayer.dto.CreateExposureCommand
import com.exposures.datalayer.dto.ExposureDto
import com.exposures.datalayer.dto.FilmMediumDto
import com.exposures.datalayer.dto.LensDto
import com.exposures.datalayer.dto.ShutterSpeedDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Real (non-scaffold) DTO/JSON compatibility checks, now that the shared Data Layer sources live
 * in this repo. Backs the compatibility policy in `docs/CONTRACT_COMPATIBILITY.md`: defaulted
 * fields must decode safely from payloads written before they existed, and unknown fields from a
 * newer writer must not break an older reader.
 */
class ContractJsonCompatibilityTest {

    @Test
    fun `FilmMediumDto colorType defaults to COLOR when absent from an older payload`() {
        val legacyJson = """
            [{
              "id": "medium-1",
              "name": "Tri-X",
              "filmStock": "Tri-X 400",
              "boxSpeedIso": 400,
              "format": "ROLL_6X7",
              "cameraBodyId": "body-1",
              "filmBackId": "back-1",
              "type": "ROLL",
              "targetFrameCount": 10,
              "status": "AVAILABLE",
              "createdAt": 0,
              "updatedAt": 0
            }]
        """.trimIndent()

        val decoded = DataLayerJson.decodeFilmMedia(legacyJson)

        assertEquals("COLOR", decoded.single().colorType)
    }

    @Test
    fun `LensDto referencePhotoZoomRatio defaults to 1_0 when absent from an older payload`() {
        val legacyJson = """
            [{
              "id": "lens-1",
              "name": "50mm",
              "minAperture": 1.4,
              "maxAperture": 16.0,
              "stopIncrement": "FULL",
              "createdAt": 0,
              "updatedAt": 0
            }]
        """.trimIndent()

        val decoded = DataLayerJson.decodeLenses(legacyJson)

        assertEquals(1.0, decoded.single().referencePhotoZoomRatio, 0.0)
    }

    @Test
    fun `canonical zoom LensDto round-trips through encode and decode`() {
        val original = LensDto(
            id = "lens-1",
            name = "24-70mm",
            cameraBodyId = "body-1",
            minAperture = 2.8,
            maxAperture = 22.0,
            stopIncrement = "THIRD_STOP",
            referencePhotoZoomRatio = 1.5,
            lensType = "ZOOM",
            focalLengthMinMm = 24,
            focalLengthMaxMm = 70,
            createdAt = 0,
            updatedAt = 0,
            remoteId = null,
        )

        val decoded = DataLayerJson.decodeLenses(DataLayerJson.encodeLenses(listOf(original)))

        assertEquals(listOf(original), decoded)
    }

    @Test
    fun `ExposureDto optional fields default to null when absent from an older payload`() {
        val legacyJson = """
            [{
              "id": "exp-1",
              "filmMediumId": "medium-1",
              "frameNumber": 1,
              "lensId": "lens-1",
              "shutterSpeed": {"kind": "FRACTION", "numerator": 1, "denominator": 125},
              "aperture": 8.0,
              "isoUsed": 400,
              "capturedAt": 0,
              "referencePhotoStatus": "NONE",
              "createdAt": 0,
              "updatedAt": 0
            }]
        """.trimIndent()

        val decoded = DataLayerJson.decodeExposures(legacyJson).single()

        assertNull(decoded.zone)
        assertNull(decoded.notes)
        assertNull(decoded.focalLengthMm)
        assertNull(decoded.remoteId)
        assertEquals(false, decoded.isFavorite)
    }

    @Test
    fun `CreateExposureCommand optional fields default to null when absent from an older payload`() {
        val legacyJson = """
            {
              "exposureId": "exp-1",
              "shutterSpeed": {"kind": "FRACTION", "numerator": 1, "denominator": 125}
            }
        """.trimIndent()

        val decoded = DataLayerJson.decodeCreateExposureCommand(legacyJson)

        assertNull(decoded.lensId)
        assertNull(decoded.aperture)
        assertNull(decoded.isoUsed)
        assertNull(decoded.notes)
    }

    @Test
    fun `canonical CreateExposureCommand round-trips through encode and decode`() {
        val original = CreateExposureCommand(
            exposureId = "exp-1",
            shutterSpeed = ShutterSpeedDto(kind = "FRACTION", numerator = 1, denominator = 125),
            lensId = "lens-1",
            aperture = 8.0,
            isoUsed = 400,
            notes = "backlit",
        )

        val decoded = DataLayerJson.decodeCreateExposureCommand(DataLayerJson.encodeCreateExposureCommand(original))

        assertEquals(original, decoded)
    }

    @Test
    fun `unknown fields from a newer writer are ignored`() {
        val fromTheFutureJson = """
            [{
              "id": "lens-1",
              "name": "50mm",
              "minAperture": 1.4,
              "maxAperture": 16.0,
              "stopIncrement": "FULL",
              "createdAt": 0,
              "updatedAt": 0,
              "aFieldThatDoesNotExistYet": "some value"
            }]
        """.trimIndent()

        val decoded = DataLayerJson.decodeLenses(fromTheFutureJson)

        assertEquals("lens-1", decoded.single().id)
    }

    @Test
    fun `canonical ExposureDto round-trips through encode and decode`() {
        val original = ExposureDto(
            id = "exp-1",
            filmMediumId = "medium-1",
            frameNumber = 3,
            lensId = "lens-1",
            shutterSpeed = ShutterSpeedDto(kind = "FRACTION", numerator = 1, denominator = 125),
            aperture = 8.0,
            isoUsed = 400,
            zone = 6,
            notes = "backlit",
            focalLengthMm = 50,
            capturedAt = 1_000L,
            referencePhotoStatus = "UPLOADED",
            createdAt = 1_000L,
            updatedAt = 1_000L,
            remoteId = "remote-1",
        )

        val decoded = DataLayerJson.decodeExposures(DataLayerJson.encodeExposures(listOf(original)))

        assertEquals(listOf(original), decoded)
    }

    @Test
    fun `canonical FilmMediumDto round-trips through encode and decode`() {
        val original = FilmMediumDto(
            id = "medium-1",
            name = "Film 1",
            filmStock = "Tri-X 400",
            boxSpeedIso = 400,
            format = "ROLL_6X7",
            colorType = "BLACK_AND_WHITE",
            cameraBodyId = "body-1",
            lightMeterId = null,
            filmBackId = "back-1",
            type = "ROLL",
            targetFrameCount = 10,
            status = "AVAILABLE",
            createdAt = 0,
            updatedAt = 0,
            remoteId = null,
        )

        val decoded = DataLayerJson.decodeFilmMedia(DataLayerJson.encodeFilmMedia(listOf(original)))

        assertEquals(listOf(original), decoded)
    }
}
