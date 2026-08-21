package com.exposures.model

/** Phone-authoritative: lenses are configured on the phone, the watch only ever reads them. */
data class Lens(
    val id: String,
    val name: String,
    /** The body this lens is used with, if configured — nullable so existing lenses don't need backfilling. */
    val cameraBodyId: String?,
    val minAperture: Double,
    val maxAperture: Double,
    val stopIncrement: StopIncrement,
    /** Phone camera zoom applied to this lens's reference photo (e.g. 50mm -> 1.0, 180mm -> 3.0). */
    val referencePhotoZoomRatio: Double,
    val lensType: LensType = LensType.PRIME,
    /** Required for [LensType.PRIME]; the lens's single fixed focal length. */
    val focalLengthMm: Int? = null,
    /** Required for [LensType.ZOOM], together with [focalLengthMaxMm]. */
    val focalLengthMinMm: Int? = null,
    val focalLengthMaxMm: Int? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus,
    val remoteId: String?,
) {
    /** Apertures selectable for this lens, per its [stopIncrement]. */
    fun availableApertures(): List<Double> = StandardApertures.forLens(minAperture, maxAperture, stopIncrement)

    /**
     * Focal lengths selectable for this lens: a single value for [LensType.PRIME], every whole mm
     * in range for [LensType.ZOOM]. Empty if the lens's focal length fields haven't been filled in
     * (e.g. a lens saved before this field existed), so the watch can fall back sensibly rather
     * than crash on a missing value.
     */
    fun availableFocalLengths(): List<Int> = when (lensType) {
        LensType.PRIME -> listOfNotNull(focalLengthMm)
        LensType.ZOOM -> {
            val min = focalLengthMinMm
            val max = focalLengthMaxMm
            if (min != null && max != null && min <= max) (min..max).toList() else emptyList()
        }
    }
}
