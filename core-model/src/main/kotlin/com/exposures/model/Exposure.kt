package com.exposures.model

/** Watch-authoritative: exposures are recorded on the watch against the currently active film medium. */
data class Exposure(
    val id: String,
    val filmMediumId: String,
    val frameNumber: Int,
    val lensId: String,
    /** The focal length actually used, mm. Required when the lens is a ZOOM; auto-populated from the lens for a PRIME. */
    val focalLengthMm: Int?,
    val shutterSpeed: ShutterSpeed,
    val aperture: Double,
    val isoUsed: Int,
    /** 1..20 EV; required when the film medium's light meter is [LightMeterType.SPOT], else null. */
    val exposureValue: Int?,
    val notes: String?,
    val capturedAt: Long,
    val referencePhotoStatus: PhotoStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus,
    val remoteId: String?,
    val isFavorite: Boolean = false,
)
