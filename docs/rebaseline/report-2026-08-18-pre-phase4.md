# Rebaseline Audit Report

- Generated at: 2026-08-18 03:15:55 UTC
- Phone repo: `/Users/drew/Desktop/sites/Personal/exposures/common/../phone`
- Watch repo: `/Users/drew/Desktop/sites/Personal/exposures/common/../watch`

Use this report with `docs/rebaseline/CHECKLIST.md` to make include/exclude and versioning decisions.

## Core Datalayer Contract Surface

| File | Status |
|---|---|
| `core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerPaths.kt` | identical |
| `core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerJson.kt` | identical |
| `core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerClient.kt` | identical |
| `core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerGateway.kt` | identical |
| `core-datalayer/src/main/kotlin/com/exposures/datalayer/dto/Dtos.kt` | identical |
| `core-datalayer/src/main/kotlin/com/exposures/datalayer/mapper/DtoMappers.kt` | identical |

## Core Model High-Risk Surface

| File | Status |
|---|---|
| `core-model/src/main/kotlin/com/exposures/model/CameraBody.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/Lens.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/FilmRoll.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/Exposure.kt` | drifted |
| `core-model/src/main/kotlin/com/exposures/model/ShutterSpeed.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/Zone.kt` | drifted |
| `core-model/src/main/kotlin/com/exposures/model/FilmBack.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/FilmBackType.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/FilmColorType.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/FilmFormat.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/FrameNumbering.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/LightMeter.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/LightMeterType.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/PhotoStatus.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/RollStatus.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/StandardApertures.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/StandardIso.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/StopIncrement.kt` | identical |
| `core-model/src/main/kotlin/com/exposures/model/SyncStatus.kt` | identical |

## Database Common Candidates (Informational)

| File | Status |
|---|---|
| `core-database/src/main/kotlin/com/exposures/database/Converters.kt` | identical |
| `core-database/src/main/kotlin/com/exposures/database/mapper/Mappers.kt` | drifted |
| `core-database/src/main/kotlin/com/exposures/database/entity/CameraBodyEntity.kt` | identical |
| `core-database/src/main/kotlin/com/exposures/database/entity/LensEntity.kt` | identical |
| `core-database/src/main/kotlin/com/exposures/database/entity/LightMeterEntity.kt` | identical |

## Manual follow-up

- Classify each drift as intentional divergence, should-converge, or out-of-scope.
- Update `docs/CONTRACT_COMPATIBILITY.md` if contract/versioning implications changed.
- Do not remove/replace consumer shared code until rebaseline sign-off is complete.

