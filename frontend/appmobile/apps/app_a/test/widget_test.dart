import 'package:flutter_test/flutter_test.dart';
import 'package:ui/ui.dart';

import 'package:app_a/main.dart';

void main() {
  testWidgets('Counter increments via PrimaryButton', (WidgetTester tester) async {
    await tester.pumpWidget(const AppA());

    expect(find.text('Count: 0'), findsOneWidget);
    expect(find.text('Count: 1'), findsNothing);

    await tester.tap(find.widgetWithText(PrimaryButton, 'Increment'));
    await tester.pump();

    expect(find.text('Count: 0'), findsNothing);
    expect(find.text('Count: 1'), findsOneWidget);
  });
}
