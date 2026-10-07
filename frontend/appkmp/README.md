# blue-rider — KMP Mobile App

Kotlin Multiplatform app (Android + iOS) với Compose Multiplatform. Nằm trong mono-repo `blue-rider`.

## Modules

| Module | Vai trò |
|---|---|
| `shared` | App shell: entry point, `Platform` (expect/actual), account — phụ thuộc `rideui` + `ridecore` |
| `rideui` | Ride UI SDK (Compose Multiplatform, self-contained) — `api(project(":ridecore"))` |
| `ridecore` | Ride business engine: API client, booking domain — không UI, không Compose |
| `androidApp` | Host Android, chỉ phụ thuộc `shared` |
| `iosApp` | Host SwiftUI, gọi `MainViewController()` từ `shared` |

Dependency rule: `shared → rideui → ridecore`, `ridecore` không phụ thuộc module nào. Chi tiết: [`docs/Architecture.md`](./docs/Architecture.md).

## Build

```bash
# Android
./gradlew :androidApp:assembleDebug

# AAR (ridecore.aar, rideui.aar)
./gradlew createAars

# XCFramework (cần Xcode đầy đủ)
./gradlew createXCFrameworks

# Lint + unit test
./gradlew :ridecore:lint :rideui:lint :shared:lint :androidApp:lintDebug
```

iOS: mở `iosApp` trong Xcode (sau khi chạy `./gradlew createXCFrameworks` nếu build framework bằng Gradle).

## Yêu cầu

- JDK 21 (Gradle daemon toolchain — tự tải qua foojay), Android SDK `compileSdk 37` / `minSdk 24`
- macOS + Xcode để build iOS
