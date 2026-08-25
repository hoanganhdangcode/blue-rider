import 'package:flutter/material.dart';

import '../spacings/app_spacing.dart';

/// Shared Material theme used by all apps.
abstract final class AppTheme {
  static const Color seedColor = Color(0xFF1565C0);

  static ThemeData get light {
    return ThemeData(
      colorScheme: ColorScheme.fromSeed(seedColor: seedColor),
      useMaterial3: true,
      appBarTheme: const AppBarTheme(
        toolbarHeight: AppSpacing.headerHeight,
        titleSpacing: AppSpacing.page,
      ),
    );
  }
}
