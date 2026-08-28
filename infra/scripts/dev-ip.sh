#!/usr/bin/env bash
# Print this machine's LAN IP and the ready-to-use URLs/flags for local dev
# (features branch): Android device/emulator on the same Wi-Fi/LAN talks to
# the backend through this IP, no Nginx involved.
set -euo pipefail

GATEWAY_PORT="${GATEWAY_PORT:-8080}"

detect_ip() {
  if command -v ip >/dev/null 2>&1; then
    ip -4 route get 1.1.1.1 2>/dev/null | awk '/src/ {for (i=1;i<=NF;i++) if ($i=="src") print $(i+1)}' | head -n1
  elif command -v ipconfig >/dev/null 2>&1; then
    ipconfig getifaddr en0 2>/dev/null || ipconfig getifaddr en1 2>/dev/null
  elif command -v hostname >/dev/null 2>&1; then
    hostname -I 2>/dev/null | awk '{print $1}'
  fi
}

LAN_IP="$(detect_ip || true)"

if [ -z "${LAN_IP:-}" ]; then
  echo "Không tự phát hiện được LAN IP. Hãy tìm thủ công (ip addr / ifconfig / ipconfig) rồi thay vào các lệnh dưới." >&2
  LAN_IP="<YOUR_LAN_IP>"
fi

cat <<EOF
LAN IP hiện tại : ${LAN_IP}
Gateway URL     : http://${LAN_IP}:${GATEWAY_PORT}

1) Chạy backend local (không Nginx), từ thư mục gốc repo:
   cd infra/docker
   docker compose --env-file ../.env -f compose.yml -f compose.local.yml up -d --build

2) Smoke test từ máy khác trong LAN:
   curl -s -X POST http://${LAN_IP}:${GATEWAY_PORT}/api \\
     -H "X-API-ACTION: auth.ping" -H "Content-Type: application/json"

3) Trỏ app Flutter vào backend LAN — cách nhanh (chạy trong apps/app_a hoặc apps/app_b):
   flutter run --dart-define=API_BASE_URL=http://${LAN_IP}:${GATEWAY_PORT}

   Hoặc ghi 1 lần vào file local dùng chung 2 app (không commit — xem
   frontend/appmobile/env/local.json.example), từ thư mục gốc repo:
   mkdir -p frontend/appmobile/env
   printf '{"API_BASE_URL": "http://%s:%s"}' "${LAN_IP}" "${GATEWAY_PORT}" > frontend/appmobile/env/local.json

   Rồi chạy từ apps/app_a hoặc apps/app_b:
   flutter run --dart-define-from-file=../../env/local.json
EOF
