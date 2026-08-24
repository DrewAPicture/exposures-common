package com.exposures.model

/**
 * How a [FilmMedium] is loaded into a camera. `ROLL` is loaded into a physical film back and advanced
 * frame by frame; `SHEET` is large-format film exposed one discrete sheet at a time with no back and
 * no advance mechanism — [FilmMedium.targetFrameCount] tracks how many sheets are in the pack instead.
 */
enum class FilmMediumType {
    ROLL,
    SHEET,
}
