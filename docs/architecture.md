# Architecture, build, and validation

## M0 implementation

- `app/src/main/java/org/libreframe/app/MainActivity.kt`: local project list, Android document-picker import/export, preview, and trim controls.
- `EditProject.kt`: immutable project metadata and microsecond trim range.
- `ProjectStore.kt`: version-tagged JSON in app-private storage, written with `AtomicFile`.
- `TrimExporter.kt`: bounded-buffer `MediaExtractor` to `MediaMuxer` sample-copy export.
- `app/src/test/java/org/libreframe/app/EditProjectTest.kt`: trim-range and source-preservation tests.

Projects reference a persisted Storage Access Framework URI; they do not copy source media. Export uses a user-selected destination and removes the destination document on failure/cancellation where the provider allows deletion. An input grant may later be revoked; the app reports the error and asks for a new project rather than changing the source.

Timestamps are integer microseconds. The current project graph contains one source and one trim interval only. The JSON has a `schemaVersion` marker, but there is no migration framework yet. Edits are saved explicitly; autosave, undo/redo, export-resume, proxies, and a recovery journal are not implemented.

## Export limits

The exporter retains source codecs and copies compressed samples. It seeks to a preceding sync sample and therefore cannot promise frame-accurate trim boundaries. The selected MP4 muxer may reject otherwise readable input codecs, and audio is copied rather than mixed. There are no encoder fallbacks or silent quality changes. It does not currently probe device encoder capabilities, provide a percentage estimate, preserve HDR guarantees, or meet the stated A/V drift acceptance test.

The sample buffer is capped at 32 MiB; samples requiring more memory fail with an actionable error. Long videos are streamed sample by sample; the whole video is not decoded into RAM. These constraints do not establish device performance.

## Build and verification record

- Gradle Kotlin DSL project; minimum API 29, provisional compile/target API 36.
- Current toolchain in the implementation environment: JDK 17, Gradle 9.8.1, Android SDK platforms 34–37.
- Android Gradle Plugin 9.0.1 was selected from the current-version search result available during implementation and had no advisory-database finding. Gradle could not resolve the plugin from Google Maven or configured plugin repositories in this sandbox. Consequently no Android build, Gradle unit test, APK install, or device test is claimed.
- The standalone `EditProject.kt` source compiled with the installed Kotlin compiler. Full app compilation and behavior remain unverified until plugin resolution works.
- No emulator or physical-device performance, codec, color, accessibility, RTL, interruption, or thermal benchmark was run.

## Reference-device plan

The validation plan names these actual device families; each must be acquired, checked for its exact model/OS/RAM/codec capabilities, and tested before any support claim:

| Purpose | Proposed device | Status |
|---|---|---|
| API-29-era low-memory baseline | Samsung Galaxy A01, 2 GB variant | Proposed only; exact variant and API-29 test image/device availability unverified |
| Approximately 8 GB midrange | Google Pixel 7a | Proposed only; physical device and sustained-preview results unavailable |
| High-end 4K/60/HDR path | Google Pixel 8 Pro | Proposed only; codec/profile support and output path not verified |

This list is a test plan, not evidence that these devices meet any acceptance target. Record exact device model, Android build/API, region, account context (none for local workflows), inspection date, source, temperature/thermal state, preview resolution, frame drops, and test media in any future benchmark.

## Milestones

M0 is incomplete: only import, metadata persistence/reopen, trim-range editing, preview, and best-effort sample-copy export exist. The text-overlay and audio-mix steps of the requested M0 vertical slice are not implemented. M1–M5 require separate implementation and verification against the feature matrix; do not infer completion from this scaffold.
