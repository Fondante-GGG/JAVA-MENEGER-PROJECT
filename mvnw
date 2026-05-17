#!/bin/sh
# Apache Maven Wrapper
# https://maven.apache.org/wrapper/

set -euf

BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)
WRAPPER_DIR="$BASE_DIR/.mvn/wrapper"
JAR="$WRAPPER_DIR/maven-wrapper.jar"
PROPS="$WRAPPER_DIR/maven-wrapper.properties"

if [ ! -f "$PROPS" ]; then
  echo "Missing $PROPS" >&2
  exit 1
fi

if [ ! -f "$JAR" ]; then
  WRAPPER_URL=$(grep -E '^wrapperUrl=' "$PROPS" | cut -d= -f2-)
  if [ -z "${WRAPPER_URL:-}" ]; then
    echo "Missing wrapperUrl in $PROPS" >&2
    exit 1
  fi
  echo "Downloading Maven wrapper jar..." >&2
  mkdir -p "$WRAPPER_DIR"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL -o "$JAR" "$WRAPPER_URL"
  elif command -v wget >/dev/null 2>&1; then
    wget -q -O "$JAR" "$WRAPPER_URL"
  else
    echo "Need curl or wget to download Maven wrapper" >&2
    exit 1
  fi
fi

JAVA_EXEC="${JAVA_HOME:-}/bin/java"
if [ ! -x "$JAVA_EXEC" ]; then
  JAVA_EXEC=java
fi

exec "$JAVA_EXEC" -jar "$JAR" -Dmaven.multiModuleProjectDirectory="$BASE_DIR" "$@"

