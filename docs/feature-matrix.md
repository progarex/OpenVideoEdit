# F01–F75 implementation matrix

This retains the historical F01–F75 targets for traceability, not as a claim that the prototype is complete or that every target will ship. `Planned` means an unimplemented candidate, not a release commitment; only explicitly scheduled subsets in the [non-AI release roadmap](release-roadmap.md) are in scope. `Excluded` means outside that roadmap. `Partial` identifies the working subset and its remaining gap. No feature is marked Implemented or Verified until its complete workflow passes documented device tests.

AI features are excluded from every planned phase, including on-device inference, model downloads, and cloud AI. Mixed workflows may include only their explicitly manual or deterministic non-model components. References to models below describe missing historical capabilities, not permission to implement them.

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
| F14 | Temporally consistent subject segmentation, cutout, replacement, correction | **Excluded** — subject-segmentation AI is outside the roadmap; no model, service, implementation, or evidence. |
| F15 | Motion/object tracking, attachable graphics, loss indication and correction | **Planned** — no tracker; no evidence. |
| F16 | Stabilization with crop and quality controls | **Planned** — no stabilizer; no evidence. |
| F17 | Subject-aware aspect-ratio reframing with editable decisions | **Excluded** — subject-aware analysis is outside the roadmap; manual canvas/transform tools are tracked under F04/F05. No evidence. |
| F18 | Adjustable original/licensed filters and user presets | **Planned** — no filters or asset presets; no evidence. |
| F19 | Exposure through vignette adjustments, reset and compare | **Planned** — no color adjustment graph; no evidence. |
| F20 | HSL, curves, LUT import, scopes and documented color behavior | **Planned** — no grading/scopes; no evidence. |
| F21 | Original blur/glow/glitch/film/distortion/lighting effects and stacks | **Planned** — no effects engine or presets; no evidence. |
| F22 | Opacity and documented blend modes with correct alpha/color-space math | **Planned** — no compositor; no evidence. |
| F23 | Distinct denoise, enhancement, upscaling, deblur, relight, flicker reduction | **Planned, non-AI candidates only** — deterministic processing is deferred beyond M5; learned enhancement/upscaling/relighting is excluded. No tools or evidence. |
| F24 | Reversible portrait adjustments and tracked reshaping without identity profiles | **Planned, manual candidates only** — manual adjustments are deferred beyond M5; AI portrait analysis/reshaping is excluded. No portrait tools, biometric storage, or evidence. |
| F25 | Rich text, imported/licensed fonts, Unicode/RTL, fallback, styling | **Planned** — no text overlay or shaping path; no evidence. |
| F26 | Animated titles and original editable presets | **Planned** — no title renderer/presets; no evidence. |
| F27 | Rights-cleared stickers/emoji/graphics with transforms and keyframes | **Planned** — no bundled assets or sticker workflow; no evidence. |
| F28 | Manual subtitles; historical on-device speech-caption target excluded | **Planned, manual subset only** — M4 schedules user-authored/imported captions and editable timing. Speech recognition and automatic captions are excluded. No editor or device evidence. |
| F29 | Real word timestamps and karaoke highlighting robust to transcript edits | **Excluded** — automatic word alignment is outside the roadmap; manual per-word highlighting is not scheduled either. No implementation or evidence. |
| F30 | Caption translation and bilingual layouts using licensed local models or funded backend | **Excluded** — model-based translation is outside the roadmap; manual bilingual layouts are not scheduled. No model, backend, or evidence. |
| F31 | Caption styling, batch changes, line breaking, punctuation, merge/split/overlap handling | **Planned** — no caption editor; no evidence. |
| F32 | Editable transcript and reversible silence/filler removal with preview | **Excluded** — transcript/semantic editing is outside the roadmap; no transcription, silence analysis, or evidence. |
| F33 | SRT/WebVTT import/export and burn-in with format limitations | **Planned** — no subtitle formats or burn-in; no evidence. |
| F34 | Teleprompter with scroll, mirror, readability, interruption recovery | **Planned** — no teleprompter; no evidence. |
| F35 | Multitrack audio edit/mix, gain, fades, envelopes, ducking, normalization, clipping protection | **Planned** — existing export only copies source audio; no mixer or audio controls; no evidence. |
| F36 | Voiceover with permissions, latency/monitoring, interruption recovery | **Planned** — no recording workflow or microphone permission; no evidence. |
| F37 | Searchable redistributable music/SFX with export rights | **Planned** — no library/assets; no evidence. |
| F38 | Extract/detach ordinary unprotected video audio with timing | **Planned** — no audio-only export/detach flow; no evidence. |
| F39 | Beat/onset analysis, editable markers, synchronized edits | **Planned** — no beat detector; no evidence. |
| F40 | Voice effects, pitch/formant, tempo, pitch-preserving stretch | **Planned** — no audio DSP; no evidence. |
| F41 | Noise, speech and echo reduction with strength/compare | **Planned, non-AI candidates only** — conventional DSP is deferred beyond M5; learned speech enhancement is excluded. No effects or evidence. |
| F42 | Licensed vocal/instrumental/source separation | **Excluded** — source-separation models are outside the roadmap; no implementation or rights/device evidence. |
| F43 | Offline TTS with supported voices, pronunciation, pacing, timeline placement | **Excluded** — speech synthesis and voice packs are outside the roadmap; no implementation or evidence. |
| F44 | Consent-based personal voice creation/cloning for authorized speakers | **Excluded** — voice creation/cloning is outside the roadmap; no model, consent workflow, or evidence. |
| F45 | Still-image editing with crop/rotate/resize/adjustments/overlays and preserved original | **Planned** — app accepts video only; no photo editor; no evidence. |
| F46 | Distinct photo cutout, replacement, removal, healing and inpainting tools | **Planned, manual candidates only** — deterministic photo tools are deferred beyond M5; model-based removal/inpainting is excluded. No photo pipeline or evidence. |
| F47 | Reversible portrait retouching without compulsory upload or identity profiles | **Planned, manual candidates only** — deterministic retouching is deferred beyond M5; AI portrait processing is excluded. No implementation or evidence. |
| F48 | Restoration/enhancement/colorization/relighting with generative limits disclosed | **Excluded** — restoration/generative enhancement is outside the roadmap; no tools, models, or evidence. |
| F49 | Generative fill/expansion/replacement/stylization with licensed models and resource disclosure | **Excluded** — generative image editing is outside the roadmap; no generator, service, or evidence. |
| F50 | Collage/layout/thumbnail/cover/product graphics | **Planned** — no design workflow or templates; no evidence. |
| F51 | Slideshows, pan/zoom, transitions, music, selectable covers | **Planned** — no still-image/slideshow workflow; no evidence. |
| F52 | Versioned templates with replaceable slots, timing, crop, transitions, effects, beat alignment | **Planned** — no template schema or assets; no evidence. |
| F53 | Template discovery/import/export/sharing and moderated publishing | **Planned** — no feed/service/moderation; no evidence. |
| F54 | Rights-cleared stock media/graphics/content packs with search and license data | **Planned** — no asset library; no evidence. |
| F55 | Script/idea assistance through supported local model or funded service | **Excluded** — AI writing assistance is outside the roadmap; no model, backend, or evidence. |
| F56 | Editable script-to-video assembly with licensed media, narration, captions and music | **Excluded** — script-to-video generation is outside the roadmap; no workflow, assets, or evidence. |
| F57 | Audited text-to-image generation with settings/cancel/resource needs | **Excluded** — image generation is outside the roadmap; no model or evidence. |
| F58 | Genuine text/image-to-video generation with measured device/service limits | **Excluded** — video generation is outside the roadmap; no model, backend, or evidence. |
| F59 | Long-video highlights using scene/speech analysis, editable choices and reframing | **Excluded** — automatic highlight selection is outside the roadmap; no analysis or evidence. |
| F60 | Licensed, consent-respecting avatars with transparent AI labeling | **Excluded** — AI avatars are outside the roadmap; no model, service, consent workflow, or evidence. |
| F61 | Translation, dubbing, alignment and supported lip sync with editable results/errors | **Excluded** — automatic translation/dubbing/lip sync is outside the roadmap; no model, service, or evidence. |
| F62 | Temporally coherent video restyling/generative extension | **Excluded** — generative video editing is outside the roadmap; no generator or evidence. |
| F63 | Scene detection, organization, editable rough cuts without overwriting manual work | **Planned, non-AI candidates only** — deterministic analysis is deferred beyond M5; learned/semantic scene selection is excluded. No analysis or edit-command history; no evidence. |
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
| F75 | Explicit offline/online behavior and local tools working offline | **Partial** — current editor/export code has no network permission or service and can run locally. Offline installation/workflow verification is absent; model management is excluded, not a missing release requirement. |

## Next acceptance work

Follow the [release roadmap](release-roadmap.md): first qualify M1's build, draft recovery, source access, and honest single-clip export. M2 adds the typed edit-command/project graph and shared preview/export renderer; M3 completes the original import → trim → text → audio → save/reopen → export slice. M4 adds layers/manual subtitles and M5 qualifies the beta. AI targets remain excluded throughout. For every milestone, record actual build/test output, independently inspect exported media, and exercise the named physical-device plan; update each row without upgrading status based on a plan or code presence alone.
