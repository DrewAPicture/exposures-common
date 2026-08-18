# Rebaseline Audit Report

- Generated at: 2026-08-18 03:19:23 UTC
- Phone repo: `/Users/drew/Desktop/sites/Personal/exposures/common/../phone`
- Watch repo: `/Users/drew/Desktop/sites/Personal/exposures/common/../watch`

Use this report with `docs/rebaseline/CHECKLIST.md` to make include/exclude and versioning decisions.

## Local core-model/core-datalayer Reintroduction Guard

| Repo | Module | Status |
|---|---|---|
| `phone` | `core-model` | absent (expected) |
| `phone` | `core-datalayer` | absent (expected) |
| `watch` | `core-model` | absent (expected) |
| `watch` | `core-datalayer` | absent (expected) |

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

