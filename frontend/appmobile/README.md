# AppMobile

Monorepo Flutter gồm **2 app** và **3 package dùng chung**, quản lý bằng **Dart Pub Workspaces** + **Melos**.

Yêu cầu: Flutter với **Dart ≥ 3.8** (`sdk: ^3.8.0`).

> Luôn mở thư mục **root** `AppMobile` trong IDE để workspace resolve đúng.

---

## Cấu trúc thư mục

```
AppMobile/
├── apps/                 # Ứng dụng chạy được (build APK/IPA)
│   ├── app_a/
│   └── app_b/
├── packages/             # Thư viện dùng chung (không phải app)
│   ├── core/             # Hằng số, lỗi, Result (pure Dart)
│   ├── ui/               # Theme, widget dùng chung (Flutter)
│   └── service/          # API / service dùng chung (phụ thuộc core)
└── pubspec.yaml          # Khai báo workspace + Melos scripts
```

| Thành phần | Vai trò |
|------------|---------|
| `apps/*` | App độc lập, có màn hình riêng, có thể chạy / build riêng |
| `packages/core` | Logic nền: constants, `AppException`, `Result` — **không UI** |
| `packages/ui` | Theme (`AppTheme`), widget dùng chung (`PrimaryButton`) |
| `packages/service` | Client API / service dùng chung (`ApiClient`, `ServiceConfig`) |

---

## Viết code ở đâu?

| Cần làm gì | Đặt vào |
|------------|---------|
| Hằng số, model thuần, lỗi dùng chung, `Result` | `packages/core` |
| Theme, button, component UI dùng chung | `packages/ui` |
| Gọi API, service dùng chung giữa các app | `packages/service` |
| Màn hình / flow chỉ của một app | `apps/app_a` hoặc `apps/app_b` |

### Hướng phụ thuộc (bắt buộc)

```
apps  →  ui / service  →  core
```

- App **chỉ** phụ thuộc `packages/*`.
- App **không** phụ thuộc app khác.
- Package **không** phụ thuộc bất kỳ app nào.
- `ui` và `service` có thể dùng `core`; `core` không phụ thuộc `ui`/`service`.

---

## Cài đặt môi trường (một lần)

```bash
# Cài Melos (nếu chưa có)
dart pub global activate melos

# Ở thư mục root AppMobile
melos bootstrap

# Hoặc chỉ resolve dependency:
dart pub get
```

---

## Chạy từng app

Mỗi app chạy độc lập:

```bash
# App A
cd apps/app_a
flutter run

# App B
cd apps/app_b
flutter run
```

Hoặc từ root (sau khi `cd` vào đúng app):

```bash
flutter run -d <device_id>
```

---

## Build từng app

```bash
# Android APK — App A
cd apps/app_a
flutter build apk

# Android APK — App B
cd apps/app_b
flutter build apk

# iOS (chỉ trên máy Mac)
cd apps/app_a
flutter build ipa
```

Package (`core` / `ui` / `service`) **không** build APK — chúng là thư viện, chỉ analyze / test.

---

## Làm việc với từng package độc lập

```bash
# Phân tích / test core (pure Dart)
cd packages/core
dart analyze
dart test

# UI (Flutter)
cd packages/ui
flutter analyze
flutter test

# Service (pure Dart)
cd packages/service
dart analyze
dart test
```

### Chạy cho cả repo (Melos)

```bash
# Ở root
melos run analyze
melos run test
```

---

## Import trong code

```dart
// Trong app
import 'package:core/core.dart';
import 'package:ui/ui.dart';
import 'package:service/service.dart';

// Ví dụ dùng UI chung
MaterialApp(
  theme: AppTheme.light,
  home: PrimaryButton(label: 'OK', onPressed: () {}),
);
```

Khai báo dependency trong `pubspec.yaml` của app (đã có sẵn):

```yaml
dependencies:
  core:
    path: ../../packages/core
  ui:
    path: ../../packages/ui
  service:
    path: ../../packages/service
```

---

## Checklist người mới join

1. Clone repo, mở **root** `AppMobile` trong IDE.
2. Chạy `melos bootstrap` (hoặc `dart pub get`).
3. Chạy thử: `cd apps/app_a && flutter run`.
4. Trước khi thêm code: xem bảng **Viết code ở đâu?** ở trên.
5. Sau khi sửa: `melos run analyze` và `melos run test`.

---

## Quy tắc nhanh

- Mở đúng **root** workspace, không mở riêng từng app nếu muốn resolve package path.
- Code dùng chung → `packages/*`; code riêng app → `apps/*`.
- Giữ dependency một chiều: `apps` → `ui`/`service` → `core`.
