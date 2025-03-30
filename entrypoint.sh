#!/bin/bash

if [ -n "${JAVA_OPTS}" ]; then
  echo "Using JAVA_OPTS: $JAVA_OPTS"
fi

echo "Starting java application.."
exec java ${JAVA_OPTS} -jar /opt/elephub-service/app.jar