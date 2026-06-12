#!/usr/bin/env bash

set -euo pipefail

HOST="localhost"
PORT="4221"
N="${1:-3}"

REQUEST=$'GET / HTTP/1.1\r\nHost: localhost:4221\r\n\r\n'

for i in $(seq 1 "$N"); do
  (
    sleep 3
    printf "%s" "$REQUEST" | nc "$HOST" "$PORT"
  ) &
done

wait