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

1. ~~Populate this repo with current shared files from both apps.~~ Done — `core-model` and `core-datalayer` are populated (Phase 2).
2. ~~Keep tests green in this repo first.~~ Done — `./gradlew test` passes.
3. ~~Wire consumers (`phone`, `watch`) via composite build or published artifacts.~~ Done (Phase 3) — via composite build (`includeBuild` + dependency substitution). Not yet a published artifact; that's Phase 5.
4. ~~Remove duplicated modules in app repos only after both integrations are validated.~~ Done (Phase 4) — both apps' local `core-model`/`core-datalayer` are deleted; each is now sourced exclusively from this repo.

## Compatibility policy

See `docs/CONTRACT_COMPATIBILITY.md`.

## Changelog

See `CHANGELOG.md` for the baseline snapshot of what's currently populated and (once consumers exist) a record of additive/breaking changes to the shared surface.

## Contract tests

- `core-datalayer/src/test/kotlin/com/exposures/datalayer/contract/ContractPathsSnapshotTest.kt` — strict parity between a hand-maintained snapshot and `DataLayerPaths`' actual constants (values, uniqueness, and no undeclared/missing constants).
- `core-datalayer/src/test/kotlin/com/exposures/datalayer/contract/ContractJsonCompatibilityTest.kt` — DTO/JSON compatibility: defaulted fields decode safely from payloads written before they existed, unknown fields from a newer writer don't break an older reader, and canonical DTOs round-trip through encode/decode.

Both were scaffolded as `@Ignore`d placeholders before shared Data Layer sources existed in this repo; now that `core-datalayer` is populated, they run for real on every `./gradlew test`.

## Rebaseline audit tooling

- Checklist: `docs/rebaseline/CHECKLIST.md`
- Script: `scripts/rebaseline-audit.sh`

Run from repo root:

- `./scripts/rebaseline-audit.sh`
- `./scripts/rebaseline-audit.sh --output docs/rebaseline/report-YYYY-MM-DD.md`
