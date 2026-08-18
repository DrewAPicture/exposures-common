package com.exposures.datalayer.contract

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * CI drift check for the checked-in Data Layer contract spec. Regenerates the spec from source
 * and requires it to match `docs/contracts/data-layer.json` byte-for-byte.
 *
 * After an intentional path/DTO change, regenerate with:
 * `./gradlew :core-datalayer:testDebugUnitTest -PupdateContractSpec`
 */
class ContractSpecDriftTest {

    @Test
    fun `checked-in spec matches the spec generated from DataLayerPaths and DTO descriptors`() {
        val generated = ContractSpecGenerator.render()
        val specFile = locateContractSpec()
        if (System.getProperty("updateContractSpec") == "true") {
            specFile.parentFile?.mkdirs()
            specFile.writeText(generated)
            return
        }
        check(specFile.isFile) {
            "Missing ${specFile.absolutePath}. Generate it with " +
                "./gradlew :core-datalayer:testDebugUnitTest -PupdateContractSpec"
        }
        assertEquals(
            "Data Layer contract spec drifted from source. If the change is intentional, " +
                "regenerate with ./gradlew :core-datalayer:testDebugUnitTest -PupdateContractSpec " +
                "and include the spec diff in the same commit.",
            specFile.readText(),
            generated,
        )
    }

    companion object {
        private const val RELATIVE_SPEC_PATH = "docs/contracts/data-layer.json"

        fun locateContractSpec(): File {
            System.getProperty("datalayer.contract.spec")?.let { return File(it) }
            var dir = File(System.getProperty("user.dir") ?: ".").absoluteFile
            while (true) {
                val atRepoRoot = File(dir, "settings.gradle.kts").isFile &&
                    File(dir, "core-datalayer").isDirectory
                if (atRepoRoot) return File(dir, RELATIVE_SPEC_PATH)
                dir = dir.parentFile ?: error(
                    "Could not locate $RELATIVE_SPEC_PATH from ${System.getProperty("user.dir")}",
                )
            }
        }
    }
}
