import 'package:service/service.dart';
import 'package:test/test.dart';

void main() {
  test('ApiClient get returns Success for non-empty path', () async {
    final client = ApiClient(
      config: const ServiceConfig(baseUrl: 'https://api.example.com'),
    );

    final result = await client.get('/health');

    expect(result.isSuccess, isTrue);
    expect(
      result.when(success: (v) => v, failure: (_) => ''),
      'https://api.example.com/health',
    );
  });

  test('ApiClient get returns Failure for empty path', () async {
    final client = ApiClient(
      config: const ServiceConfig(baseUrl: 'https://api.example.com'),
    );

    final result = await client.get('');

    expect(result.isFailure, isTrue);
  });
}
