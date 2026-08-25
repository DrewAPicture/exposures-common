package com.exposures.model

/**
 * The exposure value (EV) used to place a spot-metered reading, 1..20. Modeled as a plain Int on
 * [Exposure] rather than its own enum/value class, since it's stored, synced, and compared just
 * like the other scalar exposure fields.
 */
object ExposureValue {
    const val MIN = 1
    const val MAX = 20

    /** Where the picker starts before any exposure has ever recorded an EV. */
    const val DEFAULT = 10

    /** Validated display label for [exposureValue]. */
    fun label(exposureValue: Int): String {
        require(exposureValue in MIN..MAX) { "Exposure value must be $MIN..$MAX, was $exposureValue" }
        return exposureValue.toString()
    }
}
