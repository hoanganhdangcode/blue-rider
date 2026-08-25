import 'package:flutter/widgets.dart';

import 'app_spacing.dart';

/// [SizedBox] dùng chung để chèn khoảng trống trong [Row] / [Column].
abstract final class AppGaps {
  /// Không khoảng trống.
  static const SizedBox zero = SizedBox.shrink();

  // Ngang — dùng trong Row
  static const SizedBox h4 = SizedBox(width: AppSpacing.s4);
  static const SizedBox h8 = SizedBox(width: AppSpacing.s8);
  static const SizedBox h12 = SizedBox(width: AppSpacing.s12);
  static const SizedBox h16 = SizedBox(width: AppSpacing.s16);
  static const SizedBox h24 = SizedBox(width: AppSpacing.s24);
  static const SizedBox h32 = SizedBox(width: AppSpacing.s32);

  // Dọc — dùng trong Column
  static const SizedBox v4 = SizedBox(height: AppSpacing.s4);
  static const SizedBox v8 = SizedBox(height: AppSpacing.s8);
  static const SizedBox v12 = SizedBox(height: AppSpacing.s12);
  static const SizedBox v16 = SizedBox(height: AppSpacing.s16);
  static const SizedBox v24 = SizedBox(height: AppSpacing.s24);
  static const SizedBox v32 = SizedBox(height: AppSpacing.s32);
  static const SizedBox v48 = SizedBox(height: AppSpacing.s48);

  /// Khoảng cách giữa header và nội dung bên dưới.
  static const SizedBox afterHeader = SizedBox(height: AppSpacing.s16);

  /// Khoảng cách giữa các khối lớn trên màn hình.
  static const SizedBox section = SizedBox(height: AppSpacing.section);
}
