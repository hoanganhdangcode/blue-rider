import 'package:core/core.dart';

/// Shared service configuration (base URL, timeouts, etc.).
class ServiceConfig {
  const ServiceConfig({
    required this.baseUrl,
    this.timeout = AppConstants.defaultTimeout,
  });

  final String baseUrl;
  final Duration timeout;
}
