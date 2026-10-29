# Privacy, free access, and license manifest

## Data flow

| Data | Current handling |
|---|---|
| Imported media | Selected through Android `ACTION_OPEN_DOCUMENT`; the app retains the granted URI and reads it locally. No upload or media copy is made by the project store. |
| Project metadata | Stored as JSON under app-private files. It contains the source URI, display name, trim range, and timestamps. |
| Export | Written only to the destination selected through Android `ACTION_CREATE_DOCUMENT`. |
| Network, account, analytics | None. The app does not declare Internet permission, use accounts, or include analytics. |
| Deletion/retention | Projects remain until app data is cleared or the project is manually deleted (project deletion UI is not implemented). Source files remain under the user's control. |

The app does not currently request camera, microphone, contacts, or broad storage permissions. SAF grants can be revoked by the user or provider; the app must not silently upload or substitute media. There is no backend, cloud sync, model service, or remote processing path.

## Free-access contract

No billing library, entitlement check, subscription, credit, ad, trial, premium asset, watermark-removal path, export limit, or paid quality gate is present. No feature is unlocked through payment. Unsupported features are not exposed as mock controls. This is verified by source/dependency inspection only, not a release audit.

## License and rights inventory

| Material | Current choice/state | Release requirement |
|---|---|---|
| Original application source | Apache-2.0; see repository `LICENSE` | Preserve notices and review any future dependency's license and obligations. |
| Android platform APIs | Platform-provided; no Android runtime library is bundled by this app | Review platform terms and distribution obligations. |
| Android Gradle Plugin 9.0.1 | Build-time Maven dependency only | Preserve its license/notice in any redistributed build tooling; no APK runtime code is intentionally added. |
| JUnit 4.13.2 | Test-only Maven dependency | Preserve its license/notice for redistributed test/build artifacts. |
| Fonts | No font file bundled; Android system fallback fonts only | Audit and record a license for every imported/bundled font before adding it. |
| Models/weights/synthetic voices | None bundled or downloaded; excluded from every phase of the non-AI release roadmap | Do not introduce model downloads, inference SDKs, AI services, or synthetic voice packs as part of planned work. |
| Music, sound effects, footage, stickers, templates, stock assets | None bundled | Record creator/source, exact license, attribution, redistribution, and export rights for each asset before adding it. |
| Codecs | Platform extractor/muxer APIs; no codec binary bundled | Codec patent, profile, and distribution obligations require independent legal review. |

No remote model, media-processing endpoint, or third-party asset source is configured. The [release roadmap](release-roadmap.md) excludes AI processing, whether local or remote, and does not plan a backend. Any future non-AI network feature requires a separate proposal and explicit consent identifying the operation, data, destination, retention, and deletion policy; keep local editing independent of backend availability.

## Name and release gates

“LibreFrame” is provisional. Perform trademark/name and package-name review before public release. Also review current Play target-API policy, codec/patent obligations, privacy disclosures, license notices, and accessibility/localization before release. No competitive feature, price, entitlement, or parity claim is made.
