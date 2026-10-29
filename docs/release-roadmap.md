# Release roadmap: a reliable, non-AI editor

Planning baseline: 2026-10-29, application version `0.1.0`. Repository review found no published GitHub releases or open issues. Versions below are proposals, not published releases or delivery-date commitments. This change documents work; it does not implement any feature.

## Product boundaries

Every phase is local-first, free, and **non-AI**. AI features are excluded, not reserved for a later phase. Do not add model downloads, inference SDKs, on-device or cloud AI processing, automatic speech captions, translation/dubbing, voice synthesis/cloning, subject recognition, or generative media. Offline inference is still AI and is not an exception.

Manual subtitles, user-written text, deterministic image transforms, ordinary compositing, and conventional audio mixing are eligible. Dependencies must be checked for hidden model downloads or AI services. No phase requires an account, backend, ads, payment, watermark, or upload. Cloud-backed document providers may require their own connectivity; offline support applies to locally readable media.

## Feature review

The [feature matrix](feature-matrix.md) is broader than the executable app. Ten rows are Partial; none is Implemented or Verified. Source presence and existing test files are not proof of a passing workflow.

| Area | Present in the source | Release gap |
|---|---|---|
| Import and preview: F03, F68 | Video document picker, attempted persisted read grant, platform `VideoView` playback | No timeline or selected-range preview; missing media requires a new project rather than relinking. |
| Editing: F01 | One source, numeric trim bounds, immutable metadata | No split, sequence, reorder, undo/redo, compositing, text, or audio edits. |
| Drafts: F64, F65 | Explicit atomic JSON saves and project reopen/list | No autosave or recovery tests. The schema marker is written but not validated/migrated; unreadable projects are silently omitted from the list. No rename/delete UI. |
| Export: F69, F70, F74 | Bounded-buffer encoded-sample copy into MP4 | Start may precede the requested trim. Source codecs are retained; H.264/AAC output is not guaranteed. No rendering, encoder profiles, or independently checked A/V sync. |
| Job safety | Activity-owned executor and cancellation flag | Activity destruction cancels work. Job/destination state is not durable, overlapping exports are not prevented, and successful partial-file deletion is not verified. |
| Offline/free access: F72, F75 | No Internet permission, account, billing, ads, or added watermark in the current path | Needs device-level offline and export checks; this is not yet a release audit. |
| Build and distribution | Gradle project and two trim-model unit tests | The existing verification record reports AGP resolution failure, not a successful Android build. No CI workflow, installed-app results, or physical-device report is present. |

Evidence: [architecture and verification record](architecture.md), [app configuration](../app/build.gradle.kts), [activity](../app/src/main/java/org/libreframe/app/MainActivity.kt), [project model](../app/src/main/java/org/libreframe/app/EditProject.kt), [store](../app/src/main/java/org/libreframe/app/ProjectStore.kt), [exporter](../app/src/main/java/org/libreframe/app/TrimExporter.kt), and [unit tests](../app/src/test/java/org/libreframe/app/EditProjectTest.kt).

## Release sequence

M0 remains an incomplete foundation. Reliability comes before expanding the editor. Its originally requested text/audio vertical slice moves to M3; shipping M1 must not be described as completing that slice.

| Phase | Proposed release | User outcome | Required deliverables |
|---|---|---|---|
| **M1: next phase** | **0.2.0, offline trimming alpha** | Reliably import, trim, save/recover, and export one supported video | Reproducible build/CI, recoverable projects, source relinking, honest sample-copy boundaries, safe export jobs, signed alpha and evidence pack |
| M2 | 0.3.0, timeline alpha | Assemble multiple clips with reversible edits and accurate rendered export | Versioned clip graph, edit commands, timeline, shared preview/export composition, H.264/AAC rendering |
| M3 | 0.4.0, everyday editing alpha | Crop/reframe, add manual titles and music, and export the complete edit | Manual visual tools, text renderer, source-plus-music mix, original M0 end-to-end demonstration |
| M4 | 0.5.0, layers and manual subtitles alpha | Use overlays, simple transitions, and manually authored/imported captions | Bounded multitrack composition, cross-dissolve, manual caption editor, SRT/WebVTT workflows |
| M5 | 0.6.0, public-beta candidate | Edit and share predictably on declared reference devices | Bounded caches/proxies, supported export profiles, MediaStore/share sheet, accessibility/localization, beta release audit |

