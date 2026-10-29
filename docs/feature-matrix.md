# F01–F75 implementation matrix

This is the product target, not a claim that the current prototype is complete. `Planned` means no connected implementation exists. `Partial` identifies the working subset and its remaining gap. No feature is marked Implemented or Verified until its complete workflow passes documented device tests.

## Shared evidence and scope

- **Current version/inspection:** repository inspection on 2026-10-29; Android application version `0.1.0`. No competitor app, official competitor documentation, region, account, or current entitlement was inspected. No competitive comparison is made.
- **Hardware/API:** app minimum API 29, provisional target API 36. Hardware acceleration, model, memory, codec, color, thermal, and performance requirements are not validated. Device test plan is in `docs/architecture.md`.
- **Connectivity:** implemented workflows are local/offline. There is no backend, account, model download, or network permission. Planned remote workflows have no service.
- **Free access:** no billing, ads, account, premium gate, export counter, watermark, or paid asset is included. This source-level inspection is not a release audit.
- **Rights:** app code is Apache-2.0. No third-party model, weight, font file, audio, footage, sticker, template, or stock asset is included. See `docs/privacy-and-licenses.md`.
- **Evidence convention:** a test location is not a passing test result. Android build/test and all device/demo results remain unverified because AGP dependency resolution failed in this environment. “None” means no implementation or evidence exists.

