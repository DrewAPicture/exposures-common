# Rebaseline Audit Report

- Generated at: 2026-08-18 05:14:04 UTC
- Phone repo: `/Users/drew/Desktop/sites/Personal/exposures/common/../phone`
- Watch repo: `/Users/drew/Desktop/sites/Personal/exposures/common/../watch`

Use this report with `docs/rebaseline/CHECKLIST.md` to make include/exclude and versioning decisions.

## Local shared-source Reintroduction Guard

| Repo | Path | Status |
|---|---|---|
| `phone` | `core-model` | absent (expected) |
| `phone` | `core-datalayer` | absent (expected) |
| `phone` | `Converters.kt` | absent (expected) |
| `phone` | `CameraBodyEntity.kt` | absent (expected) |
| `phone` | `LensEntity.kt` | absent (expected) |
| `phone` | `LightMeterEntity.kt` | absent (expected) |
| `watch` | `core-model` | absent (expected) |
| `watch` | `core-datalayer` | absent (expected) |
| `watch` | `Converters.kt` | absent (expected) |
| `watch` | `CameraBodyEntity.kt` | absent (expected) |
| `watch` | `LensEntity.kt` | absent (expected) |
| `watch` | `LightMeterEntity.kt` | absent (expected) |

## Database Remaining-Local Candidates (Informational)

| File | Status |
|---|---|
| `core-database/src/main/kotlin/com/exposures/database/mapper/Mappers.kt` | drifted |
| `core-database/src/main/kotlin/com/exposures/database/entity/ExposureEntity.kt` | drifted |
| `core-database/src/main/kotlin/com/exposures/database/entity/FilmBackEntity.kt` | drifted |
| `core-database/src/main/kotlin/com/exposures/database/entity/FilmRollEntity.kt` | drifted |

## Manual follow-up

- Classify each drift as intentional divergence, should-converge, or out-of-scope.
- Update `docs/CONTRACT_COMPATIBILITY.md` if contract/versioning implications changed.
- Do not remove/replace consumer shared code until rebaseline sign-off is complete.

