# Contract Compatibility Rules

These rules apply to shared `core-model` and `core-datalayer` contracts consumed by both apps.

## Change policy

- Default to additive changes.
- Prefer optional fields or fields with safe defaults for new DTO/model properties.
- Treat field removal, rename, type change, or semantic reinterpretation as a breaking change.
- Coordinate breaking changes with a synchronized phone+watch rollout.

## Enum safety

- Adding enum values is allowed only when all consumers handle unknown values safely.
- Avoid unchecked `valueOf(...)` paths that can crash on forward values.

## App-specific usage

- It is acceptable for one app to not use every shared field.
- Shared types may include superset fields as long as compatibility guarantees are preserved.

## Spec strategy

- Use OpenAPI for actual HTTP boundaries (primarily phone backend APIs). The phone-side spec lives in `exposures-phone` at `docs/openapi/sync-api.json`, generated from `SyncApi` + `core-sync` DTOs. Regenerate with `./gradlew :core-sync:test -PupdateOpenApiSpec`.
- Use a dedicated Data Layer contract spec for watch<->phone payloads (`docs/contracts/data-layer.json` in this repo):
  - DataItem paths
  - Message command paths
  - DTO schema/defaults/requiredness
  - Writer/reader authority per path
- Both specs are generated from source (kotlinx.serialization descriptors and, for OpenAPI, Retrofit annotations). Do not edit the JSON by hand.

## CI guardrails

- Require contract encode/decode tests to pass in this repo.
- Require `ContractSpecDriftTest` to pass: it regenerates `docs/contracts/data-layer.json` from source and fails if the checked-in spec drifted. After an intentional contract change, regenerate with `./gradlew :core-datalayer:testDebugUnitTest -PupdateContractSpec` and commit the spec diff in the same change.
- Add consumer CI checks to keep phone/watch pinned to the same compatible shared version.
- Maintain contract test coverage in `core-datalayer/src/test/kotlin/com/exposures/datalayer/contract/`.

## Audit-first execution policy

- Before each abstraction step, re-audit both app repos (`exposures-phone`, `exposures-watch`) for meaningful shared-surface changes.
- Keep `exposures-common` current with meaningful Phase 8/9-driven changes before replacing consumer modules.
- Do not remove/replace shared code in consumer repos until a post-change rebaseline is documented.
- Rebaseline output should include:
  - Current shared-candidate inventory (`core-model`, `core-datalayer`, and safe database-common slices)
  - Explicit include/exclude decisions and rationale
  - Required compatibility/versioning actions for any changed DTO/model surfaces
- Use:
  - `scripts/rebaseline-audit.sh`
  - `docs/rebaseline/CHECKLIST.md`
