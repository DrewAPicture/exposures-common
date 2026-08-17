package com.exposures.datalayer.contract

import org.junit.Ignore
import org.junit.Test

/**
 * Scaffold for JSON compatibility tests.
 *
 * Intended future checks after DTO/json sources are migrated into this repo:
 * - old payloads decode with new DTOs (backward compatibility)
 * - unknown fields are tolerated
 * - newly-added optional/defaulted fields do not break old payloads
 * - enum forward-value handling is explicit and tested
 */
class ContractJsonCompatibilityScaffoldTest {

    @Ignore("Enable after DTO/DataLayerJson sources are moved into exposures-common.")
    @Test
    fun `decodes historical payload fixtures`() {
        // TODO: load fixture JSON from src/test/resources/contract-fixtures and decode with
        // DataLayerJson helpers, asserting expected defaults.
    }

    @Ignore("Enable after DTO/DataLayerJson sources are moved into exposures-common.")
    @Test
    fun `unknown fields are ignored for backward compatibility`() {
        // TODO: decode a payload fixture containing future fields; assert success.
    }

    @Ignore("Enable after DTO/DataLayerJson sources are moved into exposures-common.")
    @Test
    fun `round-trip snapshots are stable for canonical payloads`() {
        // TODO: encode/decode canonical DTO instances and compare with stored snapshots.
    }
}
