# Changelog

All notable changes to `exposures-common`'s public surface (`core-model`, `core-datalayer`) are recorded here. Not yet published as a versioned artifact — see `README.md`'s migration strategy — so this currently tracks in-repo baselines rather than release tags.

## [Unreleased] — 2026-08-17: Initial baseline

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
