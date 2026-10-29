# OpenVideoEdit

LibreFrame is a provisional working name for an original, free, local-first Android video editor. The name has not been cleared for public release. This repository is an early M0 foundation, not a feature-complete editor or a claim of parity with another product.

## Current build

The Android app provides a project list, video import through Android's document picker, a platform video preview, saved trim ranges, and MP4 sample-copy export. Project metadata is stored privately on-device; source media is not copied or uploaded.

The first exporter copies encoded audio/video samples. It may start at the preceding video keyframe, does not re-encode, cannot mix tracks, and does not render text or effects. It can fail for unsupported source codecs or document providers. These are explicit M0 limitations, not export-quality controls.

See [the feature matrix](docs/feature-matrix.md) for all F01–F75 statuses, [architecture and validation](docs/architecture.md), and [privacy and licensing](docs/privacy-and-licenses.md).

## Build

Requirements: JDK 17+, Android SDK Platform 36, and Android Gradle Plugin 9.0.1. The repository uses Gradle Kotlin DSL. If Gradle dependencies are unavailable, Android Studio can sync the project once Google Maven and Maven Central are reachable.

```sh
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

The app's minimum SDK is API 29; target SDK 36 is provisional and must be checked against Play release policy before publication. No reference-device or performance acceptance result has been measured yet.

## Free access and network

The app has no account, billing, ads, analytics, paid assets, model downloads, or network permission. Editing and the implemented export path are local. Unsupported tools are not represented as available actions.

## License

Original application code is licensed under Apache-2.0; see [LICENSE](LICENSE) and [the asset/dependency manifest](docs/privacy-and-licenses.md). Codec patent and distribution obligations require separate release review.
