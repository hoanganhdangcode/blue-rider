# Ride Platform — Architecture

## 1. Overview

Ride Platform là nền tảng đặt xe được xây dựng bằng Kotlin Multiplatform (KMP), với mục tiêu:

* App chính có thể phát triển độc lập.
* Business logic của Ride được đóng gói thành SDK.
* Khách hàng có thể lựa chọn:

    * Chỉ sử dụng `ridecore` và tự xây UI.
    * Sử dụng `rideui` + `ridecore` để tích hợp toàn bộ luồng Ride có sẵn.

Kiến trúc được chia thành 3 phần chính:

```text
                         APP
                       /     \
                      ▼       ▼
                   shared   rideui
                              │
                              ▼
                          ridecore
```

Trong đó:

* `shared`: code của app chính, không phải SDK thương mại.
* `ridecore`: business engine của Ride.
* `rideui`: UI SDK của Ride, sử dụng toàn bộ hạ tầng và nghiệp vụ từ `ridecore`.

---

# 2. Module Structure

```text
project/
│
├── shared/
│   ├── app/
│   ├── api/
│   ├── core/
│   ├── ui/
│   ├── feature/
│   └── ...
│
├── ridecore/
│   ├── commonMain/
│   ├── androidMain/
│   └── iosMain/
│
└── rideui/
    ├── commonMain/
    ├── androidMain/
    └── iosMain/
```

`shared` chỉ là một module của app và được tổ chức bằng folder.

`ridecore` và `rideui` là các module độc lập vì đây là các thành phần cần được đóng gói và phân phối dưới dạng SDK.

---

# 3. Dependency

Dependency direction:

```text
                         APP
                       /     \
                      ▼       ▼
                   shared   rideui
                              │
                              ▼
                          ridecore
```

Quy tắc:

```text
shared   → có thể sử dụng ridecore / rideui
rideui   → được phép sử dụng ridecore
ridecore → không phụ thuộc rideui
ridecore → không phụ thuộc shared
rideui   → không phụ thuộc shared
```

Không tạo dependency vòng:

```text
ridecore → rideui  ❌
rideui   → ridecore ✓
```

`ridecore` là tầng thấp hơn và phải độc lập với UI.

---

# 4. Shared

`shared` là phần code thuộc về app chính.

Nó không phải một phần của Ride SDK và không được đưa vào package thương mại của Ride.

Ví dụ:

```text
shared/
├── app/
├── api/
├── core/
├── ui/
├── feature/
└── navigation/
```

Có thể chứa:

* App entry point.
* App navigation.
* Authentication.
* User.
* Wallet.
* Notification.
* App-specific API.
* App-specific UI.
* App-specific infrastructure.
* Các feature không thuộc Ride.

`shared` có thể sử dụng SDK Ride:

```text
shared
   │
   ├── ridecore
   │
   └── rideui
```

Nhưng Ride SDK không được biết hoặc phụ thuộc vào implementation của `shared`.

---

# 5. Ride Core

`ridecore` là business engine của Ride.

Nó chứa toàn bộ logic cần thiết để vận hành luồng đặt xe mà không phụ thuộc vào UI.

Ví dụ:

```text
ridecore/
├── network/
├── storage/
├── location/
├── model/
├── repository/
├── service/
├── booking/
├── pricing/
├── driver/
├── trip/
└── payment/
```

Các trách nhiệm chính:

* Communication với backend Ride.
* Authentication/session liên quan Ride.
* Location.
* Booking.
* Pricing.
* Driver matching.
* Trip.
* Trip tracking.
* Cancellation.
* Payment liên quan Ride.
* State của Ride.
* Business rules.

`ridecore` không được phụ thuộc vào:

* Jetpack Compose.
* Android UI.
* `rideui`.
* `shared`.
* UI implementation của app host.

Mục tiêu:

> `ridecore` phải đủ khả năng để một khách hàng sử dụng business engine của Ride và tự xây toàn bộ UI của họ.

---

# 6. Ride UI

`rideui` là UI SDK của Ride.

Nó cung cấp các UI hoàn chỉnh cho các luồng Ride và sử dụng `ridecore` làm nền tảng.

```text
rideui
    │
    ▼
ridecore
```

Ví dụ:

```text
rideui/
├── booking/
├── tracking/
├── history/
├── component/
└── theme/
```

Các UI có thể bao gồm:

```text
RideBookingScreen
RideSearchingScreen
RideTrackingScreen
RideTripDetailScreen
RideHistoryScreen
```

`rideui` không tự xây lại:

* Network.
* API client.
* Repository.
* Database.
* Ride business logic.
* Ride state management độc lập.

Thay vào đó:

```text
UI
 ↓
ridecore
 ↓
API / Repository / Business Logic
```

Mục tiêu:

