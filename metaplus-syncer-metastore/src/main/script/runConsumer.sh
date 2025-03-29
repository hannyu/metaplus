#!/usr/bin/env bash

# 自动识别真实路径（兼容软链接）
SYNCER_HIVE_HOME="$(
  cd "$(dirname "$0")/.." &>/dev/null
  pwd -P
)"

# 自动匹配JAR文件（无需硬编码版本）
JAR_FILE=$(ls "$SYNCER_HIVE_HOME/libs"/*.jar | head -n 1)

exec java \
  -Xms512m -Xmx1024m \
  -Dloader.path="$SYNCER_HIVE_HOME/libs" \
  -Dspring.config.location="file:$SYNCER_HIVE_HOME/conf/" \
  -jar "$JAR_FILE" \
  "$@"

