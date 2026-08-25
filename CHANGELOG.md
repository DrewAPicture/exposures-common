# Changelog

All notable changes to `exposures-common`'s public surface (`core-model`, `core-datalayer`, `core-database-common`) are recorded here.

## [0.8.0] — 2026-08-25: Zone → EV (exposure value)

Minor bump (matches this repo's precedent of staying pre-1.0 for breaking changes) — replaces the Ansel Adams Zone System (`Int` 0..10, roman-numeral labels) with a plain numeric EV (exposure value) scale, `Int` 1..20, default 10.

`core-model`: `Zone` → `ExposureValue` (`MIN`/`MAX`/`DEFAULT` = 1/20/10; `label()` now returns the plain number rather than a roman numeral), `Exposure.zone` → `exposureValue`. `core-datalayer`: `ExposureDto.zone` → `exposureValue`, `DtoMappers.kt` updated. `docs/contracts/data-layer.json` regenerated.

Breaking (rename + range/default change) but no consumer-facing migration needed — both apps are pre-launch. Both phone and watch must bump `exposuresCommon` to 0.8.0, update consumer code, and rename their own `zone`-named DB columns/UI/exports to match (exports label the field "Exposure Value"; in-app UI uses the short label "EV").

## [0.7.0] — 2026-08-24: FilmMedium (roll and sheet film)

Minor bump (matches this repo's precedent of staying pre-1.0 for breaking changes) — the tracked unit of film is now `FilmMedium`, typed `ROLL` or `SHEET` via the new `FilmMediumType` enum. A `SHEET` medium (large-format film exposed one sheet at a time, no roll back) leaves `filmBackId` unset; a `ROLL` medium requires it as before. `targetFrameCount` covers both cases unchanged — for `SHEET` it's the sheet count in the pack.

`core-model`: `FilmRoll` → `FilmMedium` (`filmBackId` now nullable, new `type: FilmMediumType` field), `RollStatus` → `FilmMediumStatus`, `Exposure.filmRollId` → `filmMediumId`. `core-datalayer`: `FilmRollDto` → `FilmMediumDto`, `CompleteRollCommand` → `CompleteFilmMediumCommand` (field `rollId` → `filmMediumId`), `DataLayerPaths.ROLLS`/`COMPLETE_ROLL_COMMAND`/`REQUEST_ROLLS_SYNC_COMMAND` → `FILM_MEDIA`/`COMPLETE_FILM_MEDIUM_COMMAND`/`REQUEST_FILM_MEDIA_SYNC_COMMAND`, matching `DataLayerJson` encode/decode functions renamed. `core-database-common`: `Converters.fromRollStatus`/`toRollStatus` → `fromFilmMediumStatus`/`toFilmMediumStatus`, plus new `fromFilmMediumType`/`toFilmMediumType`. `docs/contracts/data-layer.json` regenerated.

Breaking (rename + nullability change) but no consumer-facing migration needed — both apps are pre-launch. Both phone and watch must bump `exposuresCommon` to 0.7.0, update consumer code, and rename their own `FilmRoll`-named DB entities/DAOs/sync classes/UI to match.

## [0.6.0] — 2026-08-24: Remove CAPTURE_PHOTO_COMMAND

Minor bump (matches this repo's precedent of staying pre-1.0 for breaking changes) — removes `CAPTURE_PHOTO_COMMAND` (`/command/capture-photo`) and `CapturePhotoCommand`. The watch no longer requests photo capture via a separate message; the phone now triggers capture directly from the exposure-sync merge step when it observes a genuinely new exposure ID, eliminating a `DataClient`/`MessageClient` ordering race that could silently drop capture requests.

Breaking (command/DTO removal) but no consumer-facing migration needed — both apps are pre-launch. Both phone and watch must bump `exposuresCommon` to 0.6.0 and update the corresponding consumer code.

Note: the changelog's previous newest entry was 0.4.0, but the repo was already tagged at 0.5.0 (`isFavorite` on `Exposure`) — that entry was never added here. Flagging the gap rather than backfilling it retroactively.

## [0.4.0] — 2026-08-21: Lens focal length model (Prime + Zoom)

Minor bump — new lens/exposure fields, additive.

Adds the feature-backlog Item 001 data model — a lens is now typed `PRIME` (single fixed focal length) or `ZOOM` (a min/max mm range), and an exposure records the focal length actually used:

- `core-model`: new `LensType` enum (`PRIME`, `ZOOM`). `Lens` gains `lensType` (defaults `PRIME`), `focalLengthMm`, `focalLengthMinMm`, `focalLengthMaxMm`, plus `availableFocalLengths()` (mirrors `availableApertures()` — a single value for PRIME, every whole mm in range for ZOOM). `Exposure` gains `focalLengthMm: Int?`.
- `core-datalayer`: `LensDto`/`ExposureDto` gain the matching fields; `DtoMappers.kt` updated.
- `core-database-common`: `LensEntity` gains the matching columns; `Converters` gains a `LensType` converter; `Mappers.kt` updated.
- `docs/contracts/data-layer.json` regenerated.

No back-compat handling — both apps are pre-launch, so there's no deployed "previous version" of these DTOs/entities to stay compatible with.

## [0.3.0] — 2026-08-20: create-exposure command pair (Google Assistant voice capture, Phase A)

Minor bump — new command pair, purely additive, no changes to existing DTOs/paths.

Adds the `CreateExposureCommand`/`CreateExposureAckCommand` pair backing the phone-originated, watch-authoritative voice-capture flow (see `exp--google-assistant-capture-plan.md`):

- `dto/Dtos.kt`: `CreateExposureCommand` (`exposureId`, `shutterSpeed`, optional `lensId`/`aperture`/`isoUsed`/`notes`) and `CreateExposureAckCommand` (`exposureId`, `accepted`, optional `reason`).
- `DataLayerPaths`: `CREATE_EXPOSURE_COMMAND = "/command/create-exposure"` (phone -> watch) and `CREATE_EXPOSURE_ACK_COMMAND = "/command/create-exposure-ack"` (watch -> phone).
- `DataLayerJson`: matching `encode/decodeCreateExposureCommand` and `encode/decodeCreateExposureAckCommand`.
- `docs/contracts/data-layer.json` regenerated to include both new paths and schemas.

### Compatibility notes

Purely additive — two new command paths and two new DTOs, no existing field/path changes. `exposureId` doubles as the client-generated idempotency key on the watch receiver (Phase B).

## [0.2.0] — 2026-08-18: core-database-common (Phase 6)

Minor bump — new module, purely additive, no changes to `core-model`/`core-datalayer`.

Adds `core-database-common`, extracting the `core-database` pieces the rebaseline audit has consistently found byte-identical between `phone`/`watch` and free of the FK/repository divergence that keeps the rest of `core-database` local (Phase 6 is explicitly marked optional in the plan):

- `Converters.kt` — Room `TypeConverter`s for `core-model` enums/value types.
- `entity/{CameraBodyEntity,LensEntity,LightMeterEntity}.kt`. `LensEntity`'s real `@ForeignKey` to `CameraBodyEntity` moves safely since both sides of that FK are in this module.
- `mapper/Mappers.kt` — only the `CameraBody`/`Lens`/`LightMeter` `toDomain()`/`toEntity()` functions, not the full file.

Not extracted: `ExposureEntity`, `FilmBackEntity`, `FilmRollEntity` (real `@ForeignKey` on phone, `@Index`-only on watch — intentional divergence from `exp-real-device-fixes-plan.md` Phase 8), `ReferencePhotoEntity` (phone-only), `AppStateEntity`/`CaptureRequestOutboxEntity` (watch-only), and the remaining local repositories/DAOs/`ExposuresDatabase`.

A plain `kotlin-jvm` module (not an Android library like `core-datalayer`): `androidx.room:room-common` resolves to `room-common-jvm`, a plain JAR with just the Room annotations — no AGP/Android SDK needed since nothing here touches actual Android APIs.

### Compatibility notes

Purely additive (new module, new artifact coordinate `com.exposures.common:core-database-common`) — no existing consumer behavior changes.

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
