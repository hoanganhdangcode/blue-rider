/// Thang khoảng cách (lưới 4pt) và kích thước ngữ nghĩa dùng chung cho mọi app.
abstract final class AppSpacing {
  // --- Thang số ---

  /// Không khoảng cách.
  static const double s0 = 0;

  /// Rất nhỏ — chỉnh lệch icon, viền mỏng.
  static const double s2 = 2;

  /// Nhỏ — khoảng cách nhãn với control.
  static const double s4 = 4;

  /// Item liên quan (icon + chữ, các field xếp sát).
  static const double s8 = 8;

  /// Padding dọc header / list item.
  static const double s12 = 12;

  /// Padding màn hình / card mặc định.
  static const double s16 = 16;

  static const double s20 = 20;

  /// Khoảng cách giữa các section.
  static const double s24 = 24;

  static const double s32 = 32;
  static const double s40 = 40;

  /// Header thu gọn (modal, màn lồng).
  static const double s48 = 48;

  /// Chiều cao AppBar / header mặc định.
  static const double s56 = 56;

  static const double s64 = 64;

  // --- Ngữ nghĩa (dùng theo ngữ cảnh, không cần nhớ số) ---

  /// Padding ngang màn hình, header và list.
  static const double page = s16;

  /// Padding dọc bên trong header tùy chỉnh.
  static const double headerVertical = s12;

  /// Chiều cao AppBar / header mặc định.
  static const double headerHeight = s56;

  /// Header thu gọn (màn lồng hoặc modal).
  static const double headerCompactHeight = s48;

  /// Khoảng cách giữa các khối nội dung trên màn hình.
  static const double section = s24;

  /// Padding bên trong card / tile.
  static const double card = s16;

  /// Khoảng cách giữa các item liên quan.
  static const double item = s8;

  /// Khoảng cách giữa nhãn và control.
  static const double label = s4;
}
