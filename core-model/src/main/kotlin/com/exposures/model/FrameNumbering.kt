package com.exposures.model

/** The frame number the next exposure logged against this film medium should use. */
fun List<Exposure>.nextFrameNumber(): Int = (maxOfOrNull { it.frameNumber } ?: 0) + 1

/** Whether a film medium has reached its target frame count given how many exposures have been logged against it. */
fun FilmMedium.isComplete(exposureCount: Int): Boolean = exposureCount >= targetFrameCount
