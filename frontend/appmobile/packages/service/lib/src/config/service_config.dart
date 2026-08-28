import 'package:core/core.dart';

/// Shared service configuration (base URL, timeouts, etc.).
class ServiceConfig {
  const ServiceConfig({
    required this.baseUrl,
    this.timeout = AppConstants.defaultTimeout,
  });

  /// Reads [baseUrl] from the `API_BASE_URL` compile-time environment
  /// variable, set per environment via `--dart-define` or
  /// `--dart-define-from-file` (see frontend/appmobile/env/*.json.example).
  /// Defaults to the local loopback backend when not provided.
  factory ServiceConfig.fromEnvironment({
    Duration timeout = AppConstants.defaultTimeout,
  }) {
    return ServiceConfig(
      baseUrl: const String.fromEnvironment(
        'API_BASE_URL',
        defaultValue: 'http://localhost:8080',
      ),
      timeout: timeout,
    );
  }

  final String baseUrl;
  final Duration timeout;
}
