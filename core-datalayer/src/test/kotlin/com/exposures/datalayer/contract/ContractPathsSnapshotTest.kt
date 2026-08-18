package com.exposures.datalayer.contract

import com.exposures.datalayer.DataLayerPaths
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Modifier

/**
 * Strict parity test for Data Layer path/command/capability constants.
 *
 * [expectedPaths] is a hand-maintained snapshot: any intentional change to a path, command, or
 * capability string must update this map in the same change, so the diff makes the contract
 * change explicit for both consumer repos rather than silently drifting.
 */
class ContractPathsSnapshotTest {

    private val expectedPaths = mapOf(
        "CAMERA_BODIES" to "/equipment/camera-bodies",
        "LENSES" to "/equipment/lenses",
        "LIGHT_METERS" to "/equipment/light-meters",
        "FILM_BACKS" to "/equipment/film-backs",
        "ROLLS" to "/rolls",
        "EXPOSURES" to "/exposures",
        "PHOTO_STATUSES" to "/photo-status",
        "CAPTURE_PHOTO_COMMAND" to "/command/capture-photo",
        "CAPTURE_RESULT_COMMAND" to "/command/capture-result",
        "COMPLETE_ROLL_COMMAND" to "/command/complete-roll",
        "REQUEST_ROLLS_SYNC_COMMAND" to "/command/request-rolls-sync",
        "CONNECTIVITY_PING_COMMAND" to "/command/connectivity-ping",
        "CONNECTIVITY_PING_ACK_COMMAND" to "/command/connectivity-ping-ack",
        "CAPABILITY_EXPOSURES_APP" to "exposures_app",
        "KEY_PAYLOAD" to "payload",
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

    @Test
    fun `DataLayerPaths values match snapshot exactly`() {
        val actual = mapOf(
            "CAMERA_BODIES" to DataLayerPaths.CAMERA_BODIES,
            "LENSES" to DataLayerPaths.LENSES,
            "LIGHT_METERS" to DataLayerPaths.LIGHT_METERS,
            "FILM_BACKS" to DataLayerPaths.FILM_BACKS,
            "ROLLS" to DataLayerPaths.ROLLS,
            "EXPOSURES" to DataLayerPaths.EXPOSURES,
            "PHOTO_STATUSES" to DataLayerPaths.PHOTO_STATUSES,
            "CAPTURE_PHOTO_COMMAND" to DataLayerPaths.CAPTURE_PHOTO_COMMAND,
            "CAPTURE_RESULT_COMMAND" to DataLayerPaths.CAPTURE_RESULT_COMMAND,
            "COMPLETE_ROLL_COMMAND" to DataLayerPaths.COMPLETE_ROLL_COMMAND,
            "REQUEST_ROLLS_SYNC_COMMAND" to DataLayerPaths.REQUEST_ROLLS_SYNC_COMMAND,
            "CONNECTIVITY_PING_COMMAND" to DataLayerPaths.CONNECTIVITY_PING_COMMAND,
            "CONNECTIVITY_PING_ACK_COMMAND" to DataLayerPaths.CONNECTIVITY_PING_ACK_COMMAND,
            "CAPABILITY_EXPOSURES_APP" to DataLayerPaths.CAPABILITY_EXPOSURES_APP,
            "KEY_PAYLOAD" to DataLayerPaths.KEY_PAYLOAD,
        )
        assertEquals(expectedPaths, actual)
    }

    @Test
    fun `no undeclared constants exist on DataLayerPaths`() {
        val declaredNames = DataLayerPaths::class.java.declaredFields
            .filter { Modifier.isStatic(it.modifiers) && it.type == String::class.java }
            .map { it.name }
            .toSet()
        assertEquals(
            "DataLayerPaths gained or lost a constant without updating this snapshot.",
            expectedPaths.keys,
            declaredNames,
        )
    }
}
