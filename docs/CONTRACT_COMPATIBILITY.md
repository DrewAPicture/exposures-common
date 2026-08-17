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

- Use OpenAPI for actual HTTP boundaries (primarily phone backend APIs).
- Use a dedicated Data Layer contract spec for watch<->phone payloads:
  - DataItem paths
  - Message command paths
  - DTO schema/defaults/requiredness

## CI guardrails

- Require contract encode/decode tests to pass in this repo.
- Add consumer CI checks to keep phone/watch pinned to the same compatible shared version.
