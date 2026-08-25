import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:ui/ui.dart';

void main() {
  testWidgets('PrimaryButton shows label', (WidgetTester tester) async {
    await tester.pumpWidget(
      MaterialApp(
        theme: AppTheme.light,
        home: Scaffold(
          body: PrimaryButton(
            label: 'Continue',
            onPressed: () {},
          ),
        ),
      ),
    );

    expect(find.text('Continue'), findsOneWidget);
  });
}
