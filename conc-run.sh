#!/usr/bin/env bash

set -u

N="${1:-10}"
URL="http://127.0.0.1:4221/"
EXPECTED_STATUS="${2:-200}"
TIMEOUT_SECONDS="${3:-3}"

TMP_DIR="$(mktemp -d)"
failures=0

make_request() {
  local id="$1"
  local out_file="$TMP_DIR/response_$id.txt"
  local meta_file="$TMP_DIR/meta_$id.txt"

  echo "[$id] START"

  curl \
    --silent \
    --show-error \
    --include \
    --max-time "$TIMEOUT_SECONDS" \
    --write-out "\nCURL_HTTP_CODE=%{http_code}\nCURL_EXIT_CODE=%{exitcode}\n" \
    "$URL" > "$out_file" 2> "$meta_file"

  local curl_exit="$?"
  local status

  status="$(grep '^CURL_HTTP_CODE=' "$out_file" | cut -d= -f2)"

  echo "========== RESPONSE $id =========="
  cat "$out_file"

  if [[ -s "$meta_file" ]]; then
    echo "========== CURL STDERR $id =========="
    cat "$meta_file"
  fi

  echo "========== RESULT $id =========="

  if [[ "$curl_exit" -eq 0 && "$status" == "$EXPECTED_STATUS" ]]; then
    echo "[$id] OK status=$status"
    return 0
  else
    echo "[$id] FAIL curl_exit=$curl_exit status=$status expected=$EXPECTED_STATUS"
    return 1
  fi
}

for i in $(seq 1 "$N"); do
  make_request "$i" &
done

for job in $(jobs -p); do
  if ! wait "$job"; then
    failures=$((failures + 1))
  fi
done

rm -rf "$TMP_DIR"

echo "========== SUMMARY =========="

if [[ "$failures" -eq 0 ]]; then
  echo "All $N requests passed"
  exit 0
else
  echo "$failures / $N requests failed"
  exit 1
fi