| ID | Target workflow | Status, implementation location, needs, and evidence |
|---|---|---|
| F01 | Non-destructive trim, split, reorder, duplicate, delete, replace, merge, with explicit ripple/gap semantics | **Partial** — trim-range metadata in `EditProject.kt`; `TrimExporter.kt` copies samples. No split/reorder/ripple/merge; boundaries may snap to a previous keyframe. Range tests: `EditProjectTest.kt`; not run. |
| F02 | Multiple visual/audio layers with overlay/PiP and visibility, mute, lock controls | **Planned** — no track graph or controls; no hardware/model/backend selected; no evidence. |
| F03 | Zoomable, accurate timeline with scrubbing, snapping, thumbnails, waveforms, selection, undo/redo | **Partial** — `VideoView` preview in `MainActivity.kt` provides platform playback/seek only; no timeline, thumbnails, waveforms, snapping, or undo. No device evidence. |
| F04 | Crop, rotate, mirror, position, scale, alignment, fit/fill, source orientation | **Planned** — no transform graph or export effect; no evidence. |
| F05 | Standard/custom aspect ratios and solid/image/blurred backgrounds | **Planned** — no canvas/background controls; no evidence. |
| F06 | Keyframes for transforms, opacity, effects, masks, and audio; copy/delete/retime | **Planned** — no keyframe model or editor; no evidence. |
| F07 | Editable easing and original in/out/loop animation presets | **Planned** — no animation engine or presets; no evidence. |
| F08 | Editable transitions with source handles and short-clip behavior | **Planned** — no transitions; no evidence. |
| F09 | Constant/curved speed changes with correct timing and pitch policy | **Planned** — no speed processing; no evidence. |
| F10 | Reverse, freeze, still extraction with bounded-memory processing | **Planned** — no reverse/freeze/still extraction; no evidence. |
| F11 | Genuine optical-flow slow motion and motion blur, distinct from frame duplication | **Planned** — no optical-flow model/effect; no evidence. |
| F12 | Animated shape/linear masks, feathering, inversion, compositing | **Planned** — no mask graph; no evidence. |
| F13 | Chroma key with picking, threshold, spill control, edge refinement | **Planned** — no keyer; no evidence. |
| F14 | Temporally consistent subject segmentation, cutout, replacement, correction | **Planned** — no segmentation model or service; exact model/rights/hardware not selected; no evidence. |
| F15 | Motion/object tracking, attachable graphics, loss indication and correction | **Planned** — no tracker; no evidence. |
| F16 | Stabilization with crop and quality controls | **Planned** — no stabilizer; no evidence. |
| F17 | Subject-aware aspect-ratio reframing with editable decisions | **Planned** — no reframing analysis; no evidence. |
| F18 | Adjustable original/licensed filters and user presets | **Planned** — no filters or asset presets; no evidence. |
| F19 | Exposure through vignette adjustments, reset and compare | **Planned** — no color adjustment graph; no evidence. |
| F20 | HSL, curves, LUT import, scopes and documented color behavior | **Planned** — no grading/scopes; no evidence. |
| F21 | Original blur/glow/glitch/film/distortion/lighting effects and stacks | **Planned** — no effects engine or presets; no evidence. |
| F22 | Opacity and documented blend modes with correct alpha/color-space math | **Planned** — no compositor; no evidence. |
| F23 | Distinct denoise, enhancement, upscaling, deblur, relight, flicker reduction | **Planned** — no tools/models; exact model rights and hardware needs not assessed; no evidence. |
| F24 | Reversible portrait adjustments and tracked reshaping without identity profiles | **Planned** — no portrait tools or biometric storage; no evidence. |
| F25 | Rich text, imported/licensed fonts, Unicode/RTL, fallback, styling | **Planned** — no text overlay or shaping path; no evidence. |
| F26 | Animated titles and original editable presets | **Planned** — no title renderer/presets; no evidence. |
| F27 | Rights-cleared stickers/emoji/graphics with transforms and keyframes | **Planned** — no bundled assets or sticker workflow; no evidence. |
| F28 | Manual subtitles and on-device speech captions with editable timing | **Planned** — no caption model/editor; no model/license/device evidence. |
| F29 | Real word timestamps and karaoke highlighting robust to transcript edits | **Planned** — no word alignment; no evidence. |
| F30 | Caption translation and bilingual layouts using licensed local models or funded backend | **Planned** — no translation model/backend; no model/license/service evidence. |
| F31 | Caption styling, batch changes, line breaking, punctuation, merge/split/overlap handling | **Planned** — no caption editor; no evidence. |
| F32 | Editable transcript and reversible silence/filler removal with preview | **Planned** — no transcription or silence analysis; no evidence. |
| F33 | SRT/WebVTT import/export and burn-in with format limitations | **Planned** — no subtitle formats or burn-in; no evidence. |
| F34 | Teleprompter with scroll, mirror, readability, interruption recovery | **Planned** — no teleprompter; no evidence. |
| F35 | Multitrack audio edit/mix, gain, fades, envelopes, ducking, normalization, clipping protection | **Planned** — existing export only copies source audio; no mixer or audio controls; no evidence. |
| F36 | Voiceover with permissions, latency/monitoring, interruption recovery | **Planned** — no recording workflow or microphone permission; no evidence. |
| F37 | Searchable redistributable music/SFX with export rights | **Planned** — no library/assets; no evidence. |
| F38 | Extract/detach ordinary unprotected video audio with timing | **Planned** — no audio-only export/detach flow; no evidence. |
| F39 | Beat/onset analysis, editable markers, synchronized edits | **Planned** — no beat detector; no evidence. |
| F40 | Voice effects, pitch/formant, tempo, pitch-preserving stretch | **Planned** — no audio DSP; no evidence. |
| F41 | Noise, speech and echo reduction with strength/compare | **Planned** — no denoise models/effects; no evidence. |
| F42 | Licensed vocal/instrumental/source separation | **Planned** — no separation model or rights audit; no evidence. |
| F43 | Offline TTS with supported voices, pronunciation, pacing, timeline placement | **Planned** — no voice pack/TTS; no license or language evidence. |
| F44 | Consent-based personal voice creation/cloning for authorized speakers | **Planned** — no voice-cloning model or consent workflow; no evidence. |
| F45 | Still-image editing with crop/rotate/resize/adjustments/overlays and preserved original | **Planned** — app accepts video only; no photo editor; no evidence. |
| F46 | Distinct photo cutout, replacement, removal, healing and inpainting tools | **Planned** — no photo pipeline/models; no evidence. |
| F47 | Reversible portrait retouching without compulsory upload or identity profiles | **Planned** — no photo retouching; no evidence. |
| F48 | Restoration/enhancement/colorization/relighting with generative limits disclosed | **Planned** — no tools/models; no evidence. |
| F49 | Generative fill/expansion/replacement/stylization with licensed models and resource disclosure | **Planned** — no generator/model/service; no evidence. |
| F50 | Collage/layout/thumbnail/cover/product graphics | **Planned** — no design workflow or templates; no evidence. |
| F51 | Slideshows, pan/zoom, transitions, music, selectable covers | **Planned** — no still-image/slideshow workflow; no evidence. |
| F52 | Versioned templates with replaceable slots, timing, crop, transitions, effects, beat alignment | **Planned** — no template schema or assets; no evidence. |
| F53 | Template discovery/import/export/sharing and moderated publishing | **Planned** — no feed/service/moderation; no evidence. |
| F54 | Rights-cleared stock media/graphics/content packs with search and license data | **Planned** — no asset library; no evidence. |
| F55 | Script/idea assistance through supported local model or funded service | **Planned** — no model/backend; no evidence. |
| F56 | Editable script-to-video assembly with licensed media, narration, captions and music | **Planned** — no assembly workflow/assets; no evidence. |
| F57 | Audited text-to-image generation with settings/cancel/resource needs | **Planned** — no generator/model; no evidence. |
| F58 | Genuine text/image-to-video generation with measured device/service limits | **Planned** — no generator or funded backend; no evidence. |
| F59 | Long-video highlights using scene/speech analysis, editable choices and reframing | **Planned** — no scene/speech analysis; no evidence. |
| F60 | Licensed, consent-respecting avatars with transparent AI labeling | **Planned** — no avatar model/service or consent workflow; no evidence. |
| F61 | Translation, dubbing, alignment and supported lip sync with editable results/errors | **Planned** — no models/service; no evidence. |
| F62 | Temporally coherent video restyling/generative extension | **Planned** — no temporal generator; no evidence. |
| F63 | Scene detection, organization, editable rough cuts without overwriting manual work | **Planned** — no scene analysis or edit-command history; no evidence. |
| F64 | Transactional drafts/autosave/reopen/process-death recovery/schema migration | **Partial** — `ProjectStore.kt` atomically persists and reloads explicit saves. No autosave, recovery journal, migration execution, or process-death test. |
| F65 | Names, duplication, folders/tags/search, relinking and portable archives | **Planned** — list/reopen only; no management/archive features. No evidence. |
| F66 | Optional encrypted cloud backup, retention, deletion, conflict handling | **Planned** — no cloud service or encryption design; local editing works without network. |
| F67 | Optional workspaces, sharing, review, revocation, versions, conflict-safe collaboration | **Planned** — no collaboration service; no evidence. |
| F68 | Photo-picker/scoped-storage import with persisted grants or copies and explicit errors | **Partial** — `MainActivity.kt` uses `ACTION_OPEN_DOCUMENT` and attempts to persist the read grant. Revocation is reported; photo import/copy strategy is absent. No device evidence. |
| F69 | Adaptive preview, thumbnail/waveform caches, proxies, bounded storage, original-quality export | **Partial** — `TrimExporter.kt` streams samples with a 32 MiB buffer cap. No adaptive cache/proxy policy or original-quality/export comparison test. |
| F70 | Supported resolution/FPS/bitrate/codec controls, including hardware-supported 1080p and 4K/60 | **Partial** — MP4 sample-copy only; no settings, encoder capability probe, or 4K/60 claim. |
| F71 | Validated HDR/wide-color/high-bit-depth paths and intentional SDR/error handling | **Planned** — no color metadata policy or validated HDR/SDR conversion; no evidence. |
| F72 | No compulsory application watermark/end card in any export | **Partial** — current sample-copy path adds neither; no text/effect/template/AI exports exist. No complete export-path audit. |
| F73 | MediaStore saving and Android sharing; authorized direct integrations only | **Partial** — export destination uses `ACTION_CREATE_DOCUMENT`; MediaStore save/share sheet/direct integrations are absent. |
| F74 | H.264/AAC MP4, validated added formats, WAV/M4A/images/GIF/subtitles/projects | **Partial** — MP4 muxer only, retaining whatever supported source audio/video tracks are present. H.264/AAC baseline is not guaranteed; no other output formats. |
| F75 | Explicit offline/online behavior and local tools/models working offline | **Partial** — current editor/export code has no network permission or service and can run locally. No model manager or offline installation/verification flow. |

## Next acceptance work

Finish M0 with a real overlay renderer and multi-track audio mix, then test import → trim → text → audio → save/reopen → export. Add a typed edit-command/project graph and migration tests before expanding feature scope. For every milestone, record actual build/test output, independently inspect exported media, and exercise the named physical-device plan; update each row without upgrading status based on code presence alone.