> Khách hàng có thể tích hợp toàn bộ luồng Ride mà không phải tự xây UI đặt xe.

---

# 7. SDK Products

Architecture hỗ trợ hai dạng sản phẩm.

## 7.1 Core SDK

Khách hàng chỉ sử dụng:

```text
ridecore
```

Flow:

```text
Customer App
     │
     ▼
ridecore
     │
     ├── Booking
     ├── Pricing
     ├── Driver
     ├── Trip
     ├── Location
     └── Payment
```

Khách hàng tự xây:

```text
UI
Navigation
UX
Design System
```

Ví dụ:

```kotlin
@Composable
fun CustomBookingScreen() {
    val state = rideCore.booking.state.collectAsState()

    // Customer-defined UI
}
```

Đây là sản phẩm dành cho khách hàng cần toàn quyền kiểm soát UI/UX.

---

## 7.2 Full UI SDK

Khách hàng sử dụng:

```text
rideui
    ↓
ridecore
```

Khách hàng chỉ cần tích hợp các UI được cung cấp:

```kotlin
RideBookingScreen()
RideTrackingScreen()
RideHistoryScreen()
```

Business logic vẫn chạy từ `ridecore`.

Không được duplicate business logic trong `rideui`.

---

# 8. KMP Strategy

`ridecore` và `rideui` được xây dựng theo KMP.

Shared implementation:

```text
commonMain
```

Platform-specific implementation:

```text
androidMain
iosMain
```

Ví dụ:

```text
ridecore/
├── commonMain/
│   ├── booking/
│   ├── trip/
│   ├── repository/
│   └── model/
│
├── androidMain/
│   └── platform-specific/
│
└── iosMain/
    └── platform-specific/
```

Mục tiêu là giữ business logic tối đa ở `commonMain`.

Platform-specific code chỉ được sử dụng khi thực sự cần native implementation.

---

# 9. Architecture Principles

## 9.1 Business logic không phụ thuộc UI

```text
ridecore
    ↑
rideui
```

Không được đảo chiều.

## 9.2 UI không tự xử lý business logic

`rideui` chỉ điều phối interaction và render state từ `ridecore`.

## 9.3 Shared không phải SDK

`shared` thuộc app chính.

Không đưa dependency của app vào Ride SDK chỉ để tái sử dụng code.

Nếu Ride thực sự cần một capability, capability đó phải thuộc `ridecore` hoặc được cung cấp thông qua abstraction/configuration.

## 9.4 Không duplicate infrastructure

Không tạo network stack thứ hai trong `rideui`.

Không tạo repository thứ hai trong `rideui`.

Không tạo business logic thứ hai trong `rideui`.

## 9.5 Product boundary phải rõ ràng

```text
shared
    = Application

ridecore
    = Ride Business Engine

rideui
    = Ride UI SDK
```

---

# 10. Integration Examples

## Custom UI

```text
Customer App
     │
     ▼
ridecore
     │
     ▼
Customer UI
```

Khách hàng toàn quyền thiết kế UI.

## Ready-made UI

```text
Customer App
     │
     ▼
rideui
     │
     ▼
ridecore
```

Khách hàng sử dụng UI có sẵn.

## Main App

```text
Main App
   │
   ├── shared
   │
   └── rideui
          │
          ▼
      ridecore
```

App chính vẫn giữ toàn bộ code riêng trong `shared`.

---

# 11. Non-Goals

Kiến trúc này không nhằm:

* Chia `shared` thành nhiều module nhỏ chỉ để đạt modular architecture.
* Tạo BaseFragment/BaseActivity cho mọi feature.
* Đưa toàn bộ code vào một module `shared` rồi gọi đó là SDK.
* Để `rideui` tự xây infrastructure riêng.
* Cho `ridecore` phụ thuộc vào UI.
* Ép khách hàng sử dụng UI nếu họ chỉ mua Core SDK.

---

# 12. Architectural Summary

```text
                               APP
                            /       \
                           /         \
                          ▼           ▼
                     ┌────────┐  ┌────────┐
                     │ shared │  │ rideui │
                     └────────┘  └────┬───┘
                                      │
                                      ▼
                                 ┌──────────┐
                                 │ ridecore │
                                 └──────────┘
```

### `shared`

Application code.

### `ridecore`

Ride business engine.

### `rideui`

Ready-made Ride UI built on top of `ridecore`.

### Commercial model

```text
CORE SDK
Customer
   │
   └── ridecore
          └── Custom UI


FULL SDK
Customer
   │
   └── rideui
          └── ridecore
```

Mục tiêu cuối cùng là biến `ridecore` và `rideui` thành **hai tầng sản phẩm có thể phân phối độc lập**, trong khi `shared` vẫn là code của app sử dụng chúng.
