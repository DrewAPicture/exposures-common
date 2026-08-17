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