Order is dependency-based, not a calendar estimate. M2 depends on M1's storage/job guarantees; M3 depends on M2's renderer; M4 extends that renderer; M5 validates the resulting complete workflows. Estimate each phase after its predecessor's evidence is available. Do not announce a 1.0 date or claim feature parity from this plan.

## M1: next release execution plan

### Scope and architecture

Keep the release deliberately small: one video and optional source audio, trim, draft recovery, relink, and MP4 sample-copy export. The initial validation target is SDR MP4 containing one H.264 video track, up to 1080p/30 fps, with optional AAC-LC mono/stereo audio. This is a proposed test envelope, not current support evidence. Probe actual tracks and reject unsupported inputs before starting export; do not silently transcode or promise support for HEVC, VFR, HDR, or 4K.

Retain Views and `AtomicFile` for M1 as a proposed alternative to a wholesale Compose/Room/DataStore migration. Record and review that architecture decision before implementation. Introduce AndroidX lifecycle/Activity Result APIs and structured coroutine job state. Move export ownership out of the activity and satisfy the applicable Android foreground/background execution and notification rules. Keep integer-microsecond project timing.

M1 does not introduce a timeline, multiple clips, text/effects rendering, audio mixing, or frame-accurate re-encoding. Preserve the existing sample-copy path only with an explicit, tested boundary policy.

### Work packages

Assign a named implementation owner and reviewer to each package at kickoff; no individual or completion date is assumed here.

| Package | Priority / dependency | Deliverable and completion check |
|---|---|---|
| N1: establish the build | P0 / none | Resolve the toolchain/dependency issue, verify wrapper integrity, and pin a tested toolchain. Add clean-checkout CI for `:app:testDebugUnitTest`, `:app:assembleDebug`, and `:app:lintDebug`. Preserve logs and an installable debug artifact tied to a commit. Do not change versions by guesswork. |
| N2: protect drafts and state | P0 / N1 | Validate schema versions, migrate supported older data, reject newer schemas without overwriting them, and surface corrupt-project errors with recovery options. Autosave accepted edits before acknowledging them. Restore editor and pending picker/job state after recreation; mark killed jobs interrupted rather than complete. Add storage, migration, and recovery tests. |
| N3: make media access recoverable | P0 / N2 | Preflight read grants and media metadata; relink missing sources in the existing project. Confirm changed duration/identity before reconciling trims. Add project deletion that never deletes original media and releases only grants unused by other projects. Report provider, permission, and storage failures explicitly. |
| N4: make export trustworthy | P0 / N2, N3 | Persist job state; serialize or reject overlapping exports. Show requested versus effective sync-sample trim bounds and confirm any difference. Preview the effective range. Report status/cancellation honestly; only show percentages if measurable. Verify cleanup and identify any partial output the provider refuses to delete. Interrupted exports restart into a fresh destination, not a half-written file. |
| N5: qualify and package the alpha | P0 / N1-N4 | Cover the acceptance matrix below with automated tests where practical and recorded device checks. Give start/end fields distinct accessible labels, retain invalid input for correction, and support large text. Produce a signed alpha APK, checksum, release notes, known limits, notices, and a commit-linked verification report. Keep signing credentials outside the repository. |
| N6: small project-list polish | P1 / N3 | Rename projects and display useful saved/source status. This is the first scope cut if it threatens a P0 deliverable; renaming must not change source identity. |

### M1 release gates

