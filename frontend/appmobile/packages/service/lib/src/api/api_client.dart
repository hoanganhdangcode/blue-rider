import 'package:core/core.dart';

import '../config/service_config.dart';

/// Minimal HTTP-agnostic API client skeleton shared by apps.
class ApiClient {
  ApiClient({required this.config});

  final ServiceConfig config;

  /// Placeholder GET that returns [Result] for callers to handle.
  Future<Result<String>> get(String path) async {
    if (path.isEmpty) {
      return const Failure(AppException('Path must not be empty', code: 'bad_path'));
    }
    // Wire a real HTTP client here later (dio / http).
    return Success('${config.baseUrl}$path');
  }
}
