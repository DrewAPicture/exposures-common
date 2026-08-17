package com.exposures.datalayer.contract

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

/**
 * Snapshot scaffold for Data Layer path/command constants.
 *
 * Why this exists:
 * - Prevent silent drift in path/command naming over time.
 * - Provide a stable baseline before strict reflection-based tests are enabled.
 *
 * How to evolve:
 * 1) Keep this snapshot updated when the contract intentionally changes.
 * 2) Enable the ignored parity test once DataLayerPaths is moved into this repo.
 */
class ContractPathsSnapshotTest {

    private val expectedPaths = mapOf(
        "CAPABILITY_EXPOSURES_APP" to "exposures_app",
        "KEY_PAYLOAD" to "payload",
        "CAMERA_BODIES_DATA_PATH" to "/equipment/camera-bodies",
        "LENSES_DATA_PATH" to "/equipment/lenses",
        "LIGHT_METERS_DATA_PATH" to "/equipment/light-meters",
        "FILM_ROLLS_DATA_PATH" to "/rolls",
        "EXPOSURES_DATA_PATH" to "/exposures",
        "PHOTO_STATUS_DATA_PATH" to "/photo-status",
        "CAPTURE_PHOTO_COMMAND" to "/command/capture-photo",
        "CAPTURE_RESULT_COMMAND" to "/command/capture-result",
        "COMPLETE_ROLL_COMMAND" to "/command/complete-roll",
        "REQUEST_ROLLS_SYNC_COMMAND" to "/command/request-rolls-sync",
        "CONNECTIVITY_PING_COMMAND" to "/command/connectivity-ping",
        "CONNECTIVITY_PING_ACK_COMMAND" to "/command/connectivity-ping-ack",
    )

    @Test
    fun `snapshot has unique values`() {
        assertEquals(
            "Duplicate Data Layer contract values found in snapshot.",
            expectedPaths.size,
            expectedPaths.values.toSet().size,
        )
    }

    @Test
    fun `snapshot command paths use expected prefix`() {
        val commandPaths = expectedPaths
            .filterKeys { it.endsWith("_COMMAND") }
            .values
        assertTrue(commandPaths.isNotEmpty())
        assertTrue(commandPaths.all { it.startsWith("/command/") })
    }

    @Ignore("Enable after DataLayerPaths is moved into exposures-common.")
    @Test
    fun `DataLayerPaths values match snapshot`() {
        // TODO: reflect over com.exposures.datalayer.DataLayerPaths constants and compare
        // against expectedPaths exactly. Left ignored until shared source is migrated here.
    }
}
