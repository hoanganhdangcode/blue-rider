import 'package:flutter/widgets.dart';

import 'app_spacing.dart';

/// [EdgeInsets] dùng chung cho màn hình, header và các bề mặt UI.
abstract final class AppInsets {
  /// Padding nội dung toàn màn hình (4 phía).
  static const EdgeInsets page = EdgeInsets.all(AppSpacing.page);

  /// Chỉ padding ngang màn hình.
  static const EdgeInsets pageHorizontal = EdgeInsets.symmetric(
    horizontal: AppSpacing.page,
  );

  /// Chỉ padding dọc màn hình.
  static const EdgeInsets pageVertical = EdgeInsets.symmetric(
    vertical: AppSpacing.page,
  );

  /// Padding nội dung header / app-bar tùy chỉnh.
  static const EdgeInsets header = EdgeInsets.symmetric(
    horizontal: AppSpacing.page,
    vertical: AppSpacing.headerVertical,
  );

  /// Padding header cộng thêm phần status bar (safe area trên).
  static EdgeInsets headerSafe(BuildContext context) {
    return header.copyWith(
      top: AppSpacing.headerVertical + MediaQuery.paddingOf(context).top,
    );
  }

  /// Padding màn hình cộng safe area thiết bị (notch, home indicator).
  static EdgeInsets pageSafe(BuildContext context) {
    return page + MediaQuery.paddingOf(context);
  }

  /// Padding bên trong card.
  static const EdgeInsets card = EdgeInsets.all(AppSpacing.card);

  /// Padding card dạng gọn.
  static const EdgeInsets cardCompact = EdgeInsets.all(AppSpacing.s12);

  /// Padding một dòng trong list.
  static const EdgeInsets listItem = EdgeInsets.symmetric(
    horizontal: AppSpacing.page,
    vertical: AppSpacing.s12,
  );

  /// Padding nút bấm.
  static const EdgeInsets button = EdgeInsets.symmetric(
    horizontal: AppSpacing.s24,
    vertical: AppSpacing.s12,
  );

  /// Padding ô nhập liệu.
  static const EdgeInsets input = EdgeInsets.symmetric(
    horizontal: AppSpacing.s16,
    vertical: AppSpacing.s12,
  );

  /// Padding dialog.
  static const EdgeInsets dialog = EdgeInsets.all(AppSpacing.s24);

  /// Padding bottom sheet.
  static const EdgeInsets bottomSheet = EdgeInsets.fromLTRB(
    AppSpacing.page,
    AppSpacing.s12,
    AppSpacing.page,
    AppSpacing.s24,
  );

  /// Padding chip / tag.
  static const EdgeInsets chip = EdgeInsets.symmetric(
    horizontal: AppSpacing.s12,
    vertical: AppSpacing.s4,
  );

  /// Padding quanh icon bấm được (touch target).
  static const EdgeInsets icon = EdgeInsets.all(AppSpacing.s8);
}
