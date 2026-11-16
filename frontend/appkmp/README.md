# blue-rider — KMP Mobile App

Kotlin Multiplatform app (Android + iOS) với Compose Multiplatform. Nằm trong mono-repo `blue-rider`.

## Modules

| Module | Vai trò |
|---|---|
| `shared` | Toàn bộ app: entry (`presentation/App.kt`), `Platform` (`core/util`) |
| `androidApp` | Host Android, chỉ phụ thuộc `shared` |
| `iosApp` | Host SwiftUI, gọi `MainViewController()` từ `shared` |

## Build

```bash
# Android
./gradlew :androidApp:assembleDebug

# XCFramework (cần Xcode đầy đủ)
./gradlew createXCFrameworks

# Lint + unit test
./gradlew :shared:lint :androidApp:lintDebug
```

iOS: mở `iosApp` trong Xcode (sau khi chạy `./gradlew createXCFrameworks` nếu build framework bằng Gradle).

## Yêu cầu

- JDK 21 (Gradle daemon toolchain — tự tải qua foojay), Android SDK `compileSdk 37` / `minSdk 24`
- macOS + Xcode để build iOS
