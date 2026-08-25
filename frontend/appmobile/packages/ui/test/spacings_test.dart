import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:ui/ui.dart';

void main() {
  test('AppSpacing uses a 4pt grid', () {
    expect(AppSpacing.s4, 4);
    expect(AppSpacing.s8, 8);
    expect(AppSpacing.s16, 16);
    expect(AppSpacing.page, AppSpacing.s16);
    expect(AppSpacing.headerHeight, AppSpacing.s56);
    expect(AppSpacing.headerVertical, AppSpacing.s12);
  });

  test('AppInsets page and header match semantic spacing', () {
    expect(AppInsets.page, const EdgeInsets.all(AppSpacing.page));
    expect(
      AppInsets.header,
      const EdgeInsets.symmetric(
        horizontal: AppSpacing.page,
        vertical: AppSpacing.headerVertical,
      ),
    );
  });

  testWidgets('AppInsets.headerSafe adds status-bar inset', (tester) async {
    late EdgeInsets insets;

    await tester.pumpWidget(
      MediaQuery(
        data: const MediaQueryData(padding: EdgeInsets.only(top: 44)),
        child: Builder(
          builder: (context) {
            insets = AppInsets.headerSafe(context);
            return const SizedBox.shrink();
          },
        ),
      ),
    );

    expect(insets.top, AppSpacing.headerVertical + 44);
    expect(insets.left, AppSpacing.page);
    expect(insets.right, AppSpacing.page);
  });

  test('AppGaps match the spacing scale', () {
    expect(AppGaps.v16.height, AppSpacing.s16);
    expect(AppGaps.h8.width, AppSpacing.s8);
    expect(AppGaps.afterHeader.height, AppSpacing.s16);
    expect(AppGaps.section.height, AppSpacing.section);
  });
}
