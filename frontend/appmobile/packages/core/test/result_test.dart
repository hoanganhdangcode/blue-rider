import 'package:core/core.dart';
import 'package:test/test.dart';

void main() {
  test('Success holds value', () {
    const result = Success<int>(42);
    expect(result.isSuccess, isTrue);
    expect(
      result.when(success: (v) => v, failure: (_) => -1),
      42,
    );
  });

  test('Failure holds error', () {
    const result = Failure<int>(AppException('boom', code: 'X'));
    expect(result.isFailure, isTrue);
    expect(
      result.when(success: (_) => '', failure: (e) => e.code),
      'X',
    );
  });
}