- Clean CI passes the unit tests, debug build, and lint with no unresolved release-blocking findings. Install and exercise the resulting APK, rather than treating compilation as workflow evidence.
- Run the supported-media corpus on API 29 and API 36 emulator images plus a physical low-memory and a physical midrange device. The device families in the architecture document remain proposals until acquired; record exact substitutions and capabilities.
- Complete every mandatory import -> trim -> save/reopen -> export case, plus ten consecutive full-cycle repetitions per physical reference device. Include portrait rotation, video without audio, AAC mono/stereo, short clips, and a ten-minute A/V reference fixture.
- Independently inspect exported streams and decode the result. The first decodable frame and output duration must match the **reported effective bounds** within one video frame or audio packet, whichever is longer. Measure A/V offset at the start, middle, and end of the ten-minute fixture; target absolute error <= 50 ms. This is not a frame-accuracy claim against arbitrary requested bounds.
- Across ten forced-process-death recovery cycles, retain the last acknowledged edit. Exercise cancelled pickers, revoked grants, unavailable providers, corrupt/newer-schema projects, full storage, export cancellation, navigation/recreation/backgrounding, and provider cleanup refusal. None may silently lose a draft or report a failed/interrupted job as complete.
- Repeat the locally stored-media workflow in airplane mode. Confirm unchanged source-file hashes, no added application watermark/end card, no application network permission, and no account/payment path.
- Record peak memory, export duration, and device/thermal conditions as baselines, not performance claims. Attach fixture provenance/licenses, exact toolchain, commit, test output, failures, and output-inspection results to the release evidence.
- Before public distribution, clear the provisional app/package name, current target-API policy, codec/distribution obligations, privacy disclosures, and dependency notices. A failed gate means no public release; do not bypass it by relabeling unsupported behavior.

## M2: a real sequential editor

Feature focus: F01, F03, F64, F65, F69, and the rendering subset of F70/F74.

**Deliverables**

- A typed, versioned source/clip/sequence graph with stable IDs, source-to-timeline time mapping, and migrations from M1 projects. Define ripple versus gap behavior explicitly.
- Commands for add, trim, split, reorder, duplicate, delete, replace, and merge; zoomable timeline, selection, scrubbing, snapping, bounded thumbnails, and undo/redo. Persist accepted commands without changing originals.
- A local rendering-engine decision and compatibility spike, evaluating maintained options such as Media3 composition against the required API, codec, timing, and license constraints. Deliver a shared preview/export graph and tested 720p30/1080p30 SDR H.264/AAC output. Arbitrary multi-clip edits must render correctly; concatenating compressed samples is not an acceptable substitute. Keep sample-copy only as a verified eligible fast path.

**Exit gate:** a ten-clip project survives save/reopen and migration; undoing/redoing 100 commands restores the expected graph exactly. Independently decoded exports match the sequence and cuts within one output frame, maintain <= 50 ms A/V offset on the reference corpus, and never overwrite source media. Do not expose a preview-only edit as exportable.

## M3: manual picture, titles, and music

Feature focus: F04/F05, basic F19, the manual/system-font subset of F25, and the basic mix subset of F35. These subsets do not complete the entire feature rows.

**Deliverables**

- Crop, rotate, mirror, position/scale, fit/fill, common aspect ratios, solid backgrounds, and deterministic exposure/contrast/saturation with reset/compare.
- User-written, timed text with alignment, basic styling, system-font fallback, Unicode/RTL, and matching export rendering. No generated writing, animated-title pack, or downloadable font library.
- Source-audio mute/gain plus one user-supplied music track with placement, trim, gain, fades, and explicit clipping warnings. No automatic ducking, speech enhancement, separation, or synthesized voices.

**Exit gate:** visual reference fixtures confirm orientation, crop, colors, and title placement in both preview and exported frames. Audio fixtures confirm placement within one output frame, measured gain/fades, and visible warnings for deliberate clipping cases. Demonstrate import -> trim/sequence -> text -> audio -> save/reopen -> export offline. Only then record the original M0 text/audio vertical slice as complete.

## M4: layers and manual subtitles

Feature focus: a bounded F02 workflow, one F08 transition, normal-alpha F22, manual F28, and F31/F33 subsets.

**Deliverables**

