# Changelog

All notable changes to `exposures-common`'s public surface (`core-model`, `core-datalayer`) are recorded here.

## [0.1.1] — 2026-08-18: CI/publishing infrastructure

No `core-model`/`core-datalayer` code changes — patch bump exists only to give `phone`/`watch`'s CI a real tagged commit to pin `setup-android-build` against (previously referenced an untagged commit SHA).

- Published to GitHub Packages for the first time (`0.1.0` was tagged and published in this same session, but predates this CI work — see below).
- Added `ci.yml` (test on push to `main`/PRs) and `publish.yml` (publish + create a GitHub release on `v*.*.*` tag push).
- Added `.github/actions/setup-android-build`, a composite action bundling checkout/JDK 17/Gradle setup, consumed cross-repo by `phone`/`watch`'s own CI (not self-referenced here — see the action's own description for why).
- All third-party Actions SHA-pinned with a `# vX.Y.Z` comment; `actions/checkout`/`actions/setup-java` specifically pinned to Node 24-native versions (v7.0.1/v5.7.0) to avoid the Node 20 deprecation warning both surfaced at v4.

## [0.1.0] — 2026-08-17: Initial baseline

First populated snapshot of `core-model` and `core-datalayer`, sourced from `exposures-watch` (Phase 0 rebaseline confirmed it as byte-identical to `exposures-phone` except two now-resolved should-converge drifts). See `docs/rebaseline/report-2026-08-17.md` for the full audit.

### core-model

19 files: `CameraBody`, `Exposure`, `FilmBack`, `FilmBackType`, `FilmColorType`, `FilmFormat`, `FilmRoll`, `FrameNumbering`, `Lens`, `LightMeter`, `LightMeterType`, `PhotoStatus`, `RollStatus`, `ShutterSpeed`, `StandardApertures`, `StandardIso`, `StopIncrement`, `SyncStatus`, `Zone`.

- `Zone.DEFAULT` (= 6) is part of the baseline. It's watch-only in current usage (the zone `Picker`'s initial value) — phone has no zone-entry UI — but is included per the compatibility policy's "unused fields are acceptable" rule.
- `ReferencePhoto` intentionally excluded: phone-only, no watch counterpart.

### core-datalayer

- `DataLayerPaths`: 7 DataItem paths (`CAMERA_BODIES`, `LENSES`, `LIGHT_METERS`, `FILM_BACKS`, `ROLLS`, `EXPOSURES`, `PHOTO_STATUSES`), 6 command paths, 1 capability string, 1 DataMap payload key.
- `DataLayerJson`: shared `kotlinx.serialization` `Json` instance (`ignoreUnknownKeys = true`) plus typed encode/decode helpers per DTO family.
- `dto/Dtos.kt`: `ShutterSpeedDto`, `CameraBodyDto`, `LensDto`, `LightMeterDto`, `FilmBackDto`, `FilmRollDto`, `ExposureDto`, `PhotoStatusDto`, `CapturePhotoCommand`, `CaptureResultCommand`, `CompleteRollCommand`.
- `mapper/DtoMappers.kt`, `DataLayerClient`, `DataLayerGateway`.

### Compatibility notes

No breaking changes possible yet — this is the first population, not a change against a previously-published contract. Once `phone`/`watch` consume this repo (Phase 3+), further entries in this file must classify each change per `docs/CONTRACT_COMPATIBILITY.md` (additive/patch vs. breaking/major) and note required coordinated-rollout steps.
