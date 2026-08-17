# exposures-common

Shared library repository for code used by both `exposures-phone` and `exposures-watch`.

## Purpose

This repo centralizes shared model and Data Layer contract/runtime code so phone/watch no longer need manual mirroring of DTOs, paths, and serialization behavior.

## Modules

- `core-model` (Kotlin/JVM): shared domain/value types.
- `core-datalayer` (Android library): Wear Data Layer DTOs, paths/constants, JSON codec, and client/gateway code.

## Non-goals (for now)

- Unifying `core-database` end-to-end. Phone/watch intentionally diverge in some Room entity FK choices, DAO semantics, and repositories.

## Migration strategy

1. Populate this repo with current shared files from both apps.
2. Keep tests green in this repo first.
3. Wire consumers (`phone`, `watch`) via composite build or published artifacts.
4. Remove duplicated modules in app repos only after both integrations are validated.

## Compatibility policy

See `docs/CONTRACT_COMPATIBILITY.md`.

## Contract test scaffolding

Initial scaffolding exists under:

- `core-datalayer/src/test/kotlin/com/exposures/datalayer/contract/ContractPathsSnapshotTest.kt`
- `core-datalayer/src/test/kotlin/com/exposures/datalayer/contract/ContractJsonCompatibilityScaffoldTest.kt`

These start as snapshot/pending checks and are intended to become strict parity/fixture tests after shared Data Layer sources are fully migrated into this repo.

## Rebaseline audit tooling

- Checklist: `docs/rebaseline/CHECKLIST.md`
- Script: `scripts/rebaseline-audit.sh`

Run from repo root:

- `./scripts/rebaseline-audit.sh`
- `./scripts/rebaseline-audit.sh --output docs/rebaseline/report-YYYY-MM-DD.md`
