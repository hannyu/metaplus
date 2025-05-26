#!/bin/bash

# 启动Spring Boot应用（需与JAR同目录）
APP_HOME=$(cd "$(dirname "$0")/.."; pwd)
cd "$APP_HOME"

# 使用外部config目录的配置（优先级最高）
JAVA_OPTS="-Dspring.config.location=file:${APP_HOME}/config/"

# 启动命令
java $JAVA_OPTS -jar ${APP_HOME}/lib/${appName} \
    --logging.file.path=${APP_HOME}/log \
    >> ${APP_HOME}/log/console.log 2>&1 &

echo "Application started. Log: ${APP_HOME}/log/console.log"