- At least two visual layers and four audio tracks in the declared reference profile, including picture-in-picture and layer visibility/mute/lock. Use normal alpha compositing; advanced blend modes remain deferred.
- An editable cross-dissolve with explicit source-handle and too-short-clip behavior; reject invalid transitions rather than silently changing duration.
- A manual caption editor with timing, styling, line wrapping, merge/split, and overlap diagnostics. Support SRT and a documented text-cue subset of WebVTT import/export plus burn-in. Report unsupported format constructs rather than dropping them silently.

**Exit gate:** a two-visual/four-audio reference project previews and exports the same timing and composition within one output frame, including cross-dissolves and short-clip failures. Supported caption text/timing round-trips with <= 1 ms timestamp rounding; malformed input and unsupported WebVTT constructs produce actionable diagnostics. All captions are typed or imported by the user; no speech recognition, word alignment, or translation dependency is present.

## M5: performance and public-beta readiness

Feature focus: F69, tested F70/F74 profiles, F72/F73/F75, plus regression coverage of earlier phases.

**Deliverables**

- Bounded thumbnail/waveform caches, eviction and clear-cache controls, adaptive preview, and an explicitly budgeted proxy path. Export must read original media, not silently use lower-quality proxies.
- Capability-probed H.264/AAC resolution/FPS/bitrate profiles, actionable unsupported-profile errors, MediaStore saving, and the Android share sheet. Retain the document-picker destination option. No 4K/60, HDR, wide-color, or extra-format promise without a separate tested scope decision.
- TalkBack and 200% font-size checks, human-authored localization with an RTL test locale, interruption/thermal/storage testing, migration regressions, and a reviewed privacy/license/free-access release checklist.

**Exit gate:** on the declared midrange reference profile, target warm-cache p95 scrub response <= 250 ms and at least 95% of expected frames during a 60-second 1080p30 preview. Low-memory devices may use a declared lower-resolution preview, never a silently degraded export. Complete a ten-minute reference export without OOM on both physical reference classes and publish measured peak memory/thermal results. These are proposed acceptance targets, not measurements; if missed, narrow the advertised profile and repeat qualification. All mandatory workflows pass and no critical/high release-blocking defect remains before a public beta.

## Deferred and excluded work

AI-focused historical targets F14, F17, F29/F30, F32, F42-F44, F48/F49, and F55-F62 are excluded from this roadmap. Mixed targets such as F23, F24, F28, F41, F46/F47, and F63 admit only explicitly manual or deterministic non-model subsets; their AI portions remain excluded.

Other non-AI targets are not automatic commitments. Keyframes, speed ramps, masks/chroma key, classical optical flow/tracking/stabilization, advanced grading, voiceover/DSP, photo editing, templates/stock packs, portable project archives, and cloud/collaboration require separate proposals after these phases. No backend is funded or designed by this plan. Do not add those dependencies to the next release.

## Delivery controls and risks

| Risk or dependency | Required control |
|---|---|
| Build resolution or devices remain unavailable | Block the release gate; record the actual failure and acquisition/runner need. Never substitute source inspection for build/device evidence. |
| Chosen render engine cannot represent an edit or codec | Prove preview/export support during the M2 spike before adding the UI; reject unsupported cases rather than introducing silent approximations. |
| Schema or URI changes make old projects unreadable | Keep migration fixtures and recovery copies; preserve unsupported/corrupt originals, expose errors, and test shared/revoked grants. |
| Long-running work exceeds OS, memory, or thermal limits | Validate foreground/background execution on target APIs; measure on physical devices, bound caches, and reduce only declared preview profiles. |
| Dependency, asset, codec, or public-name rights are unresolved | Maintain the license inventory and block distribution until the relevant review is complete. Do not add an asset pack or inference SDK as a shortcut. |

For each phase, deliver implementation and tests, project migrations where needed, an installable artifact, changelog/known limits, updated feature-matrix evidence, and a reproducible device/media report. Keep rows Partial until the complete row workflow passes its documented acceptance tests. Any scope change must update this roadmap, dependencies, release notes, and matrix together; adding AI is outside the product boundary, not a stretch goal.
