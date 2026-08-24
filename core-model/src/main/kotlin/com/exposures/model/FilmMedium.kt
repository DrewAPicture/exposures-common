package com.exposures.model

/** Phone-authoritative: film media are created/configured on the phone. The watch can only select among them. */
data class FilmMedium(
    val id: String,
    val name: String,
    val filmStock: String,
    val boxSpeedIso: Int,
    val format: FilmFormat,
    val colorType: FilmColorType,
    val cameraBodyId: String,
    val lightMeterId: String?,
    /** Required for [FilmMediumType.ROLL] (the physical roll back to load it into); unused for [FilmMediumType.SHEET]. */
    val filmBackId: String?,
    val type: FilmMediumType,
    val targetFrameCount: Int,
    val status: FilmMediumStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus,
    val remoteId: String?,
)
