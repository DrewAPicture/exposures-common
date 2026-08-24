package com.exposures.datalayer

import com.exposures.datalayer.dto.CameraBodyDto
import com.exposures.datalayer.dto.CaptureResultCommand
import com.exposures.datalayer.dto.CompleteFilmMediumCommand
import com.exposures.datalayer.dto.CreateExposureAckCommand
import com.exposures.datalayer.dto.CreateExposureCommand
import com.exposures.datalayer.dto.ExposureDto
import com.exposures.datalayer.dto.FilmBackDto
import com.exposures.datalayer.dto.FilmMediumDto
import com.exposures.datalayer.dto.LensDto
import com.exposures.datalayer.dto.LightMeterDto
import com.exposures.datalayer.dto.PhotoStatusDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** The single Json instance both apps must use, so encoding choices (e.g. default values) agree. */
object DataLayerJson {
    val instance = Json { ignoreUnknownKeys = true }

    fun encodeCameraBodies(value: List<CameraBodyDto>): String = instance.encodeToString(value)
    fun decodeCameraBodies(json: String): List<CameraBodyDto> = instance.decodeFromString(json)

    fun encodeLenses(value: List<LensDto>): String = instance.encodeToString(value)
    fun decodeLenses(json: String): List<LensDto> = instance.decodeFromString(json)

    fun encodeLightMeters(value: List<LightMeterDto>): String = instance.encodeToString(value)
    fun decodeLightMeters(json: String): List<LightMeterDto> = instance.decodeFromString(json)

    fun encodeFilmBacks(value: List<FilmBackDto>): String = instance.encodeToString(value)
    fun decodeFilmBacks(json: String): List<FilmBackDto> = instance.decodeFromString(json)

    fun encodeFilmMedia(value: List<FilmMediumDto>): String = instance.encodeToString(value)
    fun decodeFilmMedia(json: String): List<FilmMediumDto> = instance.decodeFromString(json)

    fun encodeExposures(value: List<ExposureDto>): String = instance.encodeToString(value)
    fun decodeExposures(json: String): List<ExposureDto> = instance.decodeFromString(json)

    fun encodePhotoStatuses(value: List<PhotoStatusDto>): String = instance.encodeToString(value)
    fun decodePhotoStatuses(json: String): List<PhotoStatusDto> = instance.decodeFromString(json)

    fun encodeCaptureResultCommand(value: CaptureResultCommand): String = instance.encodeToString(value)
    fun decodeCaptureResultCommand(json: String): CaptureResultCommand = instance.decodeFromString(json)

    fun encodeCompleteFilmMediumCommand(value: CompleteFilmMediumCommand): String = instance.encodeToString(value)
    fun decodeCompleteFilmMediumCommand(json: String): CompleteFilmMediumCommand = instance.decodeFromString(json)

    fun encodeCreateExposureCommand(value: CreateExposureCommand): String = instance.encodeToString(value)
    fun decodeCreateExposureCommand(json: String): CreateExposureCommand = instance.decodeFromString(json)

    fun encodeCreateExposureAckCommand(value: CreateExposureAckCommand): String = instance.encodeToString(value)
    fun decodeCreateExposureAckCommand(json: String): CreateExposureAckCommand = instance.decodeFromString(json)
}
