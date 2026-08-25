import 'package:flutter/material.dart';

import '../spacings/app_insets.dart';

/// Reusable primary action button shared across apps.
class PrimaryButton extends StatelessWidget {
  const PrimaryButton({
    super.key,
    required this.label,
    required this.onPressed,
  });

  final String label;
  final VoidCallback? onPressed;

  @override
  Widget build(BuildContext context) {
    return FilledButton(
      onPressed: onPressed,
      style: FilledButton.styleFrom(padding: AppInsets.button),
      child: Text(label),
    );
  }
}
